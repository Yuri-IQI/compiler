package com.inm.compilation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Executor {
    public static void runExecution(CompilationContext context) {
        System.out.println("\n=== FASE 5: MONTAGEM E EXECUÇÃO ===");

        String asmPath = context.asmPath();
        if (asmPath == null) {
            System.err.println("[ERRO] Caminho do arquivo .asm inválido");
            return;
        }

        try {
            WorkspacePaths paths = resolveWorkspace(context, Path.of(asmPath).toAbsolutePath());
            mount(paths);
            link(paths);
            run(paths, context);
        } catch (IOException e) {
            System.err.println("[ERRO] Falha de I/O durante a execução: " + e.getMessage());
            System.err.println("Verifique se o MASM32 SDK está instalado corretamente em C:\\masm32.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[ERRO] Execução interrompida.");
        }

        System.out.println("Fase 5 concluída.");
    }

    private static void mount(WorkspacePaths paths) throws IOException, InterruptedException {
        System.out.println("Montando com MASM (ml.exe)...");
        int exit = ProcessExecutor.mount(paths.asmFile(), paths.objFile());
        if (exit != 0) {
            throw new IOException("MASM falhou com código de saída: " + exit);
        }
        System.out.println("Montagem concluída: " + paths.objFile());
    }

    private static void link(WorkspacePaths paths) throws IOException, InterruptedException {
        System.out.println("Linkando com link.exe...");
        int exit = ProcessExecutor.link(paths.objFile(), paths.exeFile());
        if (exit != 0) {
            throw new IOException("link.exe falhou com código de saída: " + exit);
        }
        System.out.println("Linkagem concluída: " + paths.exeFile());
    }

    private static void run(WorkspacePaths paths, CompilationContext context) throws IOException, InterruptedException {
        System.out.println("\n=== SAÍDA DO PROGRAMA ===");
        System.out.flush();
        System.err.flush();

        String stdIn = context.stdInContent();
        int exit = ProcessExecutor.execute(paths.exeFile(), stdIn);
        System.out.println("=========================");
        System.out.println("Programa encerrou com código: " + exit);
    }

    private static WorkspacePaths resolveWorkspace(CompilationContext context, Path originalAsm) throws IOException {
        String programName = stripExtension(originalAsm.getFileName().toString());
        Path workDir;
        Path asmFile;

        if (originalAsm.getParent().getFileName().toString().equals(programName)) {
            workDir = originalAsm.getParent();
            asmFile = originalAsm;
        } else {
            workDir = originalAsm.getParent().resolve(programName);
            Files.createDirectories(workDir);
            asmFile = workDir.resolve(programName + ".asm");
            Files.move(originalAsm, asmFile, StandardCopyOption.REPLACE_EXISTING);
            context.setAsmPath(asmFile.toString());
        }

        System.out.println("Diretório de trabalho: " + workDir);

        Path objFile = workDir.resolve(programName + ".obj");
        Path exeFile = workDir.resolve(programName + ".exe");

        return new WorkspacePaths(asmFile, objFile, exeFile);
    }

    private static String stripExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(0, dot) : filename;
    }

    private record WorkspacePaths(Path asmFile, Path objFile, Path exeFile) {}
}