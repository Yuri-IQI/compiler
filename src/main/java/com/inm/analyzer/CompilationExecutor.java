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

    public static ExecutionContext context = new ExecutionContext();

    public static void compile(String source) {
        compile(source, false);
    }

    public static void compile(String source, boolean showTree) {

        // Fase 1: Léxico + Sintático
        context = runParsing(source, showTree);

        // Fase 2: Semântica + 3AC
        SemanticAndIntermediateListener listener = runSemantic();

        // Fase 3: Otimização
        runOptimization();

        // Fase 4: Geração de Assembly
        runAssembly();
    }
    
    private static ExecutionContext runParsing(String source, boolean showTree) {
        System.out.println("\n=== FASE 1: ANÁLISE LÉXICA E SINTÁTICA ===");

        ExecutionContext result;
        try {
            result = ParseHelper.parse(source);
        } catch (ParsingException e) {
            System.err.println("[ERRO LÉXICO/SINTÁTICO] " + e.getMessage());
            throw e;
        }

        System.out.println("\n--- Árvore Sintática (texto) ---");
        System.out.println(result.tree().toStringTree(result.parser()));
        System.out.println("--------------------------------");

        if (showTree) {
            Trees.inspect(result.tree(), result.parser());
        }

        System.out.println("Fase 1 concluída. Programa: " + result.programName());
        return result;
    }

    private static SemanticAndIntermediateListener runSemantic() {
        System.out.println("\n=== FASE 2: ANÁLISE SEMÂNTICA E GERAÇÃO DE CÓDIGO INTERMEDIÁRIO ===");

        SemanticAndIntermediateListener listener = new SemanticAndIntermediateListener();
        ContextualizedWalker walker = new ContextualizedWalker(listener);
        walker.walk();

        if (listener.getErrorCount() > 0) {
            System.err.println("\n[FALHA SEMÂNTICA] " + listener.getErrorCount() + " erro(s) encontrado(s). Compilação interrompida.");
            throw new SemanticException(listener.getErrorCount() + " erro(s) semântico(s) encontrado(s).");
        }

        System.out.println("\n--- Código Intermediário (3AC) ---");
        System.out.println(listener.getGenerated3AC());
        System.out.println("----------------------------------");
        System.out.println("Fase 2 concluída com sucesso.");

        return listener;
    }

    private static void runOptimization() {
        System.out.println("\n=== FASE 3: OTIMIZAÇÃO DE CÓDIGO ===");
        Optimizer.optimize();
        Printer.printInstructions();
        System.out.println("Fase 3 concluída.");
    }

    private static void runAssembly() {
        System.out.println("\n=== FASE 4: GERAÇÃO DE CÓDIGO FINAL (ASSEMBLY x86) ===");
        AssemblyGenerator assemblyGenerator = new AssemblyGenerator(context);
        assemblyGenerator.generate();
        System.out.println("Fase 4 concluída.");
    }
}