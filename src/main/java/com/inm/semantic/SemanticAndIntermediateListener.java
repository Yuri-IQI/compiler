package com.inm.semantic;

import com.inm.compilation.CompilationContext;
import com.inm.antlr4.ProgramBaseListener;
import com.inm.antlr4.ProgramParser;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAndIntermediateListener extends ProgramBaseListener {

    private final ThreeAddressCode tac;
    private SymbolTable currentScope;

    private final Stack<TypedOperand> exprStack = new Stack<>();
    private final Stack<String> labelStack = new Stack<>();
    private int errorCount = 0;

    public SemanticAndIntermediateListener(CompilationContext context) {
        this.tac = context.threeAddressCode();
        this.currentScope = new SymbolTable();
    }

    public int getErrorCount() { return errorCount; }
    public SymbolTable getSymbolTable() { return currentScope; }
    public ThreeAddressCode getThreeAddressCode() { return tac; }

    private void reportSemanticError(Token token, String message) {
        errorCount++;
        System.err.println("Erro Semântico [" + token.getLine() + ":" + token.getCharPositionInLine() + "]: " + message);
    }

    private String truncateIdentifier(String name, Token token) {
        if (name.length() > 16) {
            String truncated = name.substring(0, 16);
            System.out.println("Aviso [" + token.getLine() + ":"
                    + token.getCharPositionInLine() + "]: Identificador '"
                    + name + "' truncado para '" + truncated + "'.");
            return truncated;
        }
        return name;
    }

    private void pushErrorOperand() {
        exprStack.push(new TypedOperand("0", "undefined"));
    }

    private void emit(String instruction) {
        tac.emit(instruction);
    }
    private String newLabel() { return tac.newLabel(); }

    @Override
    public void enterCmdComp(ProgramParser.CmdCompContext ctx) {
        currentScope = new SymbolTable(currentScope);
    }

    @Override
    public void exitCmdComp(ProgramParser.CmdCompContext ctx) {
        currentScope = currentScope.getParent();
    }

    @Override
    public void exitDeclTip(ProgramParser.DeclTipContext ctx) {
        if (ctx.listId() == null || ctx.tip() == null) return;

        List<String> idList = new ArrayList<>();
        Token firstToken = ctx.listId().ID().getSymbol();
        String firstName = truncateIdentifier(ctx.listId().ID().getText(), firstToken);
        idList.add(firstName);
        findIdsInLine(ctx.listId().listIdLine(), idList);

        String type = ctx.tip().getText();
        Token startToken = ctx.getStart();
        for (String id : idList) {
            try {
                currentScope.declare(id, type, startToken.getLine(), startToken.getCharPositionInLine());
            } catch (RuntimeException e) {
                reportSemanticError(startToken, e.getMessage());
            }
        }
    }

    private void findIdsInLine(ProgramParser.ListIdLineContext lineCtx, List<String> idList) {
        if (lineCtx == null) return;
        if (lineCtx.ID() != null) {
            Token t = lineCtx.ID().getSymbol();
            String name = truncateIdentifier(lineCtx.ID().getText(), t);
            if (!idList.contains(name))
                idList.add(name);
        }
        findIdsInLine(lineCtx.listIdLine(), idList);
    }

    @Override
    public void exitCmdAtrib(ProgramParser.CmdAtribContext ctx) {
        if (ctx.ID() == null || exprStack.isEmpty()) return;

        String idName = ctx.ID().getText();
        Token idToken = ctx.ID().getSymbol();
        String idType = resolveType(idName, idToken);

        TypedOperand expr = exprStack.pop();
        if (idType == null) return;

        if (!idType.equalsIgnoreCase(expr.getType())) {
            reportSemanticError(idToken,
                    "Incompatibilidade de tipos: não é possível atribuir '" + expr.getType()
                            + "' à variável '" + idName + "' do tipo '" + idType.toUpperCase() + "'.");
        }

        emit(SymbolTable.getPrefixedName(idName) + " = " + expr.getValue());
    }

    @Override
    public void exitExprRel(ProgramParser.ExprRelContext ctx) {
        if (ctx.exprRelLine() != null && ctx.exprRelLine().OPREL() != null
                && exprStack.size() >= 2) {
            TypedOperand right = exprStack.pop();
            TypedOperand left = exprStack.pop();
            String op = ctx.exprRelLine().OPREL().getText();
            Token t = ctx.getStart();

            if (!left.getType().equals(right.getType())) {
                reportSemanticError(t,
                        "Operador relacional '" + op + "' não pode comparar '"
                                + left.getType() + "' com '" + right.getType() + "'.");
                exprStack.push(new TypedOperand("0", "undefined"));
            } else {
                String temp = tac.newTemp("BOOLEAN");
                emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
                exprStack.push(new TypedOperand(temp, "boolean"));
            }
        }

        boolean isIfCondition = ctx.getParent() instanceof ProgramParser.CmdIfContext;
        boolean isWhileCondition = ctx.getParent() instanceof ProgramParser.CmdWhileContext;

        if (!labelStack.isEmpty() && !exprStack.isEmpty()
                && (isIfCondition || isWhileCondition)) {

            TypedOperand cond = exprStack.peek();
            Token t = ctx.getStart();

            if (!cond.getType().equals("boolean")) {
                reportSemanticError(t,
                        "Expressão condicional deve resultar em 'boolean', mas resultou em '"
                                + cond.getType() + "'.");
            }

            exprStack.pop();

            if (isIfCondition) {
                emit("ifFalse " + cond.getValue() + " goto " + labelStack.peek());
            } else {
                emit("ifFalse " + cond.getValue() + " goto " + labelStack.get(labelStack.size() - 2));
            }
        }
    }

    @Override
    public void exitExprLogLine(ProgramParser.ExprLogLineContext ctx) {
        if (ctx.OPLOG() == null || exprStack.size() < 2) return;

        TypedOperand right = exprStack.pop();
        TypedOperand left = exprStack.pop();
        String op = ctx.OPLOG().getText();
        Token t = ctx.getStart();

        if (!left.getType().equals("boolean") || !right.getType().equals("boolean")) {
            reportSemanticError(t,
                    "Operação lógica '" + op + "' inválida entre '"
                            + left.getType() + "' e '" + right.getType() + "'. Esperado 'boolean'.");
            pushErrorOperand();
            return;
        }

        String temp = tac.newTemp("BOOLEAN");
        emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
        exprStack.push(new TypedOperand(temp, "boolean"));
    }

    private void writeOp(Token t, TypedOperand left, TypedOperand right, String op) {
        if (op.equals("+") && left.getType().equalsIgnoreCase("string") && right.getType().equalsIgnoreCase("string")) {
            String temp = tac.newTemp("STRING");
            emit(temp + " = " + left.getValue() + " CONCAT " + right.getValue());
            exprStack.push(new TypedOperand(temp, "string"));
            return;
        }

        if (!left.getType().equals("integer") || !right.getType().equals("integer")) {
            reportSemanticError(t, "Operação '" + op + "' inválida... Esperado 'integer'.");
            pushErrorOperand();
            return;
        }

        String temp = tac.newTemp("INTEGER");
        emit(temp + " = " + left.getValue() + " " + op + " " + right.getValue());
        exprStack.push(new TypedOperand(temp, "integer"));
    }

    @Override
    public void exitExprAdLine(ProgramParser.ExprAdLineContext ctx) {
        if (ctx.OPAD() == null || exprStack.size() < 2) return;
        TypedOperand right = exprStack.pop();
        TypedOperand left = exprStack.pop();
        writeOp(ctx.getStart(), left, right, ctx.OPAD().getText());
    }

    @Override
    public void exitExprMultLine(ProgramParser.ExprMultLineContext ctx) {
        if (ctx.OPMULT() == null || exprStack.size() < 2) return;

        TypedOperand right = exprStack.pop();
        TypedOperand left = exprStack.pop();
        writeOp(ctx.getStart(), left, right, ctx.OPMULT().getText());
    }

    @Override
    public void exitExprNeg(ProgramParser.ExprNegContext ctx) {
        if (ctx.OPNEG() == null || exprStack.isEmpty()) return;

        TypedOperand right = exprStack.pop();
        Token t = ctx.getStart();

        if (!right.getType().equals("boolean")) {
            reportSemanticError(t,
                    "Operador '~' inválido para o tipo '"
                            + right.getType() + "'. Esperado 'boolean'.");
            pushErrorOperand();
            return;
        }

        String temp = tac.newTemp("BOOLEAN");
        emit(temp + " = ~" + right.getValue());
        exprStack.push(new TypedOperand(temp, "boolean"));
    }

    @Override
    public void enterCmdIf(ProgramParser.CmdIfContext ctx) {
        String labelElse = newLabel();
        String labelEnd = newLabel();
        labelStack.push(labelEnd);
        labelStack.push(labelElse);
    }

    @Override
    public void exitCmdIf(ProgramParser.CmdIfContext ctx) {
        if (labelStack.size() < 2) return;
        String labelElse = labelStack.pop();
        String labelEnd = labelStack.pop();

        if (!tac.hasLabel(labelElse)) emit(labelElse + ":");
        emit(labelEnd + ":");
    }

    @Override
    public void enterCmdIfLine(ProgramParser.CmdIfLineContext ctx) {
        if (ctx.ELSE() == null || labelStack.size() < 2) return;
        String labelElse = labelStack.peek();
        String labelEnd = labelStack.get(labelStack.size() - 2);
        emit("goto " + labelEnd);
        emit(labelElse + ":");
    }

    @Override
    public void enterCmdWhile(ProgramParser.CmdWhileContext ctx) {
        String labelStart = newLabel();
        String labelEnd = newLabel();
        emit(labelStart + ":");
        labelStack.push(labelEnd);
        labelStack.push(labelStart);
    }

    @Override
    public void exitCmdWhile(ProgramParser.CmdWhileContext ctx) {
        if (labelStack.size() < 2) return;
        String labelStart = labelStack.pop();
        String labelEnd = labelStack.pop();
        emit("goto " + labelStart);
        emit(labelEnd + ":");
    }

    @Override
    public void exitExprPar(ProgramParser.ExprParContext ctx) {
        if (ctx.ID() != null) {
            String idName = ctx.ID().getText();
            Token t = ctx.ID().getSymbol();
            String idType = resolveType(idName, t);
            String prefixedName = SymbolTable.getPrefixedName(idName);
            exprStack.push(new TypedOperand(prefixedName,
                    idType != null ? idType.toLowerCase() : "undefined"));

        } else if (ctx.CTE() != null) {
            String val = ctx.CTE().getText();
            Token t = ctx.CTE().getSymbol();
            try {
                long v = Long.parseLong(val);
                if (v < -32768 || v > 32767) {
                    reportSemanticError(t, "Constante '" + val + "' excede o limite de 2 bytes (-32768 a 32767).");
                    exprStack.push(new TypedOperand("0", "integer"));
                    return;
                }
            } catch (NumberFormatException e) {
                reportSemanticError(t, "Constante '" + val + "' não é um inteiro válido.");
                exprStack.push(new TypedOperand("0", "integer"));
                return;
            }
            exprStack.push(new TypedOperand(val, "integer"));

        } else if (ctx.CADEIA() != null) {
            exprStack.push(new TypedOperand(ctx.CADEIA().getText(), "string"));

        } else if (ctx.TRUE() != null) {
            exprStack.push(new TypedOperand("TRUE", "boolean"));

        } else if (ctx.FALSE() != null) {
            exprStack.push(new TypedOperand("FALSE", "boolean"));
        }
    }

    @Override
    public void exitCmdRead(ProgramParser.CmdReadContext ctx) {
        if (ctx.listId() == null) return;

        List<String> ids = new ArrayList<>();
        Token firstToken = ctx.listId().ID().getSymbol();
        ids.add(truncateIdentifier(ctx.listId().ID().getText(), firstToken));
        findIdsInLine(ctx.listId().listIdLine(), ids);

        Token t = ctx.getStart();
        for (String idName : ids) {
            String type = resolveType(idName, t);
            if (type == null) continue;
            emit("READ_" + type.toUpperCase() + " " + SymbolTable.getPrefixedName(idName));
        }
    }

    @Override
    public void exitElemW(ProgramParser.ElemWContext ctx) {
        if (ctx.CADEIA() != null) {
            emit("WRITE " + ctx.CADEIA().getText());
            if (!exprStack.isEmpty() && exprStack.peek().getValue().equals(ctx.CADEIA().getText())) {
                exprStack.pop();
            }
        } else {
            if (!exprStack.isEmpty()) {
                TypedOperand target = exprStack.pop();
                emit("WRITE " + target.getValue());
            }
        }
    }

    private String resolveType(String name, Token token) {
        try {
            return currentScope.getType(name, token.getLine(), token.getCharPositionInLine());
        } catch (RuntimeException e) {
            reportSemanticError(token, e.getMessage());
            return null;
        }
    }
}