/* %code requires: emitido antes de tudo, define tipos usados no %union */
%code requires {
typedef struct ResultList {
    int values[100];
    int count;
} ResultList;
}

%code {
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    int yylex();
    void yyerror(const char *s);
    ResultList* create_result(int value);
    ResultList* apply_op(ResultList* a, ResultList* b, char op);
    ResultList* merge_results(YYSTYPE, YYSTYPE);
}

%glr-parser

%union {
    int num;
    ResultList* results;
}

%token <num> NUMBER
%type <results> expr input

%%

input:
    expr {
        printf("\nResultados Possiveis:\n");
        for (int i = 0; i < $1->count; i++) {
            printf("%d\n", $1->values[i]);
        }
    }
;

expr:
      expr '+' expr %merge <merge_results>
        {
            $$ = apply_op($1, $3, '+');
        }
    | expr '*' expr %merge <merge_results>
        {
            $$ = apply_op($1, $3, '*');
        }
    | expr '-' expr %merge <merge_results>
        {
            $$ = apply_op($1, $3, '-');
        }
    | expr '/' expr %merge <merge_results>
        {
            $$ = apply_op($1, $3, '/');
        }
    | NUMBER
        {
            $$ = create_result($1);
        }
;

%%

ResultList* merge_results(YYSTYPE x, YYSTYPE y) {
    ResultList* a = x.results;
    ResultList* b = y.results;
    ResultList* r = malloc(sizeof(ResultList));
    r->count = 0;
    for (int i = 0; i < a->count; i++)
        r->values[r->count++] = a->values[i];
    for (int i = 0; i < b->count; i++)
        r->values[r->count++] = b->values[i];
    return r;
}

ResultList* create_result(int value) {
    ResultList* r = malloc(sizeof(ResultList));
    r->count = 1;
    r->values[0] = value;
    return r;
}

ResultList* apply_op(ResultList* a, ResultList* b, char op) {
    ResultList* r = malloc(sizeof(ResultList));
    r->count = 0;
    for (int i = 0; i < a->count; i++) {
        for (int j = 0; j < b->count; j++) {
            int value = 0;
            switch (op) {
                case '+': value = a->values[i] + b->values[j]; break;
                case '*': value = a->values[i] * b->values[j]; break;
                case '-': value = a->values[i] - b->values[j]; break;
                case '/': value = a->values[i] / b->values[j]; break;
            }
            r->values[r->count++] = value;
        }
    }
    return r;
}

void yyerror(const char *s) {
    printf("Erro: %s\n", s);
}

int main() {
    printf("Digite expressao:\n");
    yyparse();
    return 0;
}
