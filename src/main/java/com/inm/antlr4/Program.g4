grammar Program;

// TODO: Adicionar tratamento de exceções e Imprimir tokens

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
listCmd : cmd PVIG | cmd PVIG listCmd ;
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
PROGRAM : 'PROGRAM' { System.out.println("Token: " + getText() + " | Tipo: PROGRAM"); } ;
INTEGER : 'INTEGER' { System.out.println("Token: " + getText() + " | Tipo: INTEGER"); } ;
BOOLEAN : 'BOOLEAN' { System.out.println("Token: " + getText() + " | Tipo: BOOLEAN"); } ;
BEGIN : 'BEGIN' { System.out.println("Token: " + getText() + " | Tipo: BEGIN"); } ;
END : 'END' { System.out.println("Token: " + getText() + " | Tipo: END"); } ;
WHILE : 'WHILE' { System.out.println("Token: " + getText() + " | Tipo: WHILE"); } ;
DO : 'DO' { System.out.println("Token: " + getText() + " | Tipo: DO"); } ;
READ : 'READ' { System.out.println("Token: " + getText() + " | Tipo: READ"); } ;
VAR : 'VAR' { System.out.println("Token: " + getText() + " | Tipo: VAR"); } ;
FALSE : 'FALSE' { System.out.println("Token: " + getText() + " | Tipo: FALSE"); } ;
TRUE : 'TRUE' { System.out.println("Token: " + getText() + " | Tipo: TRUE"); } ;
WRITE : 'WRITE' { System.out.println("Token: " + getText() + " | Tipo: WRITE"); } ;
STRING : 'STRING' { System.out.println("Token: " + getText() + " | Tipo: STRING"); } ;
IF : 'IF' { System.out.println("Token: " + getText() + " | Tipo: IF"); } ;
THEN : 'THEN' { System.out.println("Token: " + getText() + " | Tipo: THEN"); } ;
ELSE : 'ELSE' { System.out.println("Token: " + getText() + " | Tipo: ELSE"); } ;

CTE : [0-9]+
  { System.out.println("Token: " + getText() + " | Tipo: CTE | Atributo: " + getText()); } ;

// Operadores Aritméticos e Lógicos
OPAD  : '+' { System.out.println("Token: " + getText() + " | Tipo: OPAD | Atributo: MAIS"); }
    | '-' { System.out.println("Token: " + getText() + " | Tipo: OPAD | Atributo: MENOS"); } ;
OPMULT : '*' { System.out.println("Token: " + getText() + " | Tipo: OPMULT | Atributo: VEZES"); }
    | '/' { System.out.println("Token: " + getText() + " | Tipo: OPMULT | Atributo: DIV"); } ;
OPLOG : 'OR' { System.out.println("Token: " + getText() + " | Tipo: OPLOG | Atributo: OR"); }
    | 'AND' { System.out.println("Token: " + getText() + " | Tipo: OPLOG | Atributo: AND"); } ;
OPNEG : '~' { System.out.println("Token: " + getText() + " | Tipo: OPNEG | Atributo: NEG"); } ;

// Operadores Relacionais
OPREL : '<>' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: DIFER"); }
    | '<=' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: MENIG"); }
    | '>=' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: MAIG"); }
    | '==' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: IGUAL"); }
    | '<' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: MENOR"); }
    | '>' { System.out.println("Token: " + getText() + " | Tipo: OPREL | Atributo: MAIOR"); } ;

// Simbolos de Marcação
ATRIB : ':=' { System.out.println("Token: " + getText() + " | Tipo: ATRIB"); } ;
DPONTOS : ':' { System.out.println("Token: " + getText() + " | Tipo: DPONTOS"); } ;
PVIG : ';' { System.out.println("Token: " + getText() + " | Tipo: PVIG"); } ;
PONTO : '.' { System.out.println("Token: " + getText() + " | Tipo: PONTO"); } ;
VIG : ',' { System.out.println("Token: " + getText() + " | Tipo: VIG"); } ;
ABPAR : '(' { System.out.println("Token: " + getText() + " | Tipo: ABPAR"); } ;
FPAR : ')' { System.out.println("Token: " + getText() + " | Tipo: FPAR"); } ;

ID : [a-z][a-z0-9]*
   {
       if (getText().length() > 16) { setText(getText().substring(0, 16)); }
       System.out.println("Token: " + getText() + " | Tipo: ID | Atributo: " + getText());
   } ;

CADEIA : '"' (~["\r\n])* '"'
       { System.out.println("Token: " + getText() + " | Tipo: CADEIA | Atributo: " + getText()); } ;

WS_RULE : [ \t\r\n]+ -> skip ;
COMMENT_RULE : '/' .*? '/' -> skip ;

ERR : .
    {
        System.out.println("Erro léxico: caractere inválido '" + getText() +
            "' | Linha: " + getLine() +
            " | Coluna: " + getCharPositionInLine());
        System.exit(1);
    } ;