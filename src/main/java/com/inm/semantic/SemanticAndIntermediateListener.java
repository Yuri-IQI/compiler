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

        if (ctx.getParent() instanceof ProgramParser.CmdIfContext && !labelStack.isEmpty()) {
            if (!exprStack.isEmpty()) {
                String condicao = exprStack.pop();
                String labelElse = labelStack.peek(); // Apenas espia o rótulo do Else
                tac.emit("ifFalse " + condicao + " goto " + labelElse);
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
        if (ctx.exprAdLine() != null && ctx.exprAdLine().OPAD() != null) {
            if (exprStack.size() >= 2) {
                String left = exprStack.pop();
                String right = exprStack.pop();
                String op = ctx.exprAdLine().OPAD().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
            }
        }
    }

    @Override
    public void exitExprMult(ProgramParser.ExprMultContext ctx) {
        if (ctx.exprMultLine() != null && ctx.exprMultLine().OPMULT() != null) {
            if (exprStack.size() >= 2) {
                String left = exprStack.pop();
                String right = exprStack.pop();
                String op = ctx.exprMultLine().OPMULT().getText();

                String temp = tac.newTemp();
                tac.emit(temp + " = " + left + " " + op + " " + right);
                exprStack.push(temp);
            }
        }
    }

    @Override
    public void enterCmdIf(ProgramParser.CmdIfContext ctx) {
        // Ao entrar no IF, criamos os rótulos (Labels) que usaremos para os desvios
        String labelElse = tac.newLabel(); // Rótulo para o bloco Else (ou fim do IF se não houver else)
        String labelEnd = tac.newLabel();  // Rótulo para o fim definitivo do IF

        // Guarda na pilha para sabermos como desviar quando sairmos dos nós filhos
        labelStack.push(labelEnd);
        labelStack.push(labelElse);
    }

    @Override
    public void exitCmdIf(ProgramParser.CmdIfContext ctx) {
        if (!labelStack.isEmpty()) {
            // Se o IF não possuía bloco ELSE físico, o enterCmdIfLine não rodou.
            // Precisamos garantir que o labelElse seja impresso caso ele não tenha sido impresso antes.
            String labelElse = labelStack.pop();
            String labelEnd = labelStack.pop();

            // Se o código intermediário não contém o labelElse impresso, coloca ele antes do fim
            if (!tac.getCode().contains(labelElse + ":")) {
                tac.emit(labelElse + ":");
            }

            tac.emit(labelEnd + ":");
        }
    }

    @Override
    public void enterCmdIfLine(ProgramParser.CmdIfLineContext ctx) {
        // Se a pilha tiver labels, significa que estamos em um IF válido
        if (labelStack.size() >= 2 && ctx.ELSE() != null) {
            // O topo da pilha (neste momento) é o labelElse, e abaixo dele está o labelEnd
            String labelElse = labelStack.peek(); // Apenas espia sem desempilhar
            String labelEnd = labelStack.get(labelStack.size() - 2);

            // O bloco THEN acabou de rodar. Antes de entrar no ELSE, precisamos saltar para o FIM do IF
            tac.emit("goto " + labelEnd);

            // E aqui começa o bloco ELSE oficialmente, então colocamos o rótulo do Else
            tac.emit(labelElse + ":");
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

    // --- 4. VALORES FOLHA / TERMINAIS (BASE DA ARVORE) ---
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
        // Nota: Se a regra cair em ABPAR expr Rel FPAR, o valor de exprRel já terá sido processado e empilhado
    }

    // --- 5. COMANDOS DE ENTRADA E SAÍDA (READ / WRITE) ---
    @Override
    public void exitCmdRead(ProgramParser.CmdReadContext ctx) {
        Token t = ctx.getStart();
        // Coleta todos os IDs disparados dentro do escopo do READ
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
        // No padrão Listener, as expressões ou cadeias de texto internas já foram empilhadas
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