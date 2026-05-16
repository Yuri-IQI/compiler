#!/bin/sh

set -e

echo "Compilando o parser..."
if ! bison -d parser.y; then
    echo -e "ERRO: falha ao compilar parser.y \n" >&2
    exit 1
fi

echo "Compilando o lexer..."
if ! flex lexer.l; then
    echo -e "ERRO: falha ao compilar lexer.l \n" >&2
    exit 1
fi

echo "Compilando o executavel..."
if ! gcc parser.tab.c lex.yy.c -o parser; then
    echo -e "ERRO: falha ao compilar com gcc \n" >&2
    exit 1
fi

echo -e "Compilacao concluida com sucesso! \n"