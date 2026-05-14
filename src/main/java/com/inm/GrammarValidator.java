package com.inm;

import com.inm.antlr4.ProgramLexer;
import org.antlr.v4.runtime.Token;

public class GrammarValidator {

    public static void validarTamanhoIdentificador(ProgramLexer lexer) {
        Token token = lexer.nextToken();
        while (token.getType() != Token.EOF) {
            System.out.println("Token: " + token.getText() + " | Tamanho: " + token.getText().length());
            token = lexer.nextToken();
        }
    }
}
