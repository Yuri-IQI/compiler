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
    private final Stack<String> exprStack = new Stack<>();

    // Pilha para gerenciar os rótulos de desvios de controle de fluxo
    private final Stack<String> labelStack = new Stack<>();

    public String getGenerated3AC() {
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

            symbolTable.getType(idName, idToken.getLine(), idToken.getCharPositionInLine());

            if (!exprStack.isEmpty()) {
                String exprResult = exprStack.pop();
                tac.emit(idName + " = " + exprResult);
            }
        }
    }

    // --- 3. OPERAÇÕES MATEMÁTICAS, RELACIONAIS E LOGICAS (EXPR) ---
    @Override
    public void exitExprRel(ProgramParser.ExprRelContext ctx) {
        if (ctx.exprRelLine() != null && ctx.exprRelLine().OPREL() != null) {
            if (exprStack.size() >= 2) {
                String right = exprStack.pop();
                String left = exprStack.pop();
                String op = ctx.exprRelLine().OPREL().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
            }
        }

        if (!labelStack.isEmpty() && !exprStack.isEmpty()) {
            if (ctx.getParent() instanceof ProgramParser.CmdIfContext) {
                String condicao = exprStack.pop();
                String labelElse = labelStack.peek();
                tac.emit("ifFalse " + condicao + " goto " + labelElse);
            } else if (ctx.getParent() instanceof ProgramParser.CmdWhileContext) {
                String condicao = exprStack.pop();
                String labelEnd = labelStack.get(labelStack.size() - 2);
                tac.emit("ifFalse " + condicao + " goto " + labelEnd);
            }
        }
    }

    @Override
    public void exitExprLog(ProgramParser.ExprLogContext ctx) {
        if (ctx.exprLogLine() != null && ctx.exprLogLine().OPLOG() != null) {
            if (exprStack.size() >= 2) {
                String right = exprStack.pop();
                String left = exprStack.pop();
                String op = ctx.exprLogLine().OPLOG().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
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
                // Em estruturas recursivas à direita avaliadas no "exit", invertemos
                // para manter a semântica correta (Esquerda OPERADOR Direita)
                String right = exprStack.pop();
                String left = exprStack.pop();
                String op = ctx.OPAD().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
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
                String right = exprStack.pop();
                String left = exprStack.pop();
                String op = ctx.OPMULT().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
            }
        }
    }

    @Override
    public void exitExprNeg(ProgramParser.ExprNegContext ctx) {
        if (ctx.OPNEG() != null) {
            if (!exprStack.isEmpty()) {
                String right = exprStack.pop();
                String temp = tac.newTemp();
                tac.emit(temp + " = ~" + right);
                exprStack.push(temp);
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
            symbolTable.getType(idName, t.getLine(), t.getCharPositionInLine());
            exprStack.push(idName);
        } else if (ctx.CTE() != null) {
            exprStack.push(ctx.CTE().getText());
        } else if (ctx.CADEIA() != null) {
            exprStack.push(ctx.CADEIA().getText());
        } else if (ctx.TRUE() != null) {
            exprStack.push("TRUE");
        } else if (ctx.FALSE() != null) {
            exprStack.push("FALSE");
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
            String target = exprStack.pop();
            tac.emit("WRITE " + target);
        }
    }

    @Override
    public void exitElemW(ProgramParser.ElemWContext ctx) {
        if (ctx.CADEIA() != null) {
            exprStack.push(ctx.CADEIA().getText());
        }
    }
}