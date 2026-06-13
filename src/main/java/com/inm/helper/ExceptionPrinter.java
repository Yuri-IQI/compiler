package com.inm.helper;

import com.inm.antlr4.ProgramParser;
import com.inm.exceptions.ParsingException;
import org.antlr.v4.runtime.*;

public class ExceptionPrinter extends BaseErrorListener {

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer, Object symbol,
            int line, int col, String msg, RecognitionException e
    ) {
        throw new ParsingException(
                "\n------ERRO-----------------------------"
                + "\nLinha " + line + ":" + col + " " + msg + "\n" +
                "------ERRO-----------------------------"
        );
    }

    public void setErrorListener(ProgramParser parser) {
        parser.removeErrorListeners();
        parser.addErrorListener(this);
    }
}