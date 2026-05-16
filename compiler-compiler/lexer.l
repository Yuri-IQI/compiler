%option noyywrap

%{
#include "parser.tab.h"
#include <stdlib.h>
%}

%%

[0-9]+ {
    yylval.num = atoi(yytext);
    return NUMBER;
}

"+" return '+';

"*" return '*';

"-" return '-';

"/" return '/';

[ \t\r] ;

\n return 0;

. {
    printf("Caractere invalido: %s\n", yytext);
}

%%