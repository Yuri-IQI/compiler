package com.inm.semantic;

import com.inm.antlr4.ProgramBaseListener;
import com.inm.antlr4.ProgramParser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAndIntermediateListener extends ProgramBaseListener {

    private final SymbolTable symbolTable = new SymbolTable();
    private final ThreeAddressCode tac = new ThreeAddressCode();

    // Pilha auxiliar para gerenciar os resultados temporários das expressões
    private final Stack<TypedOperand> exprStack = new Stack<>();

    // Pilha para gerenciar os rótulos de desvios de controle de fluxo
    private final Stack<String> labelStack = new Stack<>();

    private int errorCount = 0;

    public int getErrorCount() { return errorCount; }

    private void reportSemanticError(Token token, String message) {
        this.errorCount++;
        System.err.println("Erro Semântico [" + token.getLine() + ":" + token.getCharPositionInLine() + "]: " + message);
    }

    public String getGenerated3AC() {
        if (errorCount > 0) {
            return "Código intermediario não gerado devido a " + errorCount + " erro(s) semânticos(s).";
        }
        return tac.getCode();
    }


    // --- 1. CAPTURA DE DECLARAÇÃO DE VARIÁVEIS ---
    @Override
    public void exitDeclTip(ProgramParser.DeclTipContext ctx) {
        List<String> idList = new ArrayList<>();
        if (ctx.listId() != null && ctx.listId().ID() != null) {
            idList.add(ctx.listId().ID().getText());
            findIdsInLine(ctx.listId().listIdLine(), idList);
        }

        if (ctx.tip() != null && !idList.isEmpty()) {
            String type = ctx.tip().getText();
            Token startToken = ctx.getStart();
            for(String id : idList) {
                symbolTable.declare(id, type, startToken.getLine(), startToken.getCharPositionInLine());
            }
        }
    }

    private void findIdsInLine(ProgramParser.ListIdLineContext lineCtx, List<String> idList) {
        if (lineCtx.ID() != null) {
            String idName = lineCtx.ID().getText();
            if (!idList.contains(idName)) {
                idList.add(idName);
            }
        }
        if (lineCtx.listIdLine() != null) {
            findIdsInLine(lineCtx.listIdLine(), idList);
        }
    }

    // --- 2. COMANDO DE ATRIBUIÇÃO ---
    @Override
    public void exitCmdAtrib(ProgramParser.CmdAtribContext ctx) {
        if (ctx.ID() != null) {
            String idName = ctx.ID().getText();
            Token idToken = ctx.ID().getSymbol();

            // Pega o tipo da variável destino
            String idType = symbolTable.getType(idName, idToken.getLine(), idToken.getCharPositionInLine());

            if (!exprStack.isEmpty()) {
                TypedOperand exprResult = exprStack.pop();

                // VALIDAÇÃO: Tipo da variável destino deve ser igual ao tipo da expressão produzida
                if (idType != null && !idType.toLowerCase().equals(exprResult.getType())) {
                    reportSemanticError(idToken, "Incompatibilidade de tipos. Não é possível atribuir "
                            + exprResult.getType() + " à variável '" + idName + "' do tipo " + idType.toUpperCase());
                }

                tac.emit(idName + " = " + exprResult.getValue());
            }
        }
    }

    // --- 3. OPERAÇÕES MATEMÁTICAS, RELACIONAIS E LOGICAS (EXPR) ---
    @Override
    public void exitExprRel(ProgramParser.ExprRelContext ctx) {
        if (ctx.exprRelLine() != null && ctx.exprRelLine().OPREL() != null) {
            if (exprStack.size() >= 2) {
                TypedOperand right = exprStack.pop();
                TypedOperand left = exprStack.pop();
                String op = ctx.exprRelLine().OPREL().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "boolean"));
            }
        }

        if (!labelStack.isEmpty() && !exprStack.isEmpty()) {
            TypedOperand condicao = exprStack.peek(); // Olhamos sem remover ainda
            Token t = ctx.getStart();

            // VALIDAÇÃO: Condição estrutural precisa ser estritamente booleana
            if (!condicao.getType().equals("boolean")) {
                reportSemanticError(t, "Expressão condicional deve resultar em tipo boolean, mas resultou em " + condicao.getType());
            }

            if (ctx.getParent() instanceof ProgramParser.CmdIfContext) {
                exprStack.pop();
                String labelElse = labelStack.peek();
                tac.emit("ifFalse " + condicao.getValue() + " goto " + labelElse);
            } else if (ctx.getParent() instanceof ProgramParser.CmdWhileContext) {
                exprStack.pop();
                String labelEnd = labelStack.get(labelStack.size() - 2);
                tac.emit("ifFalse " + condicao.getValue() + " goto " + labelEnd);
            }
        }
    }

    @Override
    public void exitExprLog(ProgramParser.ExprLogContext ctx) {
        if (ctx.exprLogLine() != null && ctx.exprLogLine().OPLOG() != null) {
            if (exprStack.size() >= 2) {
                TypedOperand right = exprStack.pop();
                TypedOperand left = exprStack.pop();
                String op = ctx.exprLogLine().OPLOG().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "boolean"));
            }
        }
    }

    @Override
    public void exitExprLogLine(ProgramParser.ExprLogLineContext ctx) {
        if (ctx.OPLOG() != null) {
            if (exprStack.size() >= 2) {
                TypedOperand right = exprStack.pop();
                TypedOperand left = exprStack.pop();
                String op = ctx.OPLOG().getText();
                Token t = ctx.getStart();

                if (!left.getType().equals("boolean") || !right.getType().equals("boolean")) {
                    reportSemanticError(t, "Operação lógica '" + op + "' inválida entre os tipos " + left.getType() + " e " + right.getType());
                }

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "boolean"));
            }
        }
    }

    @Override
    public void exitExprAd(ProgramParser.ExprAdContext ctx) {
        // Deixamos a sub-regra de cauda (exitExprAdLine) emitir as operações.
    }

    @Override
    public void exitExprAdLine(ProgramParser.ExprAdLineContext ctx) {
        if (ctx.OPAD() != null) {
            if (exprStack.size() >= 2) {
                TypedOperand right = exprStack.pop();
                TypedOperand left = exprStack.pop();
                String op = ctx.OPAD().getText();
                Token t = ctx.getStart();

                // VALIDAÇÃO: Adição e Subtração exigem tipos numéricos (integer)
                if (!left.getType().equals("integer") || !right.getType().equals("integer")) {
                    reportSemanticError(t, "Operação '" + op + "' inválida entre os tipos " + left.getType() + " e " + right.getType());
                }

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "integer"));
            }
        }
    }

    @Override
    public void exitExprMult(ProgramParser.ExprMultContext ctx) {
        // Deixamos a sub-regra de cauda (exitExprMultLine) emitir as operações.
    }

    @Override
    public void exitExprMultLine(ProgramParser.ExprMultLineContext ctx) {
        if (ctx.OPMULT() != null) {
            if (exprStack.size() >= 2) {
                TypedOperand right = exprStack.pop();
                TypedOperand left = exprStack.pop();
                String op = ctx.OPMULT().getText();
                Token t = ctx.getStart();

                // VALIDAÇÃO: Multiplicação e Divisão exigem inteiros
                if (!left.getType().equals("integer") || !right.getType().equals("integer")) {
                    reportSemanticError(t, "Operação '" + op + "' inválida entre os tipos " + left.getType() + " e " + right.getType());
                }

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "integer"));
            }
        }
    }

    @Override
    public void exitExprNeg(ProgramParser.ExprNegContext ctx) {
        if (ctx.OPNEG() != null) {
            if (!exprStack.isEmpty()) {
                TypedOperand right = exprStack.pop();
                Token t = ctx.getStart();

                // VALIDAÇÃO: Operador unário de negação '~' exige booleano
                if (!right.getType().equals("boolean")) {
                    reportSemanticError(t, "Operador unário '~' inválido para o tipo " + right.getType());
                }

                String temp = tac.newTemp();
                tac.emit(temp + " = ~" + right.getValue());
                exprStack.push(new TypedOperand(temp, "boolean"));
            }
        }
    }

    // --- 4. CONTROLE DE FLUXO (IF / WHILE) ---
    @Override
    public void enterCmdIf(ProgramParser.CmdIfContext ctx) {
        String labelElse = tac.newLabel();
        String labelEnd = tac.newLabel();

        labelStack.push(labelEnd);
        labelStack.push(labelElse);
    }

    @Override
    public void exitCmdIf(ProgramParser.CmdIfContext ctx) {
        if (!labelStack.isEmpty()) {
            String labelElse = labelStack.pop();
            String labelEnd = labelStack.pop();

            if (!tac.getCode().contains(labelElse + ":")) {
                tac.emit(labelElse + ":");
            }

            tac.emit(labelEnd + ":");
        }
    }

    @Override
    public void enterCmdIfLine(ProgramParser.CmdIfLineContext ctx) {
        if (labelStack.size() >= 2 && ctx.ELSE() != null) {
            String labelElse = labelStack.peek();
            String labelEnd = labelStack.get(labelStack.size() - 2);

            tac.emit("goto " + labelEnd);
            tac.emit(labelElse + ":");
        }
    }

    @Override
    public void enterCmdWhile(ProgramParser.CmdWhileContext ctx) {
        String labelStart = tac.newLabel();
        String labelEnd = tac.newLabel();

        tac.emit(labelStart + ":");

        labelStack.push(labelEnd);
        labelStack.push(labelStart);
    }

    @Override
    public void exitCmdWhile(ProgramParser.CmdWhileContext ctx) {
        if (labelStack.size() >= 2) {
            String labelStart = labelStack.pop();
            String labelEnd = labelStack.pop();

            tac.emit("goto " + labelStart);
            tac.emit(labelEnd + ":");
        }
    }

    // --- 5. VALORES FOLHA / TERMINAIS (BASE DA ARVORE) ---
    @Override
    public void exitExprPar(ProgramParser.ExprParContext ctx) {
        if (ctx.ID() != null) {
            String idName = ctx.ID().getText();
            Token t = ctx.ID().getSymbol();
            String idType = symbolTable.getType(idName, t.getLine(), t.getCharPositionInLine());
            exprStack.push(new TypedOperand(idName, idType != null ? idType.toLowerCase() : "undefined"));

        } else if (ctx.CTE() != null) {
            String cteValue = ctx.CTE().getText();
            Token t = ctx.CTE().getSymbol();

            // VALIDAÇÃO: Tamanho da constante (Evita estouro de limite numérico)
            try {
                long cte = Long.parseLong(cteValue);
                if (cte < -32768 || cte > 32767) {
                    reportSemanticError(t,
                        "Constante numérica '" + cteValue + "' excede o limite de 2 bytes com sinal " +
                                "(-32768 a 32767). Valor encontrado: " + cte + "."
                    );
                }
            } catch (NumberFormatException e) {
                reportSemanticError(t, "Constante numérica '" + cteValue + "' não é um número.");
            }

            exprStack.push(new TypedOperand(cteValue, "integer"));

        } else if (ctx.CADEIA() != null) {
            exprStack.push(new TypedOperand(ctx.CADEIA().getText(), "string"));

        } else if (ctx.TRUE() != null) {
            exprStack.push(new TypedOperand("TRUE", "boolean"));

        } else if (ctx.FALSE() != null) {
            exprStack.push(new TypedOperand("FALSE", "boolean"));
        }
    }

    // --- 6. COMANDOS DE ENTRADA E SAÍDA (READ / WRITE) ---
    @Override
    public void exitCmdRead(ProgramParser.CmdReadContext ctx) {
        Token t = ctx.getStart();
        for (TerminalNode idNode : ctx.getTokens(ProgramParser.ID)) {
            String idName = idNode.getText();
            if (!idName.equalsIgnoreCase("READ")) {
                symbolTable.getType(idName, t.getLine(), t.getCharPositionInLine());
                tac.emit("READ " + idName);
            }
        }
    }

    @Override
    public void exitCmdWrite(ProgramParser.CmdWriteContext ctx) {
        if (!exprStack.isEmpty()) {
            TypedOperand target = exprStack.pop();
            tac.emit("WRITE " + target.getValue());
        }
    }

    @Override
    public void exitElemW(ProgramParser.ElemWContext ctx) {
        if (ctx.CADEIA() != null) {
            // Em vez de push de string pura, encapsulamos como um TypedOperand do tipo "string"
            exprStack.push(new TypedOperand(ctx.CADEIA().getText(), "string"));
        }
    }
}