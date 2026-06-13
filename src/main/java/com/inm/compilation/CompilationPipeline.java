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
            runExecution(context);
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
        context.setFinalCode(finalCode);

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

    public void runExecution(CompilationContext context) {
        System.out.println("\n=== FASE 5: MONTAGEM E EXECUÇÃO ===");

        String asmPath = context.asmPath();
        if (asmPath == null) {
            System.err.println("[ERRO] Caminho do arquivo .asm não definido.");
            return;
        }

        try {
            Path originalAsm = Path.of(asmPath).toAbsolutePath();
            String programName = originalAsm.getFileName().toString().replace(".asm", "");

            Path programDir = originalAsm.getParent().resolve(programName);
            Files.createDirectories(programDir);

            Path asmInDir = programDir.resolve(programName + ".asm");
            Files.move(originalAsm, asmInDir, StandardCopyOption.REPLACE_EXISTING);
            context.setAsmPath(asmInDir.toString());

            String asmFile = asmInDir.toAbsolutePath().toString();
            String objFile = programDir.resolve(programName + ".o").toAbsolutePath().toString();
            String binFile = programDir.resolve(programName).toAbsolutePath().toString();

            System.out.println("Pasta de execução: " + programDir);

            System.out.println("Montando com NASM...");
            int nasmExit;

            if (ExecutionEnvHelper.isAvailable("nasm")) {
                nasmExit = run("nasm", "-f", "elf32", asmFile, "-o", objFile);
            } else if (ExecutionEnvHelper.isDockerImageAvailable()) {
                nasmExit = run("docker", "run", "--rm",
                        "-v", programDir.toAbsolutePath() + ":/app",
                        "-w", "/app",
                        ExecutionEnvHelper.PROJECT_IMAGE,
                        "nasm", "-f", "elf32", programName + ".asm", "-o", programName + ".o");
            } else {
                System.err.println("[ERRO] Nem NASM nativo nem imagem Docker do projeto encontrados.");
                System.err.println("Execute: docker compose build");
                return;
            }

            System.out.println("Linkando com ld...");
            int ldExit;

            if (ExecutionEnvHelper.isAvailable("ld")) {
                ldExit = run("ld", "-m", "elf_i386", objFile, "-o", binFile);
            } else if (ExecutionEnvHelper.isDockerImageAvailable()) {
                ldExit = run("docker", "run", "--rm",
                        "-v", programDir.toAbsolutePath() + ":/app",
                        "-w", "/app",
                        ExecutionEnvHelper.PROJECT_IMAGE,
                        "ld", "-m", "elf_i386", programName + ".o", "-o", programName);
            } else {
                System.err.println("[ERRO] ld não encontrado.");
                return;
            }

            System.out.println("\n=== SAÍDA DO PROGRAMA ===");
            int runExit;

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                runExit = run("docker", "run", "--rm",
                        "-v", programDir.toAbsolutePath() + ":/app",
                        "-w", "/app",
                        ExecutionEnvHelper.PROJECT_IMAGE,
                        "./" + programName);
            } else {
                runExit = run(binFile);
            }

            System.out.println("=========================");
            System.out.println("Programa encerrou com código: " + runExit);

        } catch (IOException e) {
            System.err.println("[ERRO] Falha ao executar: " + e.getMessage());
            System.err.println("Verifique se NASM e ld estão instalados.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[ERRO] Execução interrompida.");
        }

        System.out.println("Fase 5 concluída.");
    }

    private int run(String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .inheritIO()
                .start();
        return process.waitFor();
    }
}