%{
#include "parser.tab.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void truncarIdentificador(char *texto) {
    if (strlen(texto) > 16) {
        texto[16] = '\0';
    }
}
%}

%option caseless
%option noyywrap
%option yylineno

%%

"PROGRAM"   { printf("Token: %-16s | Tipo: PROGRAM\n",  yytext); fflush(stdout); return PROGRAM;      }
"INTEGER"   { printf("Token: %-16s | Tipo: INTEGER\n",  yytext); return INTEGER;      }
"BOOLEAN"   { printf("Token: %-16s | Tipo: BOOLEAN\n",  yytext); return BOOLEAN;      }
"STRING"    { printf("Token: %-16s | Tipo: STRING\n",   yytext); return STRING;       }
"BEGIN"     { printf("Token: %-16s | Tipo: BEGIN\n",    yytext); return BEGIN_TOKEN;  }
"END"       { printf("Token: %-16s | Tipo: END\n",      yytext); return END_TOKEN;    }
"IF"        { printf("Token: %-16s | Tipo: IF\n",       yytext); return IF;           }
"THEN"      { printf("Token: %-16s | Tipo: THEN\n",     yytext); return THEN;         }
"ELSE"      { printf("Token: %-16s | Tipo: ELSE\n",     yytext); return ELSE;         }
"WHILE"     { printf("Token: %-16s | Tipo: WHILE\n",    yytext); return WHILE;        }
"DO"        { printf("Token: %-16s | Tipo: DO\n",       yytext); return DO;           }
"READ"      { printf("Token: %-16s | Tipo: READ\n",     yytext); return READ;         }
"WRITE"     { printf("Token: %-16s | Tipo: WRITE\n",    yytext); return WRITE;        }
"VAR"       { printf("Token: %-16s | Tipo: VAR\n",      yytext); return VAR;          }
"TRUE"      { printf("Token: %-16s | Tipo: TRUE\n",     yytext); return TRUE_TOKEN;   }
"FALSE"     { printf("Token: %-16s | Tipo: FALSE\n",    yytext); return FALSE_TOKEN;  }
"OR"        { printf("Token: %-16s | Tipo: OPLOG  | Atributo: OR\n",    yytext); return OPLOG; }
"AND"       { printf("Token: %-16s | Tipo: OPLOG  | Atributo: AND\n",   yytext); return OPLOG; }

[0-9]+      {
                yylval.ival = atoi(yytext);
                printf("Token: %-16s | Tipo: CTE    | Atributo: %s\n", yytext, yytext);
                return CTE;
            }

"<>"        { printf("Token: %-16s | Tipo: OPREL  | Atributo: DIFER\n",  yytext); return OPREL; }
"<="        { printf("Token: %-16s | Tipo: OPREL  | Atributo: MENIG\n",  yytext); return OPREL; }
">="        { printf("Token: %-16s | Tipo: OPREL  | Atributo: MAIG\n",   yytext); return OPREL; }
"=="        { printf("Token: %-16s | Tipo: OPREL  | Atributo: IGUAL\n",  yytext); return OPREL; }
"<"         { printf("Token: %-16s | Tipo: OPREL  | Atributo: MENOR\n",  yytext); return OPREL; }
">"         { printf("Token: %-16s | Tipo: OPREL  | Atributo: MAIOR\n",  yytext); return OPREL; }

"+"         { printf("Token: %-16s | Tipo: OPAD   | Atributo: MAIS\n",   yytext); return OPAD;   }
"-"         { printf("Token: %-16s | Tipo: OPAD   | Atributo: MENOS\n",  yytext); return OPAD;   }
"*"         { printf("Token: %-16s | Tipo: OPMULT | Atributo: VEZES\n",  yytext); return OPMULT; }
"/"         { printf("Token: %-16s | Tipo: OPMULT | Atributo: DIV\n",    yytext); return OPMULT; }
"~"         { printf("Token: %-16s | Tipo: OPNEG  | Atributo: NEG\n",    yytext); return OPNEG;  }

":="        { printf("Token: %-16s | Tipo: ATRIB\n",   yytext); return ATRIB;   }
":"         { printf("Token: %-16s | Tipo: DPONTOS\n", yytext); return DPONTOS; }
";"         { printf("Token: %-16s | Tipo: PVIG\n",    yytext); return PVIG;    }
"."         { printf("Token: %-16s | Tipo: PONTO\n",   yytext); return PONTO;   }
","         { printf("Token: %-16s | Tipo: VIG\n",     yytext); return VIG;     }
"("         { printf("Token: %-16s | Tipo: ABPAR\n",   yytext); return ABPAR;   }
")"         { printf("Token: %-16s | Tipo: FPAR\n",    yytext); return FPAR;    }

[a-z][a-z0-9]* {
                    truncarIdentificador(yytext);
                    yylval.sval = strdup(yytext);
                    printf("Token: %-16s | Tipo: ID     | Atributo: %s\n", yytext, yytext);
                    return ID;
                }

\"[^"\r\n]*\"  {
                    yylval.sval = strdup(yytext);
                    printf("Token: %-16s | Tipo: CADEIA | Atributo: %s\n", yytext, yytext);
                    return CADEIA;
               }

[ \t\r\n]+          { /* descartar espaços em branco */ }
"/"[^/]*"/"         { /* descartar comentários */       }

.   {
        printf("Erro léxico: caractere inválido '%s' | Linha: %d\n", yytext, yylineno);
        exit(1);
    }

%%