package com.inm.helper;

import com.inm.compilation.CompilationContext;
import com.inm.antlr4.ProgramParser;
import com.inm.terminal.ExecutionParams;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class ParseHelper {
    public static CompilationContext parse(String source, ExecutionParams execParams) {
        PrintingLexer lexer = new PrintingLexer(CharStreams.fromString(source));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);

        ExceptionPrinter exceptionPrinter = new ExceptionPrinter();
        exceptionPrinter.setErrorListener(parser);

        ParseTree tree = parser.prog();

        ParseTree nameNode = tree.getChildCount() > 1 ? tree.getChild(1) : null;
        String programName = nameNode != null ? nameNode.getText() : "<desconhecido>";

        return new CompilationContext(programName, tree, parser, execParams);
    }
}