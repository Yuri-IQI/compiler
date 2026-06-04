grammar Program;

@header {
  package com.inm.antlr4;
}

options {
  caseInsensitive = true;
}

// Produções da Gramática
prog : PROGRAM ID PVIG decls cmdComp PONTO EOF ;

decls : (VAR listDecl)? ;
listDecl : declTip listDeclLine ;
listDeclLine : (declTip listDeclLine)? ;
declTip : listId DPONTOS tip PVIG ;
listId : ID listIdLine ;
listIdLine : (VIG ID listIdLine)? ;
tip : INTEGER | BOOLEAN | STRING ;

cmdComp : BEGIN listCmd END ;
listCmd : varDecl | cmdDecl ;
varDecl : (VAR declTip listCmd)? ;
cmdDecl : (cmd PVIG listCmd)? ;
cmd : cmdIf | cmdWhile | cmdRead | cmdWrite | cmdAtrib | cmdComp ;

cmdIf : IF exprRel THEN cmdComp cmdIfLine ;
cmdIfLine : (ELSE cmdComp)? ;

cmdWhile : WHILE exprRel DO cmdComp ;

cmdRead : READ ABPAR listId FPAR ;
cmdWrite : WRITE ABPAR listW FPAR ;
listW : elemW listWLine ;
listWLine : (VIG elemW listWLine)? ;
elemW : exprRel | CADEIA ;
cmdAtrib : ID ATRIB exprRel ;

exprRel : exprLog exprRelLine ;
exprRelLine : (OPREL exprLog exprRelLine)? ;
exprLog : exprAd exprLogLine ;
exprLogLine : (OPLOG exprAd exprLogLine)? ;
exprAd : exprMult exprAdLine ;
exprAdLine : (OPAD exprMult exprAdLine)? ;
exprMult : exprNeg exprMultLine ;
exprMultLine : (OPMULT exprNeg exprMultLine)? ;
exprNeg : OPNEG exprNeg | exprPar ;
exprPar : ABPAR exprRel FPAR | ID | CTE | TRUE | FALSE | CADEIA ;

// --- Tokens ---------------------------------------------

// Palavras Reservadas
PROGRAM : 'PROGRAM' ;
INTEGER : 'INTEGER' ;
BOOLEAN : 'BOOLEAN' ;
BEGIN : 'BEGIN' ;
END : 'END' ;
WHILE : 'WHILE' ;
DO : 'DO' ;
READ : 'READ' ;
VAR : 'VAR' ;
FALSE : 'FALSE' ;
TRUE : 'TRUE' ;
WRITE : 'WRITE' ;
STRING : 'STRING' ;
IF : 'IF' ;
THEN : 'THEN' ;
ELSE : 'ELSE' ;

CTE : ('+' | '-')? [0-9]+  ;

// O comentário vai dar conflito com a divisão enquante ele precisar de uma /
COMMENT_RULE : '/' [ \t] ~[\r\n]* '/' -> skip ;

// Operadores Aritméticos e Lógicos
OPAD : '+' | '-'  ;
OPMULT : '*' | '/' ;
OPLOG : 'OR' | 'AND' ;
OPNEG : '~'  ;

// Operadores Relacionais
OPREL : '<>' | '<=' | '>='
    | '==' | '<' | '>' ;

// Simbolos de Marcação
ATRIB : ':=' ;
DPONTOS : ':' ;
PVIG : ';' ;
PONTO : '.' ;
VIG : ',' ;
ABPAR : '(' ;
FPAR : ')' ;

ID : [a-z][a-z0-9]* {if (getText().length() > 16) { setText(getText().substring(0, 16)); }} ;

CADEIA : '"' (~["\r\n])* '"' ;

WS_RULE : [ \t\r\n]+ -> skip ;