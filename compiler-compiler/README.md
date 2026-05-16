# Parser com Bison/Flex/GCC

Este parser são será utilizado na versão final do compilador, servindo apenas para verificação da gramática e experimentações além do que é permitido pelo ANTLR4.

Este pasta contém uma imagem Docker para compilar e executar um parser usando Bison, Flex e GCC.
A imagem utiliza um bind mount dessa pasta para que seja possível editar os arquivos localmente e executá-los dentro do container.
Para modificar o parser, basta editar os arquivos `parser.y` e `lexer.l` e depois executar o comando `run` dentro do container para compilar e rodar o parser.

## Executando Parser com Bison/Flex/GCC

Executar docker-compose:

```bash
docker-compose up -d
```

Caso deseje, também é possível construir a imagem manualmente:

```bash
docker build -t compiler-compiler-container .
```

e rodar o container:

```bash
docker run -it --name ccc -v .:/cc -w /cc compiler-compiler-container sh
```

Entrar no container:

```bash
docker exec -it ccc sh
```

Dentro do container, executar o parser:

```bash
run
```

Ou compilar o parser separadamente:

```bash
compile
```

Para utilizar o parser, use o comando:

```bash
./parser
```

Caso queira testar alguma etapa da compilação, como o lexer ou o parser, é possível executar os comandos diretamente:

```bash
bison -d parser.y
flex lexer.l
gcc -o parser parser.tab.c lex.yy.c -lfl
```