package com.inm;

import com.inm.antlr4.ProgramLexer;
import com.inm.validators.GrammarValidator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

public class Main {
    public static void main(String[] args) {
        String input = "abcdefgh123456789";

        CharStream charStream = CharStreams.fromString(input);
        ProgramLexer lexer = new ProgramLexer(charStream);

        GrammarValidator.validarTamanhoIdentificador(lexer);
    }
}