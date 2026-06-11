    package com.inm.helper;

    import com.inm.analyzer.PrintingLexer;
    import com.inm.antlr4.ProgramLexer;
    import com.inm.antlr4.ProgramParser;
    import org.antlr.v4.runtime.CharStreams;
    import org.antlr.v4.runtime.CommonTokenStream;
    import org.antlr.v4.runtime.tree.ParseTree;

    import java.util.List;

    public class ParseHelper {

        public record ParseResult(String programName, ParseTree tree, ProgramParser parser, List<String> errors) {
            public boolean isValid() { return errors.isEmpty(); }
        }

        public static ParseResult parse(String source, boolean printErrors) {
            PrintingLexer lexer = new PrintingLexer(CharStreams.fromString(source));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ProgramParser parser = new ProgramParser(tokens);

            Printer printer = new Printer();
            printer.setErrorListener(parser);

            ParseTree tree = parser.prog();

            ParseTree nameNode = tree.getChildCount() > 1 ? tree.getChild(1) : null;
            String programName = nameNode != null ? nameNode.getText() : "<desconhecido>";

            if (printErrors) printer.printErrors();

            return new ParseResult(programName, tree, parser, printer.getErrors());
        }

        public static ParseResult parse(String source) {
            return parse(source, true);
        }
    }