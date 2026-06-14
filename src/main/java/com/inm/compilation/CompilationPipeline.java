package com.inm.compilation;

import com.inm.exceptions.ParsingException;
import com.inm.exceptions.SemanticException;
import com.inm.generator.assembly.AssemblyGenerator;
import com.inm.generator.Optimizer;
import com.inm.helper.ExecutionEnvHelper;
import com.inm.helper.ParseHelper;
import com.inm.helper.Printer;
import com.inm.semantic.ContextualizedWalker;
import com.inm.semantic.SemanticAndIntermediateListener;
import com.inm.terminal.ExecutionMode;
import com.inm.terminal.ExecutionParams;
import org.antlr.v4.gui.Trees;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CompilationPipeline {

    public void compile(String source, ExecutionMode executionMode, boolean execute, String output) {
        ExecutionParams execParams = new ExecutionParams(
                executionMode,
                false,
                "#",
                execute,
                output,
                "./"
        );

        compile(source, execParams);
    }

    public void compile(String source, ExecutionParams execParams) {
        CompilationContext context = runParsing(source, execParams);
        runSemantic(context);
        runOptimization(context);
        runAssembly(context);

        if (execParams.execute()) {
            Executor.runExecution(context);
        }
    }

    private CompilationContext runParsing(String source, ExecutionParams execParams) {
        System.out.println("\n=== FASE 1: ANÁLISE LÉXICA E SINTÁTICA ===");

        CompilationContext context;
        try {
            context = ParseHelper.parse(source, execParams);
        } catch (ParsingException e) {
            System.err.println("[ERRO LÉXICO/SINTÁTICO] " + e.getMessage());
            throw e;
        }

        System.out.println("\n--- Árvore Sintática (texto) ---");
        System.out.println(context.tree().toStringTree(context.parser()));
        System.out.println("--------------------------------");

        if (execParams.showTree()) {
            Trees.inspect(context.tree(), context.parser());
        }

        System.out.println("Fase 1 concluída. Programa: " + context.programName());
        return context;
    }

    private void runSemantic(CompilationContext context) {
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

    private void runOptimization(CompilationContext context) {
        System.out.println("\n=== FASE 3: OTIMIZAÇÃO DE CÓDIGO ===");
        new Optimizer(context).optimize();
        Printer.printInstructions(context);
        System.out.println("Fase 3 concluída.");
    }

    private void runAssembly(CompilationContext context) {
        System.out.println("\n=== FASE 4: GERAÇÃO DE CÓDIGO FINAL (ASSEMBLY x86) ===");

        AssemblyGenerator generator = new AssemblyGenerator(context);
        String finalCode = generator.generate();

        try {
            Path outputDir = Path.of(context.executionParams().output());
            if (!Files.exists(outputDir)) Files.createDirectories(outputDir);

            String fileName = context.programName().toLowerCase();
            Path asmPath = outputDir.resolve(fileName + ".asm");
            Files.writeString(asmPath, finalCode);
            context.setAsmPath(asmPath.toString());

            System.out.println("Assembly salvo em: " + asmPath);
        } catch (IOException e) {
            System.err.println("[ERRO] Não foi possível salvar o arquivo .asm: " + e.getMessage());
        }

        System.out.println("Fase 4 concluída.");
    }
}