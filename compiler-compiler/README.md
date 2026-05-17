# Analisador com Bison/Flex/GCC

Este analisador não será utilizado na versão final do compilador, servindo apenas para verificação da gramática e experimentações além do que é permitido pelo ANTLR4.

Esta pasta contém uma imagem Docker para compilar e executar o analisador usando Bison, Flex e GCC.
A imagem utiliza um bind mount desta pasta para que seja possível editar os arquivos localmente e executá-los dentro do container.

## Executando

Iniciar o container:

```bash
docker-compose up -d
```

Entrar no container:

```bash
docker exec -it ccc sh
```

Dentro do container, compilar e executar:

```bash
run
```

Ou apenas compilar:

```bash
compile
```

Executar o analisador:

```bash
./analisador
```

Ou com um arquivo de entrada:

```bash
./analisador < program.txt
```

## Compilação Manual

Caso queira compilar as etapas separadamente:

```bash
bison -d parser.y
flex lexer.l
gcc -o analisador parser.tab.c lex.yy.c -lfl
```

## Construção Manual da Imagem

```bash
docker build -t compiler-compiler-container .
docker run -it --name ccc -v .:/cc -w /cc compiler-compiler-container sh
```