#!/bin/sh

if [ -z "$1" ]; then
    echo "Uso: sh run.sh <caminho-do-arquivo.asm>"
    echo "Exemplo: sh run.sh outputs/calculodesconto.asm"
    echo "Exemplo: sh run.sh calculodesconto.asm"
    exit 1
fi

INPUT="$1"

if [ "${INPUT#/}" = "$INPUT" ]; then
    INPUT="$(pwd)/$INPUT"
fi

if [ ! -f "$INPUT" ]; then
    echo "[ERRO] Arquivo não encontrado: $INPUT"
    exit 1
fi

DIR="$(dirname "$INPUT")"
BASENAME="$(basename "$INPUT" .asm)"

ASM="$DIR/${BASENAME}.asm"
OBJ="$DIR/${BASENAME}.o"
BIN="$DIR/${BASENAME}"

echo "=== Montando com NASM: $ASM ==="
nasm -f elf32 "$ASM" -o "$OBJ"
if [ $? -ne 0 ]; then
    echo "[ERRO] Falha na montagem."
    exit 1
fi

echo "=== Linkando com ld ==="
ld -m elf_i386 "$OBJ" -o "$BIN"
if [ $? -ne 0 ]; then
    echo "[ERRO] Falha na linkagem."
    exit 1
fi

echo "=== Executando: $BASENAME ==="
echo
"$BIN"
echo
echo "=== Encerrado com código: $? ==="