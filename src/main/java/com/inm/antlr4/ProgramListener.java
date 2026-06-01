// Generated from src/main/java/com/inm/antlr4/Program.g4 by ANTLR 4.13.2

  package com.inm.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ProgramParser}.
 */
public interface ProgramListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ProgramParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(ProgramParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(ProgramParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#decls}.
	 * @param ctx the parse tree
	 */
	void enterDecls(ProgramParser.DeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#decls}.
	 * @param ctx the parse tree
	 */
	void exitDecls(ProgramParser.DeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listDecl}.
	 * @param ctx the parse tree
	 */
	void enterListDecl(ProgramParser.ListDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listDecl}.
	 * @param ctx the parse tree
	 */
	void exitListDecl(ProgramParser.ListDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listDeclLine}.
	 * @param ctx the parse tree
	 */
	void enterListDeclLine(ProgramParser.ListDeclLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listDeclLine}.
	 * @param ctx the parse tree
	 */
	void exitListDeclLine(ProgramParser.ListDeclLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#declTip}.
	 * @param ctx the parse tree
	 */
	void enterDeclTip(ProgramParser.DeclTipContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#declTip}.
	 * @param ctx the parse tree
	 */
	void exitDeclTip(ProgramParser.DeclTipContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listId}.
	 * @param ctx the parse tree
	 */
	void enterListId(ProgramParser.ListIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listId}.
	 * @param ctx the parse tree
	 */
	void exitListId(ProgramParser.ListIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listIdLine}.
	 * @param ctx the parse tree
	 */
	void enterListIdLine(ProgramParser.ListIdLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listIdLine}.
	 * @param ctx the parse tree
	 */
	void exitListIdLine(ProgramParser.ListIdLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#tip}.
	 * @param ctx the parse tree
	 */
	void enterTip(ProgramParser.TipContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#tip}.
	 * @param ctx the parse tree
	 */
	void exitTip(ProgramParser.TipContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdComp}.
	 * @param ctx the parse tree
	 */
	void enterCmdComp(ProgramParser.CmdCompContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdComp}.
	 * @param ctx the parse tree
	 */
	void exitCmdComp(ProgramParser.CmdCompContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listCmd}.
	 * @param ctx the parse tree
	 */
	void enterListCmd(ProgramParser.ListCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listCmd}.
	 * @param ctx the parse tree
	 */
	void exitListCmd(ProgramParser.ListCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listCmdLine}.
	 * @param ctx the parse tree
	 */
	void enterListCmdLine(ProgramParser.ListCmdLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listCmdLine}.
	 * @param ctx the parse tree
	 */
	void exitListCmdLine(ProgramParser.ListCmdLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(ProgramParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(ProgramParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdIf}.
	 * @param ctx the parse tree
	 */
	void enterCmdIf(ProgramParser.CmdIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdIf}.
	 * @param ctx the parse tree
	 */
	void exitCmdIf(ProgramParser.CmdIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdIfLine}.
	 * @param ctx the parse tree
	 */
	void enterCmdIfLine(ProgramParser.CmdIfLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdIfLine}.
	 * @param ctx the parse tree
	 */
	void exitCmdIfLine(ProgramParser.CmdIfLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdWhile}.
	 * @param ctx the parse tree
	 */
	void enterCmdWhile(ProgramParser.CmdWhileContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdWhile}.
	 * @param ctx the parse tree
	 */
	void exitCmdWhile(ProgramParser.CmdWhileContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdRead}.
	 * @param ctx the parse tree
	 */
	void enterCmdRead(ProgramParser.CmdReadContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdRead}.
	 * @param ctx the parse tree
	 */
	void exitCmdRead(ProgramParser.CmdReadContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdWrite}.
	 * @param ctx the parse tree
	 */
	void enterCmdWrite(ProgramParser.CmdWriteContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdWrite}.
	 * @param ctx the parse tree
	 */
	void exitCmdWrite(ProgramParser.CmdWriteContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listW}.
	 * @param ctx the parse tree
	 */
	void enterListW(ProgramParser.ListWContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listW}.
	 * @param ctx the parse tree
	 */
	void exitListW(ProgramParser.ListWContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#listWLine}.
	 * @param ctx the parse tree
	 */
	void enterListWLine(ProgramParser.ListWLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#listWLine}.
	 * @param ctx the parse tree
	 */
	void exitListWLine(ProgramParser.ListWLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#elemW}.
	 * @param ctx the parse tree
	 */
	void enterElemW(ProgramParser.ElemWContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#elemW}.
	 * @param ctx the parse tree
	 */
	void exitElemW(ProgramParser.ElemWContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#cmdAtrib}.
	 * @param ctx the parse tree
	 */
	void enterCmdAtrib(ProgramParser.CmdAtribContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#cmdAtrib}.
	 * @param ctx the parse tree
	 */
	void exitCmdAtrib(ProgramParser.CmdAtribContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprRel}.
	 * @param ctx the parse tree
	 */
	void enterExprRel(ProgramParser.ExprRelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprRel}.
	 * @param ctx the parse tree
	 */
	void exitExprRel(ProgramParser.ExprRelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprRelLine}.
	 * @param ctx the parse tree
	 */
	void enterExprRelLine(ProgramParser.ExprRelLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprRelLine}.
	 * @param ctx the parse tree
	 */
	void exitExprRelLine(ProgramParser.ExprRelLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprLog}.
	 * @param ctx the parse tree
	 */
	void enterExprLog(ProgramParser.ExprLogContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprLog}.
	 * @param ctx the parse tree
	 */
	void exitExprLog(ProgramParser.ExprLogContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprLogLine}.
	 * @param ctx the parse tree
	 */
	void enterExprLogLine(ProgramParser.ExprLogLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprLogLine}.
	 * @param ctx the parse tree
	 */
	void exitExprLogLine(ProgramParser.ExprLogLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprAd}.
	 * @param ctx the parse tree
	 */
	void enterExprAd(ProgramParser.ExprAdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprAd}.
	 * @param ctx the parse tree
	 */
	void exitExprAd(ProgramParser.ExprAdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprAdLine}.
	 * @param ctx the parse tree
	 */
	void enterExprAdLine(ProgramParser.ExprAdLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprAdLine}.
	 * @param ctx the parse tree
	 */
	void exitExprAdLine(ProgramParser.ExprAdLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprMult}.
	 * @param ctx the parse tree
	 */
	void enterExprMult(ProgramParser.ExprMultContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprMult}.
	 * @param ctx the parse tree
	 */
	void exitExprMult(ProgramParser.ExprMultContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprMultLine}.
	 * @param ctx the parse tree
	 */
	void enterExprMultLine(ProgramParser.ExprMultLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprMultLine}.
	 * @param ctx the parse tree
	 */
	void exitExprMultLine(ProgramParser.ExprMultLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprNeg}.
	 * @param ctx the parse tree
	 */
	void enterExprNeg(ProgramParser.ExprNegContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprNeg}.
	 * @param ctx the parse tree
	 */
	void exitExprNeg(ProgramParser.ExprNegContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProgramParser#exprPar}.
	 * @param ctx the parse tree
	 */
	void enterExprPar(ProgramParser.ExprParContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProgramParser#exprPar}.
	 * @param ctx the parse tree
	 */
	void exitExprPar(ProgramParser.ExprParContext ctx);
}