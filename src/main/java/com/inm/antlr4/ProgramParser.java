// Generated from src/main/java/com/inm/antlr4/Program.g4 by ANTLR 4.13.2

  package com.inm.antlr4;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ProgramParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PROGRAM=1, INTEGER=2, BOOLEAN=3, BEGIN=4, END=5, WHILE=6, DO=7, READ=8, 
		VAR=9, FALSE=10, TRUE=11, WRITE=12, STRING=13, IF=14, THEN=15, ELSE=16, 
		CTE=17, COMMENT_RULE=18, OPAD=19, OPMULT=20, OPLOG=21, OPNEG=22, OPREL=23, 
		ATRIB=24, DPONTOS=25, PVIG=26, PONTO=27, VIG=28, ABPAR=29, FPAR=30, ID=31, 
		CADEIA=32, WS_RULE=33;
	public static final int
		RULE_prog = 0, RULE_decls = 1, RULE_listDecl = 2, RULE_listDeclLine = 3, 
		RULE_declTip = 4, RULE_listId = 5, RULE_listIdLine = 6, RULE_tip = 7, 
		RULE_cmdComp = 8, RULE_listCmd = 9, RULE_varDecl = 10, RULE_cmdDecl = 11, 
		RULE_cmd = 12, RULE_cmdIf = 13, RULE_cmdIfLine = 14, RULE_cmdWhile = 15, 
		RULE_cmdRead = 16, RULE_cmdWrite = 17, RULE_listW = 18, RULE_listWLine = 19, 
		RULE_elemW = 20, RULE_cmdAtrib = 21, RULE_exprRel = 22, RULE_exprRelLine = 23, 
		RULE_exprLog = 24, RULE_exprLogLine = 25, RULE_exprAd = 26, RULE_exprAdLine = 27, 
		RULE_exprMult = 28, RULE_exprMultLine = 29, RULE_exprNeg = 30, RULE_exprPar = 31;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "decls", "listDecl", "listDeclLine", "declTip", "listId", "listIdLine", 
			"tip", "cmdComp", "listCmd", "varDecl", "cmdDecl", "cmd", "cmdIf", "cmdIfLine", 
			"cmdWhile", "cmdRead", "cmdWrite", "listW", "listWLine", "elemW", "cmdAtrib", 
			"exprRel", "exprRelLine", "exprLog", "exprLogLine", "exprAd", "exprAdLine", 
			"exprMult", "exprMultLine", "exprNeg", "exprPar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'PROGRAM'", "'INTEGER'", "'BOOLEAN'", "'BEGIN'", "'END'", "'WHILE'", 
			"'DO'", "'READ'", "'VAR'", "'FALSE'", "'TRUE'", "'WRITE'", "'STRING'", 
			"'IF'", "'THEN'", "'ELSE'", null, null, null, null, null, "'~'", null, 
			"':='", "':'", "';'", "'.'", "','", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PROGRAM", "INTEGER", "BOOLEAN", "BEGIN", "END", "WHILE", "DO", 
			"READ", "VAR", "FALSE", "TRUE", "WRITE", "STRING", "IF", "THEN", "ELSE", 
			"CTE", "COMMENT_RULE", "OPAD", "OPMULT", "OPLOG", "OPNEG", "OPREL", "ATRIB", 
			"DPONTOS", "PVIG", "PONTO", "VIG", "ABPAR", "FPAR", "ID", "CADEIA", "WS_RULE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Program.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ProgramParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode PROGRAM() { return getToken(ProgramParser.PROGRAM, 0); }
		public TerminalNode ID() { return getToken(ProgramParser.ID, 0); }
		public TerminalNode PVIG() { return getToken(ProgramParser.PVIG, 0); }
		public DeclsContext decls() {
			return getRuleContext(DeclsContext.class,0);
		}
		public CmdCompContext cmdComp() {
			return getRuleContext(CmdCompContext.class,0);
		}
		public TerminalNode PONTO() { return getToken(ProgramParser.PONTO, 0); }
		public TerminalNode EOF() { return getToken(ProgramParser.EOF, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(PROGRAM);
			setState(65);
			match(ID);
			setState(66);
			match(PVIG);
			setState(67);
			decls();
			setState(68);
			cmdComp();
			setState(69);
			match(PONTO);
			setState(70);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclsContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(ProgramParser.VAR, 0); }
		public ListDeclContext listDecl() {
			return getRuleContext(ListDeclContext.class,0);
		}
		public DeclsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decls; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterDecls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitDecls(this);
		}
	}

	public final DeclsContext decls() throws RecognitionException {
		DeclsContext _localctx = new DeclsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_decls);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(72);
				match(VAR);
				setState(73);
				listDecl();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListDeclContext extends ParserRuleContext {
		public DeclTipContext declTip() {
			return getRuleContext(DeclTipContext.class,0);
		}
		public ListDeclLineContext listDeclLine() {
			return getRuleContext(ListDeclLineContext.class,0);
		}
		public ListDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListDecl(this);
		}
	}

	public final ListDeclContext listDecl() throws RecognitionException {
		ListDeclContext _localctx = new ListDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_listDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			declTip();
			setState(77);
			listDeclLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListDeclLineContext extends ParserRuleContext {
		public DeclTipContext declTip() {
			return getRuleContext(DeclTipContext.class,0);
		}
		public ListDeclLineContext listDeclLine() {
			return getRuleContext(ListDeclLineContext.class,0);
		}
		public ListDeclLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listDeclLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListDeclLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListDeclLine(this);
		}
	}

	public final ListDeclLineContext listDeclLine() throws RecognitionException {
		ListDeclLineContext _localctx = new ListDeclLineContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_listDeclLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(79);
				declTip();
				setState(80);
				listDeclLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclTipContext extends ParserRuleContext {
		public ListIdContext listId() {
			return getRuleContext(ListIdContext.class,0);
		}
		public TerminalNode DPONTOS() { return getToken(ProgramParser.DPONTOS, 0); }
		public TipContext tip() {
			return getRuleContext(TipContext.class,0);
		}
		public TerminalNode PVIG() { return getToken(ProgramParser.PVIG, 0); }
		public DeclTipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declTip; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterDeclTip(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitDeclTip(this);
		}
	}

	public final DeclTipContext declTip() throws RecognitionException {
		DeclTipContext _localctx = new DeclTipContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declTip);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			listId();
			setState(85);
			match(DPONTOS);
			setState(86);
			tip();
			setState(87);
			match(PVIG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListIdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ProgramParser.ID, 0); }
		public ListIdLineContext listIdLine() {
			return getRuleContext(ListIdLineContext.class,0);
		}
		public ListIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListId(this);
		}
	}

	public final ListIdContext listId() throws RecognitionException {
		ListIdContext _localctx = new ListIdContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_listId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(ID);
			setState(90);
			listIdLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListIdLineContext extends ParserRuleContext {
		public TerminalNode VIG() { return getToken(ProgramParser.VIG, 0); }
		public TerminalNode ID() { return getToken(ProgramParser.ID, 0); }
		public ListIdLineContext listIdLine() {
			return getRuleContext(ListIdLineContext.class,0);
		}
		public ListIdLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listIdLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListIdLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListIdLine(this);
		}
	}

	public final ListIdLineContext listIdLine() throws RecognitionException {
		ListIdLineContext _localctx = new ListIdLineContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_listIdLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VIG) {
				{
				setState(92);
				match(VIG);
				setState(93);
				match(ID);
				setState(94);
				listIdLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TipContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(ProgramParser.INTEGER, 0); }
		public TerminalNode BOOLEAN() { return getToken(ProgramParser.BOOLEAN, 0); }
		public TerminalNode STRING() { return getToken(ProgramParser.STRING, 0); }
		public TipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tip; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterTip(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitTip(this);
		}
	}

	public final TipContext tip() throws RecognitionException {
		TipContext _localctx = new TipContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_tip);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8204L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdCompContext extends ParserRuleContext {
		public TerminalNode BEGIN() { return getToken(ProgramParser.BEGIN, 0); }
		public ListCmdContext listCmd() {
			return getRuleContext(ListCmdContext.class,0);
		}
		public TerminalNode END() { return getToken(ProgramParser.END, 0); }
		public CmdCompContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdComp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdComp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdComp(this);
		}
	}

	public final CmdCompContext cmdComp() throws RecognitionException {
		CmdCompContext _localctx = new CmdCompContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_cmdComp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(BEGIN);
			setState(100);
			listCmd();
			setState(101);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListCmdContext extends ParserRuleContext {
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public CmdDeclContext cmdDecl() {
			return getRuleContext(CmdDeclContext.class,0);
		}
		public ListCmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listCmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListCmd(this);
		}
	}

	public final ListCmdContext listCmd() throws RecognitionException {
		ListCmdContext _localctx = new ListCmdContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_listCmd);
		try {
			setState(106);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				varDecl();
				}
				break;
			case BEGIN:
			case WHILE:
			case READ:
			case WRITE:
			case IF:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				cmdDecl();
				}
				break;
			case END:
				enterOuterAlt(_localctx, 3);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(ProgramParser.VAR, 0); }
		public DeclTipContext declTip() {
			return getRuleContext(DeclTipContext.class,0);
		}
		public ListCmdContext listCmd() {
			return getRuleContext(ListCmdContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitVarDecl(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_varDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(VAR);
			setState(109);
			declTip();
			setState(110);
			listCmd();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdDeclContext extends ParserRuleContext {
		public CmdContext cmd() {
			return getRuleContext(CmdContext.class,0);
		}
		public TerminalNode PVIG() { return getToken(ProgramParser.PVIG, 0); }
		public ListCmdContext listCmd() {
			return getRuleContext(ListCmdContext.class,0);
		}
		public CmdDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdDecl(this);
		}
	}

	public final CmdDeclContext cmdDecl() throws RecognitionException {
		CmdDeclContext _localctx = new CmdDeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_cmdDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			cmd();
			setState(113);
			match(PVIG);
			setState(114);
			listCmd();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdContext extends ParserRuleContext {
		public CmdIfContext cmdIf() {
			return getRuleContext(CmdIfContext.class,0);
		}
		public CmdWhileContext cmdWhile() {
			return getRuleContext(CmdWhileContext.class,0);
		}
		public CmdReadContext cmdRead() {
			return getRuleContext(CmdReadContext.class,0);
		}
		public CmdWriteContext cmdWrite() {
			return getRuleContext(CmdWriteContext.class,0);
		}
		public CmdAtribContext cmdAtrib() {
			return getRuleContext(CmdAtribContext.class,0);
		}
		public CmdCompContext cmdComp() {
			return getRuleContext(CmdCompContext.class,0);
		}
		public CmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmd(this);
		}
	}

	public final CmdContext cmd() throws RecognitionException {
		CmdContext _localctx = new CmdContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_cmd);
		try {
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				cmdIf();
				}
				break;
			case WHILE:
				enterOuterAlt(_localctx, 2);
				{
				setState(117);
				cmdWhile();
				}
				break;
			case READ:
				enterOuterAlt(_localctx, 3);
				{
				setState(118);
				cmdRead();
				}
				break;
			case WRITE:
				enterOuterAlt(_localctx, 4);
				{
				setState(119);
				cmdWrite();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 5);
				{
				setState(120);
				cmdAtrib();
				}
				break;
			case BEGIN:
				enterOuterAlt(_localctx, 6);
				{
				setState(121);
				cmdComp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdIfContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(ProgramParser.IF, 0); }
		public ExprRelContext exprRel() {
			return getRuleContext(ExprRelContext.class,0);
		}
		public TerminalNode THEN() { return getToken(ProgramParser.THEN, 0); }
		public CmdCompContext cmdComp() {
			return getRuleContext(CmdCompContext.class,0);
		}
		public CmdIfLineContext cmdIfLine() {
			return getRuleContext(CmdIfLineContext.class,0);
		}
		public CmdIfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdIf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdIf(this);
		}
	}

	public final CmdIfContext cmdIf() throws RecognitionException {
		CmdIfContext _localctx = new CmdIfContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_cmdIf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(IF);
			setState(125);
			exprRel();
			setState(126);
			match(THEN);
			setState(127);
			cmdComp();
			setState(128);
			cmdIfLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdIfLineContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(ProgramParser.ELSE, 0); }
		public CmdCompContext cmdComp() {
			return getRuleContext(CmdCompContext.class,0);
		}
		public CmdIfLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdIfLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdIfLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdIfLine(this);
		}
	}

	public final CmdIfLineContext cmdIfLine() throws RecognitionException {
		CmdIfLineContext _localctx = new CmdIfLineContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_cmdIfLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(130);
				match(ELSE);
				setState(131);
				cmdComp();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdWhileContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(ProgramParser.WHILE, 0); }
		public ExprRelContext exprRel() {
			return getRuleContext(ExprRelContext.class,0);
		}
		public TerminalNode DO() { return getToken(ProgramParser.DO, 0); }
		public CmdCompContext cmdComp() {
			return getRuleContext(CmdCompContext.class,0);
		}
		public CmdWhileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdWhile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdWhile(this);
		}
	}

	public final CmdWhileContext cmdWhile() throws RecognitionException {
		CmdWhileContext _localctx = new CmdWhileContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_cmdWhile);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(WHILE);
			setState(135);
			exprRel();
			setState(136);
			match(DO);
			setState(137);
			cmdComp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdReadContext extends ParserRuleContext {
		public TerminalNode READ() { return getToken(ProgramParser.READ, 0); }
		public TerminalNode ABPAR() { return getToken(ProgramParser.ABPAR, 0); }
		public ListIdContext listId() {
			return getRuleContext(ListIdContext.class,0);
		}
		public TerminalNode FPAR() { return getToken(ProgramParser.FPAR, 0); }
		public CmdReadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdRead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdRead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdRead(this);
		}
	}

	public final CmdReadContext cmdRead() throws RecognitionException {
		CmdReadContext _localctx = new CmdReadContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_cmdRead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(READ);
			setState(140);
			match(ABPAR);
			setState(141);
			listId();
			setState(142);
			match(FPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdWriteContext extends ParserRuleContext {
		public TerminalNode WRITE() { return getToken(ProgramParser.WRITE, 0); }
		public TerminalNode ABPAR() { return getToken(ProgramParser.ABPAR, 0); }
		public ListWContext listW() {
			return getRuleContext(ListWContext.class,0);
		}
		public TerminalNode FPAR() { return getToken(ProgramParser.FPAR, 0); }
		public CmdWriteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdWrite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdWrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdWrite(this);
		}
	}

	public final CmdWriteContext cmdWrite() throws RecognitionException {
		CmdWriteContext _localctx = new CmdWriteContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_cmdWrite);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(WRITE);
			setState(145);
			match(ABPAR);
			setState(146);
			listW();
			setState(147);
			match(FPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListWContext extends ParserRuleContext {
		public ElemWContext elemW() {
			return getRuleContext(ElemWContext.class,0);
		}
		public ListWLineContext listWLine() {
			return getRuleContext(ListWLineContext.class,0);
		}
		public ListWContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listW; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListW(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListW(this);
		}
	}

	public final ListWContext listW() throws RecognitionException {
		ListWContext _localctx = new ListWContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_listW);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			elemW();
			setState(150);
			listWLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListWLineContext extends ParserRuleContext {
		public TerminalNode VIG() { return getToken(ProgramParser.VIG, 0); }
		public ElemWContext elemW() {
			return getRuleContext(ElemWContext.class,0);
		}
		public ListWLineContext listWLine() {
			return getRuleContext(ListWLineContext.class,0);
		}
		public ListWLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listWLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterListWLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitListWLine(this);
		}
	}

	public final ListWLineContext listWLine() throws RecognitionException {
		ListWLineContext _localctx = new ListWLineContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_listWLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VIG) {
				{
				setState(152);
				match(VIG);
				setState(153);
				elemW();
				setState(154);
				listWLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElemWContext extends ParserRuleContext {
		public ExprRelContext exprRel() {
			return getRuleContext(ExprRelContext.class,0);
		}
		public TerminalNode CADEIA() { return getToken(ProgramParser.CADEIA, 0); }
		public ElemWContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elemW; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterElemW(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitElemW(this);
		}
	}

	public final ElemWContext elemW() throws RecognitionException {
		ElemWContext _localctx = new ElemWContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_elemW);
		try {
			setState(160);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(158);
				exprRel();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				match(CADEIA);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CmdAtribContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(ProgramParser.ID, 0); }
		public TerminalNode ATRIB() { return getToken(ProgramParser.ATRIB, 0); }
		public ExprRelContext exprRel() {
			return getRuleContext(ExprRelContext.class,0);
		}
		public CmdAtribContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdAtrib; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterCmdAtrib(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitCmdAtrib(this);
		}
	}

	public final CmdAtribContext cmdAtrib() throws RecognitionException {
		CmdAtribContext _localctx = new CmdAtribContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_cmdAtrib);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(ID);
			setState(163);
			match(ATRIB);
			setState(164);
			exprRel();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprRelContext extends ParserRuleContext {
		public ExprLogContext exprLog() {
			return getRuleContext(ExprLogContext.class,0);
		}
		public ExprRelLineContext exprRelLine() {
			return getRuleContext(ExprRelLineContext.class,0);
		}
		public ExprRelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprRel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprRel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprRel(this);
		}
	}

	public final ExprRelContext exprRel() throws RecognitionException {
		ExprRelContext _localctx = new ExprRelContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_exprRel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			exprLog();
			setState(167);
			exprRelLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprRelLineContext extends ParserRuleContext {
		public TerminalNode OPREL() { return getToken(ProgramParser.OPREL, 0); }
		public ExprLogContext exprLog() {
			return getRuleContext(ExprLogContext.class,0);
		}
		public ExprRelLineContext exprRelLine() {
			return getRuleContext(ExprRelLineContext.class,0);
		}
		public ExprRelLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprRelLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprRelLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprRelLine(this);
		}
	}

	public final ExprRelLineContext exprRelLine() throws RecognitionException {
		ExprRelLineContext _localctx = new ExprRelLineContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_exprRelLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPREL) {
				{
				setState(169);
				match(OPREL);
				setState(170);
				exprLog();
				setState(171);
				exprRelLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprLogContext extends ParserRuleContext {
		public ExprAdContext exprAd() {
			return getRuleContext(ExprAdContext.class,0);
		}
		public ExprLogLineContext exprLogLine() {
			return getRuleContext(ExprLogLineContext.class,0);
		}
		public ExprLogContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprLog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprLog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprLog(this);
		}
	}

	public final ExprLogContext exprLog() throws RecognitionException {
		ExprLogContext _localctx = new ExprLogContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_exprLog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			exprAd();
			setState(176);
			exprLogLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprLogLineContext extends ParserRuleContext {
		public TerminalNode OPLOG() { return getToken(ProgramParser.OPLOG, 0); }
		public ExprAdContext exprAd() {
			return getRuleContext(ExprAdContext.class,0);
		}
		public ExprLogLineContext exprLogLine() {
			return getRuleContext(ExprLogLineContext.class,0);
		}
		public ExprLogLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprLogLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprLogLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprLogLine(this);
		}
	}

	public final ExprLogLineContext exprLogLine() throws RecognitionException {
		ExprLogLineContext _localctx = new ExprLogLineContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_exprLogLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPLOG) {
				{
				setState(178);
				match(OPLOG);
				setState(179);
				exprAd();
				setState(180);
				exprLogLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprAdContext extends ParserRuleContext {
		public ExprMultContext exprMult() {
			return getRuleContext(ExprMultContext.class,0);
		}
		public ExprAdLineContext exprAdLine() {
			return getRuleContext(ExprAdLineContext.class,0);
		}
		public ExprAdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprAd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprAd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprAd(this);
		}
	}

	public final ExprAdContext exprAd() throws RecognitionException {
		ExprAdContext _localctx = new ExprAdContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_exprAd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			exprMult();
			setState(185);
			exprAdLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprAdLineContext extends ParserRuleContext {
		public TerminalNode OPAD() { return getToken(ProgramParser.OPAD, 0); }
		public ExprMultContext exprMult() {
			return getRuleContext(ExprMultContext.class,0);
		}
		public ExprAdLineContext exprAdLine() {
			return getRuleContext(ExprAdLineContext.class,0);
		}
		public ExprAdLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprAdLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprAdLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprAdLine(this);
		}
	}

	public final ExprAdLineContext exprAdLine() throws RecognitionException {
		ExprAdLineContext _localctx = new ExprAdLineContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_exprAdLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPAD) {
				{
				setState(187);
				match(OPAD);
				setState(188);
				exprMult();
				setState(189);
				exprAdLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprMultContext extends ParserRuleContext {
		public ExprNegContext exprNeg() {
			return getRuleContext(ExprNegContext.class,0);
		}
		public ExprMultLineContext exprMultLine() {
			return getRuleContext(ExprMultLineContext.class,0);
		}
		public ExprMultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprMult; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprMult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprMult(this);
		}
	}

	public final ExprMultContext exprMult() throws RecognitionException {
		ExprMultContext _localctx = new ExprMultContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_exprMult);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			exprNeg();
			setState(194);
			exprMultLine();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprMultLineContext extends ParserRuleContext {
		public TerminalNode OPMULT() { return getToken(ProgramParser.OPMULT, 0); }
		public ExprNegContext exprNeg() {
			return getRuleContext(ExprNegContext.class,0);
		}
		public ExprMultLineContext exprMultLine() {
			return getRuleContext(ExprMultLineContext.class,0);
		}
		public ExprMultLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprMultLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprMultLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprMultLine(this);
		}
	}

	public final ExprMultLineContext exprMultLine() throws RecognitionException {
		ExprMultLineContext _localctx = new ExprMultLineContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_exprMultLine);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPMULT) {
				{
				setState(196);
				match(OPMULT);
				setState(197);
				exprNeg();
				setState(198);
				exprMultLine();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprNegContext extends ParserRuleContext {
		public TerminalNode OPNEG() { return getToken(ProgramParser.OPNEG, 0); }
		public ExprNegContext exprNeg() {
			return getRuleContext(ExprNegContext.class,0);
		}
		public ExprParContext exprPar() {
			return getRuleContext(ExprParContext.class,0);
		}
		public ExprNegContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprNeg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprNeg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprNeg(this);
		}
	}

	public final ExprNegContext exprNeg() throws RecognitionException {
		ExprNegContext _localctx = new ExprNegContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_exprNeg);
		try {
			setState(205);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPNEG:
				enterOuterAlt(_localctx, 1);
				{
				setState(202);
				match(OPNEG);
				setState(203);
				exprNeg();
				}
				break;
			case FALSE:
			case TRUE:
			case CTE:
			case ABPAR:
			case ID:
			case CADEIA:
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
				exprPar();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprParContext extends ParserRuleContext {
		public TerminalNode ABPAR() { return getToken(ProgramParser.ABPAR, 0); }
		public ExprRelContext exprRel() {
			return getRuleContext(ExprRelContext.class,0);
		}
		public TerminalNode FPAR() { return getToken(ProgramParser.FPAR, 0); }
		public TerminalNode ID() { return getToken(ProgramParser.ID, 0); }
		public TerminalNode CTE() { return getToken(ProgramParser.CTE, 0); }
		public TerminalNode TRUE() { return getToken(ProgramParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(ProgramParser.FALSE, 0); }
		public TerminalNode CADEIA() { return getToken(ProgramParser.CADEIA, 0); }
		public ExprParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprPar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).enterExprPar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProgramListener ) ((ProgramListener)listener).exitExprPar(this);
		}
	}

	public final ExprParContext exprPar() throws RecognitionException {
		ExprParContext _localctx = new ExprParContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_exprPar);
		try {
			setState(216);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABPAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(207);
				match(ABPAR);
				setState(208);
				exprRel();
				setState(209);
				match(FPAR);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(211);
				match(ID);
				}
				break;
			case CTE:
				enterOuterAlt(_localctx, 3);
				{
				setState(212);
				match(CTE);
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 4);
				{
				setState(213);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 5);
				{
				setState(214);
				match(FALSE);
				}
				break;
			case CADEIA:
				enterOuterAlt(_localctx, 6);
				{
				setState(215);
				match(CADEIA);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001!\u00db\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0003\u0001K\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0003\u0003S\b\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006`\b\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0003"+
		"\tk\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003"+
		"\f{\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001"+
		"\u000e\u0003\u000e\u0085\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u009d\b\u0013\u0001\u0014\u0001\u0014\u0003\u0014\u00a1"+
		"\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003"+
		"\u0017\u00ae\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u00b7\b\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003"+
		"\u001b\u00c0\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u00c9\b\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0003\u001e\u00ce\b\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0003\u001f\u00d9\b\u001f\u0001\u001f\u0000\u0000 \u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.02468:<>\u0000\u0001\u0002\u0000\u0002\u0003\r\r\u00d1\u0000"+
		"@\u0001\u0000\u0000\u0000\u0002J\u0001\u0000\u0000\u0000\u0004L\u0001"+
		"\u0000\u0000\u0000\u0006R\u0001\u0000\u0000\u0000\bT\u0001\u0000\u0000"+
		"\u0000\nY\u0001\u0000\u0000\u0000\f_\u0001\u0000\u0000\u0000\u000ea\u0001"+
		"\u0000\u0000\u0000\u0010c\u0001\u0000\u0000\u0000\u0012j\u0001\u0000\u0000"+
		"\u0000\u0014l\u0001\u0000\u0000\u0000\u0016p\u0001\u0000\u0000\u0000\u0018"+
		"z\u0001\u0000\u0000\u0000\u001a|\u0001\u0000\u0000\u0000\u001c\u0084\u0001"+
		"\u0000\u0000\u0000\u001e\u0086\u0001\u0000\u0000\u0000 \u008b\u0001\u0000"+
		"\u0000\u0000\"\u0090\u0001\u0000\u0000\u0000$\u0095\u0001\u0000\u0000"+
		"\u0000&\u009c\u0001\u0000\u0000\u0000(\u00a0\u0001\u0000\u0000\u0000*"+
		"\u00a2\u0001\u0000\u0000\u0000,\u00a6\u0001\u0000\u0000\u0000.\u00ad\u0001"+
		"\u0000\u0000\u00000\u00af\u0001\u0000\u0000\u00002\u00b6\u0001\u0000\u0000"+
		"\u00004\u00b8\u0001\u0000\u0000\u00006\u00bf\u0001\u0000\u0000\u00008"+
		"\u00c1\u0001\u0000\u0000\u0000:\u00c8\u0001\u0000\u0000\u0000<\u00cd\u0001"+
		"\u0000\u0000\u0000>\u00d8\u0001\u0000\u0000\u0000@A\u0005\u0001\u0000"+
		"\u0000AB\u0005\u001f\u0000\u0000BC\u0005\u001a\u0000\u0000CD\u0003\u0002"+
		"\u0001\u0000DE\u0003\u0010\b\u0000EF\u0005\u001b\u0000\u0000FG\u0005\u0000"+
		"\u0000\u0001G\u0001\u0001\u0000\u0000\u0000HI\u0005\t\u0000\u0000IK\u0003"+
		"\u0004\u0002\u0000JH\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000"+
		"K\u0003\u0001\u0000\u0000\u0000LM\u0003\b\u0004\u0000MN\u0003\u0006\u0003"+
		"\u0000N\u0005\u0001\u0000\u0000\u0000OP\u0003\b\u0004\u0000PQ\u0003\u0006"+
		"\u0003\u0000QS\u0001\u0000\u0000\u0000RO\u0001\u0000\u0000\u0000RS\u0001"+
		"\u0000\u0000\u0000S\u0007\u0001\u0000\u0000\u0000TU\u0003\n\u0005\u0000"+
		"UV\u0005\u0019\u0000\u0000VW\u0003\u000e\u0007\u0000WX\u0005\u001a\u0000"+
		"\u0000X\t\u0001\u0000\u0000\u0000YZ\u0005\u001f\u0000\u0000Z[\u0003\f"+
		"\u0006\u0000[\u000b\u0001\u0000\u0000\u0000\\]\u0005\u001c\u0000\u0000"+
		"]^\u0005\u001f\u0000\u0000^`\u0003\f\u0006\u0000_\\\u0001\u0000\u0000"+
		"\u0000_`\u0001\u0000\u0000\u0000`\r\u0001\u0000\u0000\u0000ab\u0007\u0000"+
		"\u0000\u0000b\u000f\u0001\u0000\u0000\u0000cd\u0005\u0004\u0000\u0000"+
		"de\u0003\u0012\t\u0000ef\u0005\u0005\u0000\u0000f\u0011\u0001\u0000\u0000"+
		"\u0000gk\u0003\u0014\n\u0000hk\u0003\u0016\u000b\u0000ik\u0001\u0000\u0000"+
		"\u0000jg\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000ji\u0001\u0000"+
		"\u0000\u0000k\u0013\u0001\u0000\u0000\u0000lm\u0005\t\u0000\u0000mn\u0003"+
		"\b\u0004\u0000no\u0003\u0012\t\u0000o\u0015\u0001\u0000\u0000\u0000pq"+
		"\u0003\u0018\f\u0000qr\u0005\u001a\u0000\u0000rs\u0003\u0012\t\u0000s"+
		"\u0017\u0001\u0000\u0000\u0000t{\u0003\u001a\r\u0000u{\u0003\u001e\u000f"+
		"\u0000v{\u0003 \u0010\u0000w{\u0003\"\u0011\u0000x{\u0003*\u0015\u0000"+
		"y{\u0003\u0010\b\u0000zt\u0001\u0000\u0000\u0000zu\u0001\u0000\u0000\u0000"+
		"zv\u0001\u0000\u0000\u0000zw\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000"+
		"\u0000zy\u0001\u0000\u0000\u0000{\u0019\u0001\u0000\u0000\u0000|}\u0005"+
		"\u000e\u0000\u0000}~\u0003,\u0016\u0000~\u007f\u0005\u000f\u0000\u0000"+
		"\u007f\u0080\u0003\u0010\b\u0000\u0080\u0081\u0003\u001c\u000e\u0000\u0081"+
		"\u001b\u0001\u0000\u0000\u0000\u0082\u0083\u0005\u0010\u0000\u0000\u0083"+
		"\u0085\u0003\u0010\b\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0001\u0000\u0000\u0000\u0085\u001d\u0001\u0000\u0000\u0000\u0086\u0087"+
		"\u0005\u0006\u0000\u0000\u0087\u0088\u0003,\u0016\u0000\u0088\u0089\u0005"+
		"\u0007\u0000\u0000\u0089\u008a\u0003\u0010\b\u0000\u008a\u001f\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0005\b\u0000\u0000\u008c\u008d\u0005\u001d\u0000"+
		"\u0000\u008d\u008e\u0003\n\u0005\u0000\u008e\u008f\u0005\u001e\u0000\u0000"+
		"\u008f!\u0001\u0000\u0000\u0000\u0090\u0091\u0005\f\u0000\u0000\u0091"+
		"\u0092\u0005\u001d\u0000\u0000\u0092\u0093\u0003$\u0012\u0000\u0093\u0094"+
		"\u0005\u001e\u0000\u0000\u0094#\u0001\u0000\u0000\u0000\u0095\u0096\u0003"+
		"(\u0014\u0000\u0096\u0097\u0003&\u0013\u0000\u0097%\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0005\u001c\u0000\u0000\u0099\u009a\u0003(\u0014\u0000"+
		"\u009a\u009b\u0003&\u0013\u0000\u009b\u009d\u0001\u0000\u0000\u0000\u009c"+
		"\u0098\u0001\u0000\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d"+
		"\'\u0001\u0000\u0000\u0000\u009e\u00a1\u0003,\u0016\u0000\u009f\u00a1"+
		"\u0005 \u0000\u0000\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0\u009f\u0001"+
		"\u0000\u0000\u0000\u00a1)\u0001\u0000\u0000\u0000\u00a2\u00a3\u0005\u001f"+
		"\u0000\u0000\u00a3\u00a4\u0005\u0018\u0000\u0000\u00a4\u00a5\u0003,\u0016"+
		"\u0000\u00a5+\u0001\u0000\u0000\u0000\u00a6\u00a7\u00030\u0018\u0000\u00a7"+
		"\u00a8\u0003.\u0017\u0000\u00a8-\u0001\u0000\u0000\u0000\u00a9\u00aa\u0005"+
		"\u0017\u0000\u0000\u00aa\u00ab\u00030\u0018\u0000\u00ab\u00ac\u0003.\u0017"+
		"\u0000\u00ac\u00ae\u0001\u0000\u0000\u0000\u00ad\u00a9\u0001\u0000\u0000"+
		"\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae/\u0001\u0000\u0000\u0000"+
		"\u00af\u00b0\u00034\u001a\u0000\u00b0\u00b1\u00032\u0019\u0000\u00b11"+
		"\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005\u0015\u0000\u0000\u00b3\u00b4"+
		"\u00034\u001a\u0000\u00b4\u00b5\u00032\u0019\u0000\u00b5\u00b7\u0001\u0000"+
		"\u0000\u0000\u00b6\u00b2\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000"+
		"\u0000\u0000\u00b73\u0001\u0000\u0000\u0000\u00b8\u00b9\u00038\u001c\u0000"+
		"\u00b9\u00ba\u00036\u001b\u0000\u00ba5\u0001\u0000\u0000\u0000\u00bb\u00bc"+
		"\u0005\u0013\u0000\u0000\u00bc\u00bd\u00038\u001c\u0000\u00bd\u00be\u0003"+
		"6\u001b\u0000\u00be\u00c0\u0001\u0000\u0000\u0000\u00bf\u00bb\u0001\u0000"+
		"\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c07\u0001\u0000\u0000"+
		"\u0000\u00c1\u00c2\u0003<\u001e\u0000\u00c2\u00c3\u0003:\u001d\u0000\u00c3"+
		"9\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\u0014\u0000\u0000\u00c5\u00c6"+
		"\u0003<\u001e\u0000\u00c6\u00c7\u0003:\u001d\u0000\u00c7\u00c9\u0001\u0000"+
		"\u0000\u0000\u00c8\u00c4\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000"+
		"\u0000\u0000\u00c9;\u0001\u0000\u0000\u0000\u00ca\u00cb\u0005\u0016\u0000"+
		"\u0000\u00cb\u00ce\u0003<\u001e\u0000\u00cc\u00ce\u0003>\u001f\u0000\u00cd"+
		"\u00ca\u0001\u0000\u0000\u0000\u00cd\u00cc\u0001\u0000\u0000\u0000\u00ce"+
		"=\u0001\u0000\u0000\u0000\u00cf\u00d0\u0005\u001d\u0000\u0000\u00d0\u00d1"+
		"\u0003,\u0016\u0000\u00d1\u00d2\u0005\u001e\u0000\u0000\u00d2\u00d9\u0001"+
		"\u0000\u0000\u0000\u00d3\u00d9\u0005\u001f\u0000\u0000\u00d4\u00d9\u0005"+
		"\u0011\u0000\u0000\u00d5\u00d9\u0005\u000b\u0000\u0000\u00d6\u00d9\u0005"+
		"\n\u0000\u0000\u00d7\u00d9\u0005 \u0000\u0000\u00d8\u00cf\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d3\u0001\u0000\u0000\u0000\u00d8\u00d4\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d5\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d7\u0001\u0000\u0000\u0000\u00d9?\u0001\u0000\u0000"+
		"\u0000\u000eJR_jz\u0084\u009c\u00a0\u00ad\u00b6\u00bf\u00c8\u00cd\u00d8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}