package com.inm.helper;

import com.inm.antlr4.ProgramParser;
import org.antlr.v4.runtime.*;

import java.util.*;

public class Printer extends BaseErrorListener {

    private final List<String> errors = new ArrayList<>();

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer, Object symbol,
            int line, int col, String msg, RecognitionException e
    ) {
        errors.add(
            "------ERRO-----------------------------"
            + "\nLinha " + line + ":" + col + " " + msg + "\n" +
            "------ERRO-----------------------------"
        );
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void setErrorListener(ProgramParser parser) {
        parser.removeErrorListeners();
        parser.addErrorListener(this);
    }

    public void printErrors() {
        errors.forEach(System.out::println);
    }

    public void printTokens(CommonTokenStream tokens, ProgramParser parser) {
        tokens.fill();
        for (Token tk : tokens.getTokens()) {
            if (tk.getType() == Token.EOF) continue;

            String token = parser.getVocabulary().getSymbolicName(tk.getType());
            String text = tk.getText();

            System.out.printf("Token: %-10s | Tipo: %-10s | Atributo: %s%n",
                    text, token, resolveAttribute(token, text));
        }
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