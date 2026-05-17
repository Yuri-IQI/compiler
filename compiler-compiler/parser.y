%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern int yylineno;
void yyerror(const char *s);
%}

%union {
    int ival;
    char *sval;
}

/* Palavras Reservadas */
%token PROGRAM INTEGER BOOLEAN BEGIN_TOKEN END_TOKEN
%token WHILE DO READ VAR WRITE STRING
%token IF THEN ELSE
%token <ival> FALSE_TOKEN TRUE_TOKEN

/* Operadores */
%token OPLOG OPNEG OPREL OPAD OPMULT

/* Símbolos de Marcação */
%token ATRIB DPONTOS PVIG PONTO VIG ABPAR FPAR

/* Tokens com Atributo */
%token <ival> CTE
%token <sval> ID CADEIA

/* Resolve o conflito dangling else — ELSE tem maior precedência que THEN */
%nonassoc THEN
%nonassoc ELSE

%%

start : prog { printf("Análise sintática concluída com sucesso!\n"); } ;

prog : PROGRAM ID PVIG decls cmdComp PONTO ;

decls : VAR listDecls
      | /* vazio */
      ;

listDecls : declTip
          | declTip listDecls
          ;

declTip : listId DPONTOS tip PVIG ;

listId : ID
       | ID VIG listId
       ;

tip : INTEGER
    | BOOLEAN
    | STRING
    ;

cmdComp : BEGIN_TOKEN listCmd END_TOKEN ;

listCmd : cmd
        | cmd PVIG listCmd
        ;

cmd : cmdIf
    | cmdWhile
    | cmdRead
    | cmdWrite
    | cmdAtrib
    | cmdComp
    ;

cmdIf : IF exprRel THEN cmd
      | IF exprRel THEN cmd ELSE cmd
      ;

cmdWhile : WHILE exprRel DO cmd ;

cmdRead  : READ ABPAR listId FPAR ;
cmdWrite : WRITE ABPAR listW FPAR ;

listW : elemW
      | elemW VIG listW
      ;

elemW : exprRel
      | CADEIA
      ;

cmdAtrib : ID ATRIB exprRel ;

exprRel  : exprLog OPREL exprRel
         | exprLog
         ;

exprLog  : exprAd OPLOG exprLog
         | exprAd
         ;

exprAd   : exprMult OPAD exprAd
         | exprMult
         ;

exprMult : exprNeg OPMULT exprMult
         | exprNeg
         ;

exprNeg  : OPNEG exprNeg
         | exprPar
         ;

exprPar  : ABPAR exprRel FPAR
         | ID
         | CTE
         | TRUE_TOKEN
         | FALSE_TOKEN
         ;

%%

void yyerror(const char *s) {
    printf("Erro sintático: %s | Linha: %d\n", s, yylineno);
    exit(1);
}

int main() {
    setvbuf(stdout, NULL, _IONBF, 0); /* desabilita buffer completamente */
    setvbuf(stderr, NULL, _IONBF, 0);
    yyparse();
    printf("Análise concluída com sucesso!\n");
    return 0;
}