package com.inm.helper;

import com.inm.antlr4.ProgramLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;

public class PrintingLexer extends ProgramLexer {

    public PrintingLexer(CharStream input) {
        super(input);
    }

    @Override
    public Token nextToken() {
        Token token = super.nextToken();

        if (token.getType() != Token.EOF) {
            String tipo = getVocabulary().getSymbolicName(token.getType());
            String tk = token.getText();
            String atributo = resolveAttribute(tipo, tk);

            System.out.printf("Token: %-10s | Tipo: %-10s | Atributo: %s%n", tk, tipo, atributo);
        }

        return token;
    }

    private static String resolveAttribute(String token, String text) {
        return switch (token) {
            case "ID", "CTE" -> text;
            case "CADEIA" -> text.substring(1, text.length() - 1);
            case "OPAD"   -> text.equals("+") ? "MAIS" : "MENOS";
            case "OPMULT" -> text.equals("*") ? "VEZES" : "DIV";
            case "OPLOG"  -> text.toUpperCase();
            case "OPNEG"  -> "NEG";
            case "OPREL"  -> switch (text) {
                case "<>" -> "DIFER";
                case "<=" -> "MENIG";
                case "<"  -> "MENOR";
                case ">=" -> "MAIG";
                case ">"  -> "MAIOR";
                case "==" -> "IGUAL";
                default   -> text;
            };
            default -> "-";
        };
    }
}