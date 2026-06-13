package com.inm.helper;

import com.inm.analyzer.ExecutionContext;
import com.inm.antlr4.ProgramParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class ParseHelper {
    public static ExecutionContext parse(String source) {
        PrintingLexer lexer = new PrintingLexer(CharStreams.fromString(source));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);

        ExceptionPrinter exceptionPrinter = new ExceptionPrinter();
        exceptionPrinter.setErrorListener(parser);

        ParseTree tree = parser.prog();

        ParseTree nameNode = tree.getChildCount() > 1 ? tree.getChild(1) : null;
        String programName = nameNode != null ? nameNode.getText() : "<desconhecido>";

        return new ExecutionContext(programName, tree, parser);
    }
}