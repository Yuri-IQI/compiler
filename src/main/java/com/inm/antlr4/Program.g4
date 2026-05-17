grammar Program;

// TODO: Adicionar tratamento de exceções

@header {
    package com.inm.antlr4;
}

options {
    caseInsensitive = true;
}

start : prog EOF ;

// Produções da Gramática
prog : PROGRAM ID PVIG decls cmdComp PONTO ;
decls : (VAR listDecls)? ;
listDecls : declTip | declTip listDecls ;
declTip : listId DPONTOS tip PVIG ; // A gramática está exigindo que a última linha antes de END. não termine com ;
listId : ID | ID VIG listId ;
tip : INTEGER | BOOLEAN | STRING ;

cmdComp : BEGIN listCmd END ;
listCmd : cmd | cmd PVIG listCmd ;
cmd : cmdIf | cmdWhile | cmdRead | cmdWrite | cmdAtrib | cmdComp ;

cmdIf : IF exprRel THEN cmd
    | IF exprRel THEN cmd ELSE cmd ;

cmdWhile : WHILE exprRel DO cmd ;

cmdRead : READ ABPAR listId FPAR ; // Na gramática esses parenteses estão como literais
cmdWrite : WRITE ABPAR listW FPAR ;
listW : elemW | elemW VIG listW ;
elemW : exprRel | CADEIA ;
cmdAtrib : ID ATRIB exprRel ; // Na gramática essa atribuição está como literal

/* exprOp : expr OPREL expr | expr OPAD expr | expr OPMULT expr | expr ;
expr : ID | CTE | ABPAR exprOp FPAR | TRUE | FALSE | OPNEG exprOp ; */

// expr separado para cada tipo de operação por conta de erro de reatribuição e outras coisas
exprRel : exprLog OPREL exprRel | exprLog ;
exprLog : exprAd OPLOG exprLog | exprAd ; // OPLOGs não são aplicados em lugar nenhum, mas eu vou aplica-lo a expr como outros OPs
exprAd : exprMult OPAD exprAd | exprMult ;
exprMult : exprNeg OPMULT exprMult | exprNeg ;
exprNeg : OPNEG exprNeg | exprPar ;
exprPar : ABPAR exprRel FPAR | ID | CTE | TRUE | FALSE ;

// Tokens

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

CTE : [+-]?[0-9]+ ;

// Operadores Aritméticos e Lógicos
OPAD : '+' | '-' ;
OPMULT : '*' | '/' ;
OPLOG : 'OR' | 'AND' ;
OPNEG : '~' ;

// Operadores Relacionais
OPREL : '<>' | '<=' | '<' | '>=' | '>' | '==' ;

// Simbolos de Marcação
PVIG : ';' ;
PONTO : '.' ;
ATRIB : ':=' ;
DPONTOS : ':' ;
VIG : ',' ;
ABPAR : '(' ;
FPAR : ')' ;

ID : [a-z][a-z0-9]*
    { if ( getText().length() > 16 ) { setText(getText().substring(0, 16)); } } ;

CADEIA : '"' (~["\r\n])* '"' ;

WS_RULE : [ \t\r\n]+ -> skip ;
COMMENT_RULE : '/' .*? '/' -> skip ;