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

// A produção ListDecl apresentava recursão a esquerda na gramática original,
// então ela foi fatorada com a criação de ListDecl' (listDeclLine).
listDecl : declTip listDeclLine ;
listDeclLine : (declTip listDeclLine)? ;

declTip : listId DPONTOS tip PVIG ;
listId : ID listIdLine ;
listIdLine : (VIG ID listIdLine)? ;
tip : INTEGER | BOOLEAN | STRING ;

cmdComp : BEGIN listCmd END ;
listCmd : cmd listCmdLine ;
listCmdLine : (PVIG cmd listCmdLine)? ;
cmd : cmdIf | cmdWhile | cmdRead | cmdWrite | cmdAtrib | cmdComp ;

// Para resolver o dangling else,
// o cmdIf usará o bloco BEGIN END de cmdComp para demarcar o que ele precisa executar
cmdIf : IF exprRel THEN cmdComp cmdIfLine ;
cmdIfLine : (ELSE cmdComp)? ;

cmdWhile : WHILE exprRel DO cmdComp ;

cmdRead : READ ABPAR listId FPAR ; // Na gramática esses parenteses estão como literais
cmdWrite : WRITE ABPAR listW FPAR ;
listW : elemW listWLine ;
listWLine : (VIG elemW listWLine)? ;
elemW : exprRel | CADEIA ;
cmdAtrib : ID ATRIB exprRel ; // Na gramática essa atribuição está como literal

// expr separado para cada tipo de operação para resolver erros
exprRel : exprLog exprRelLine ;
exprRelLine : (OPREL exprLog exprRelLine)? ;
exprLog : exprAd exprLogLine ; // OPLOGs não são aplicados em lugar nenhum, mas eu vou aplica-lo a expr como outros OPs
exprLogLine : (OPLOG exprAd exprLogLine)? ;
exprAd : exprMult exprAdLine ;
exprAdLine : (OPAD exprMult exprAdLine)? ;
exprMult : exprNeg exprMultLine ;
exprMultLine : (OPMULT exprNeg exprMultLine)? ;
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

CTE : [0-9]+  ;

// O comentário vai dar conflito com a divisão enquante ele precisar de uma /
COMMENT_RULE : '/' ~['\r\n]+ '/' -> skip ;

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

ERR : .
    {
        System.out.println("Erro léxico: caractere inválido '" + getText()
            + "' | Linha: " + getLine()
            + " | Coluna: " + getCharPositionInLine());

        System.exit(1);
    } ;