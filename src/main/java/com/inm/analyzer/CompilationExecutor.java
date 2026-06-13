package com.inm.analyzer;

import com.inm.exceptions.ParsingException;
import com.inm.exceptions.SemanticException;
import com.inm.generator.AssemblyGenerator;
import com.inm.generator.Optimizer;
import com.inm.helper.ParseHelper;
import com.inm.helper.Printer;
import com.inm.semantic.ContextualizedWalker;
import com.inm.semantic.SemanticAndIntermediateListener;
import org.antlr.v4.gui.Trees;

public class CompilationExecutor {

    public static void compile(String source) {
        compile(source, false);
    }

    public static void compile(String source, boolean showTree) {
        ExecutionContext context = runParsing(source, showTree);
        runSemantic(context);
        runOptimization(context);
        runAssembly(context);
    }

    private static ExecutionContext runParsing(String source, boolean showTree) {
        System.out.println("\n=== FASE 1: ANÁLISE LÉXICA E SINTÁTICA ===");

        ExecutionContext context;
        try {
            context = ParseHelper.parse(source);
        } catch (ParsingException e) {
            System.err.println("[ERRO LÉXICO/SINTÁTICO] " + e.getMessage());
            throw e;
        }

        System.out.println("\n--- Árvore Sintática (texto) ---");
        System.out.println(context.tree().toStringTree(context.parser()));
        System.out.println("--------------------------------");

        if (showTree) {
            Trees.inspect(context.tree(), context.parser());
        }

        System.out.println("Fase 1 concluída. Programa: " + context.programName());
        return context;
    }

    private static void runSemantic(ExecutionContext context) {
        System.out.println("\n=== FASE 2: ANÁLISE SEMÂNTICA E GERAÇÃO DE CÓDIGO INTERMEDIÁRIO ===");

        SemanticAndIntermediateListener listener = new SemanticAndIntermediateListener(context);
        ContextualizedWalker walker = new ContextualizedWalker(listener, context);
        walker.walk();

        if (listener.getErrorCount() > 0) {
            System.err.println("\n[FALHA SEMÂNTICA] " + listener.getErrorCount() + " erro(s) encontrado(s). Compilação interrompida.");
            throw new SemanticException(listener.getErrorCount() + " erro(s) semântico(s) encontrado(s).");
        }

        context.setSymbolTable(listener.getSymbolTable());
        context.setThreeAddressCode(listener.getThreeAddressCode());

        System.out.println("\n--- Código Intermediário (3AC) ---");
        System.out.println(context.threeAddressCode().getCode());
        System.out.println("----------------------------------");
        System.out.println("Fase 2 concluída com sucesso.");
    }

    private static void runOptimization(ExecutionContext context) {
        System.out.println("\n=== FASE 3: OTIMIZAÇÃO DE CÓDIGO ===");

        Optimizer optimizer = new Optimizer(context);
        optimizer.optimize();
        Printer.printInstructions(context);

        System.out.println("Fase 3 concluída.");
    }

    private static void runAssembly(ExecutionContext context) {
        System.out.println("\n=== FASE 4: GERAÇÃO DE CÓDIGO FINAL (ASSEMBLY x86) ===");

        AssemblyGenerator generator = new AssemblyGenerator(context);
        String finalCode = generator.generate();
        context.setFinalCode(finalCode);

        System.out.println("Fase 4 concluída.");
    }
}