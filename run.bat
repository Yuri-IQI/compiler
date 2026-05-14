@echo off

set ANTLR_VERSION=4.13.2
set ANTLR_JAR=antlr-%ANTLR_VERSION%-complete.jar
set ANTLR_URL=https://www.antlr.org/download/%ANTLR_JAR%

if not exist %ANTLR_JAR% (
    echo Arquivo %ANTLR_JAR% nao encontrado na raíz do projeto. Baixando...
    curl -o %ANTLR_JAR% %ANTLR_URL%
)

echo Gerando arquivos ANTLR...
java -jar %ANTLR_JAR% src\main\java\com\inm\antlr4\Program.g4

echo Compilando...
javac -cp ".;%ANTLR_JAR%" src\main\java\com\inm\antlr4\*.java src\main\java\com\inm\*.java

echo Executando...
java -cp ".;%ANTLR_JAR%;src\main\java" com.inm.Main

exit