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
}