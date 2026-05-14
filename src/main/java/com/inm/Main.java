package com.inm;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import com.inm.antlr4.ProgramLexer;

public class Main {
    public static void main(String[] args) {
        String input = "abcdefgh123456789";

        CharStream charStream = CharStreams.fromString(input);
        ProgramLexer lexer = new ProgramLexer(charStream);

        GrammarValidator.validarTamanhoIdentificador(lexer);
    }
}