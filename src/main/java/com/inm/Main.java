package com.inm;

import com.inm.antlr4.ProgramLexer;
import com.inm.antlr4.ProgramParser;
import com.inm.validators.GrammarValidator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import org.antlr.v4.gui.Trees;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nome do arquivo: ");
        String fileName = scanner.nextLine();

        String programFileContent = Files.readString(Path.of(fileName));
        ProgramLexer lexer = new ProgramLexer(CharStreams.fromString(programFileContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProgramParser parser = new ProgramParser(tokens);

        ParseTree tree = parser.start();
        Trees.inspect(tree, parser);
    }
}