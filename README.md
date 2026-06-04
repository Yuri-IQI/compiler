# Analisador Léxico e Sintático

---

## Requisitos

- Java 21+
- Maven 3.8+
- ANTLR4 4.13.2+

---

## Estrutura do Projeto

```
src/
  main/
    antlr4/com/inm/antlr4/
      Program.g4              # Gramática da linguagem
    java/com/inm/
      Main.java
      analyzer/
        ProgramAnalyzer.java  # Leitura e análise do código
      helper/
        ParseHelper.java      # Criação da árvore sintática
        Printer.java          # Impressão de tokens e erros
  test/
    java/com/inm/
      ProgramParserTest.java  # Suite de testes para os scripts em resources/scripts
    resources/scripts/
      valid/                  # Scripts corretos
      invalid/                # Scripts com erros
```

---

## Compilar e Executar

### Via Maven

```bat
# compilar classes de lexer e parser pelo ANTLR4 (substituir %ANTLR_JAR% pelo caminho para o jar do ANTLR)
java -jar %ANTLR_JAR% src\main\java\com\inm\antlr4\Program.g4

# lê um script dos resources
./mvnw compile exec:java -DreadFile=true

# escreve o código manualmente
./mvnw compile exec:java -DreadFile=false

# abre a visualização do parser em uma árvore
./mvnw compile exec:java -DreadFile=false -DshowTree=true
```

### Via JAR

```bat
# compilar classes de lexer e parser pelo ANTLR4 (substitui %ANTLR_JAR% pelo caminho para o jar do ANTLR)
java -jar %ANTLR_JAR% src\main\java\com\inm\antlr4\Program.g4

# gerar o JAR
./mvnw package

# lê um script dos resources
java -DreadFile=true -jar .\target\analisador.jar

# escreve o código manualmente
java -DreadFile=false -jar .\target\analisador.jar

# abre a visualização do parser em uma árvore
java -DreadFile=false -DshowTree=true -jar .\target\analisador.jar
```

---

## Modos de Entrada

### Leitura de arquivo (`-DreadFile=true`)

Lista os scripts disponíveis nos resources e permite escolher por código ou caminho:

```
Arquivos disponíveis:
Válidos
  [V1] atribuicao.prog
  [V2] comentario-e-div.prog
  ...
Inválidos
  [I1] attr-sem-expr.prog
  [I2] if-sem-then.prog
  ...

Escolha um código (ex: V1, I2) ou digite o caminho:
```

### Escrita manual (`-DreadFile=false`)

```
Escreva o código (@ em nova linha para analisar):
program teste;
begin
    write("ola")
end.
@
```

---

## Saída

Para cada token reconhecido, o analisador imprime seu texto, tipo e atributo:

```
Token: program    | Tipo: PROGRAM    | Atributo: -
Token: atribuicao | Tipo: ID         | Atributo: atribuicao
Token: ;          | Tipo: PVIG       | Atributo: -
Token: 2          | Tipo: CTE        | Atributo: 2
Token: +          | Tipo: OPAD       | Atributo: MAIS
Token: <=         | Tipo: OPREL      | Atributo: MENIG
```

Em caso de erro sintático:

```
------ERRO-----------------------------
Linha 5:4 mismatched input 'begin' expecting {'INTEGER', 'BOOLEAN', 'STRING'}
------ERRO-----------------------------
```

Caso o argumento `-DshowTree=true` seja passado, a árvore sintática é exibida em uma janela gráfica após a análise.

---

## Testes

```bat
./mvnw test
```

Os testes verificam automaticamente todos os scripts em `src/test/resources/scripts/valid/` e `src/test/resources/scripts/invalid/`.

```
✓ acceptsValidScripts(scripts/valid/atribuicao.prog)
✓ acceptsValidScripts(scripts/valid/condicional.prog)
...
✓ rejectsInvalidScripts(scripts/invalid/sem-begin.prog)
✓ rejectsInvalidScripts(scripts/invalid/tipo-invalido.prog)
...
```

---

## Estrutura do Código

### `Program.g4`

Gramática LL(1) da linguagem, processada pelo ANTLR4 para gerar automaticamente o lexer (`ProgramLexer`) e o parser (`ProgramParser`).

A gramática define:

- **Tokens**: palavras reservadas, operadores, símbolos de marcação e tokens de atributo variável.
- **Regras do parser**: estrutura completa da linguagem, da declaração de variáveis aos comandos e expressões, organizada em níveis de precedência.

---

### `Main`

Ponto de entrada da aplicação. Lê as system properties `-DreadFile` e `-DshowTree` e delega para `ProgramAnalyzer`.

---

### `ProgramAnalyzer`

Responsável pela leitura do código-fonte e pela orquestração da análise.

| Método | Descrição                                                                                                                                                                        |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `run(boolean shouldReadFile)` | Decide entre leitura de arquivo ou entrada manual e chama `analyze`.                                                                                                             |
| `analyze(String source, boolean showTree)` | Chama `ParseHelper.parse` e exibe o resultado; se `showTree`, abre a janela da árvore sintática.                                                                                 |
| `readFile(Scanner scanner)` | Lista os scripts dos resources, interpreta o código de seleção (`V1`, `I2`) ou caminho digitado e retorna o conteúdo.                                                            |
| `readScript(Scanner scanner)` | Lê linhas do stdin até encontrar `@` e retorna o código digitado. O caractere `@` foi escolhido como FLAG por não ser usado gramática e poder ser descartado ao final do script. |
| `mapFiles()` | Carrega e exibe os arquivos `.prog` disponíveis nos diretórios `scripts/valid` e `scripts/invalid` dos resources.                                                                |
| `listResourceFiles(String folder)` | Lista arquivos `.prog` em um diretório dos resources, com suporte tanto ao filesystem (Maven) quanto ao JA.R                                                                     |
| `showTree(ParseTree, ProgramParser)` | Exibe a árvore sintática em uma janela Swing e aguarda o fechamento antes de encerrar.                                                                                           |

---

### `ParseHelper`

Responsável por construir a árvore sintática a partir de um código-fonte.

| Método / Tipo | Descrição                                                                                             |
|---------------|-------------------------------------------------------------------------------------------------------|
| `parse(String source)` | Cria o lexer, o parser e o `Printer`, executa o parse e retorna um `ParseResult`.                     |
| `parse(String source, boolean printErrors)` | Sobrecarga que controla se os erros são impressos, usada nos testes para suprimir saída.              |
| `ParseResult` | Record com `programName`, `tree`, `parser` e `errors`; expõe `isValid()` para checar se não há erros. |

---

### `Printer`

Implementa `BaseErrorListener` do ANTLR4 para capturar erros sintáticos, além de imprimir os tokens e seus atributos.

| Método | Descrição                                                                                                                                                                                       |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `syntaxError(...)` | Chamado automaticamente pelo ANTLR ao encontrar um erro, formata e armazena a mensagem.                                                                                                         |
| `setErrorListener(ProgramParser)` | Remove os listeners padrão do ANTLR e registra o `Printer` como listener de erros.                                                                                                              |
| `printTokens(CommonTokenStream, ProgramParser)` | Itera sobre todos os tokens e imprime texto, tipo e atributo de cada um.                                                                                                                        |
| `printErrors()` | Imprime todos os erros capturados.                                                                                                                                                              |
| `getErrors()` | Retorna a lista imutável de erros, usada pelo `ParseResult`.                                                                                                                                    |
| `resolveAttribute(String token, String text)` | Resolve o atributo semântico de cada token: para `ID`, `CTE` e `CADEIA` retorna o valor, para operadores retorna o nome semântico (`MAIS`, `VEZES`, `DIFER`, etc.), para os demais retorna `-`. |
