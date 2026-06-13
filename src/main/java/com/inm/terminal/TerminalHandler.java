package com.inm.terminal;

import com.inm.compilation.CompilationPipeline;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TerminalHandler {

    private final CompilationPipeline compilationPipeline = new CompilationPipeline();

    public void run(ExecutionParams params) throws IOException {
        switch (params.mode()) {
            case TEST -> runTest(params);
            case SCRIPT -> runScript(params);
            case FILE -> runFile(params);
            case DIR -> runDir(params);
        }
    }

    private void runTest(ExecutionParams params) throws IOException {
        Map<String, List<Path>> fileMap = mapFiles();

        System.out.print("\nEscolha um código (ex: V1, I2) ou caminho: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine().trim();
            String source = resolveTestInput(input, fileMap);
            compilationPipeline.compile(source, params);
        }
    }

    private String resolveTestInput(String input, Map<String, List<Path>> fileMap) throws IOException {
        try {
            char prefix = Character.toUpperCase(input.charAt(0));
            int index = Integer.parseInt(input.substring(1)) - 1;

            String key = switch (prefix) {
                case 'V' -> "Válidos";
                case 'I' -> "Inválidos";
                default -> throw new IOException("Prefixo inválido: " + prefix + " (use V ou I)");
            };

            List<Path> paths = fileMap.get(key);
            if (index < 0 || index >= paths.size())
                throw new IOException("Índice fora do intervalo: " + (index + 1));

            String folder = key.equals("Válidos") ? "scripts/valid/" : "scripts/invalid/";
            String resourcePath = folder + paths.get(index).getFileName();

            try (var is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (is == null) throw new IOException("Recurso não encontrado: " + resourcePath);
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (NumberFormatException e) {
            try (var is = getClass().getClassLoader().getResourceAsStream(input)) {
                if (is != null) return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
            try {
                return Files.readString(Path.of(input), StandardCharsets.UTF_8);
            } catch (NoSuchFileException ex) {
                throw new IOException("Arquivo não encontrado: " + input, ex);
            }
        }
    }

    private void runScript(ExecutionParams params) {
        System.out.println("Escreva o script (digite " + params.flag() + " em nova linha para compilar):");
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals(params.flag())) break;
                sb.append(line).append("\n");
            }
        }
        compilationPipeline.compile(sb.toString(), params);
    }

    private void runFile(ExecutionParams params) throws IOException {
        Path path = Path.of(params.filePath());

        if (!Files.exists(path)) {
            throw new IOException("Arquivo não encontrado: " + path);
        }
        if (!path.toString().endsWith(".prog")) {
            throw new IOException("Arquivo não é um .prog: " + path);
        }

        System.out.println("Compilando arquivo: " + path);
        String source = Files.readString(path, StandardCharsets.UTF_8);
        compilationPipeline.compile(source, params);
    }

    private void runDir(ExecutionParams params) throws IOException {
        Path dir = Path.of(params.filePath());

        if (!Files.isDirectory(dir)) {
            throw new IOException("Caminho não é uma pasta: " + dir);
        }

        List<Path> files;
        try (var stream = Files.list(dir)) {
            files = stream
                    .filter(p -> p.toString().endsWith(".prog"))
                    .sorted()
                    .toList();
        }

        if (files.isEmpty()) {
            System.out.println("Nenhum arquivo .prog encontrado em: " + dir);
            return;
        }

        System.out.println("Encontrados " + files.size() + " arquivo(s) .prog em: " + dir);
        int success = 0;
        int failure = 0;

        for (Path file : files) {
            System.out.println("\n--- Compilando: " + file.getFileName() + " ---");
            try {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                compilationPipeline.compile(source, params);
                success++;
            } catch (Exception e) {
                System.err.println("[ERRO] " + file.getFileName() + ": " + e.getMessage());
                failure++;
            }
        }

        System.out.println("\n=== Resultado: " + success + " sucesso(s), " + failure + " falha(s) ===");
    }

    private Map<String, List<Path>> mapFiles() throws IOException {
        Map<String, List<Path>> fileMap = new LinkedHashMap<>();
        fileMap.put("Válidos", listResourceFiles("scripts/valid"));
        fileMap.put("Inválidos", listResourceFiles("scripts/invalid"));

        boolean empty = fileMap.values().stream().allMatch(List::isEmpty);
        if (empty) throw new IOException("Nenhum arquivo encontrado em resources/scripts");

        System.out.println("Arquivos disponíveis:");
        fileMap.forEach((key, paths) -> {
            System.out.println(key);
            for (int i = 0; i < paths.size(); i++)
                System.out.printf("[%c%d] %s%n", key.charAt(0), i + 1, paths.get(i).getFileName());
        });

        return fileMap;
    }

    private List<Path> listResourceFiles(String folder) throws IOException {
        var url = getClass().getClassLoader().getResource(folder);
        if (url == null) return new ArrayList<>();

        if (url.getProtocol().equals("jar")) {
            var jarUri = URI.create(url.toString().split("!")[0]);
            try (var fs = FileSystems.newFileSystem(jarUri, Map.of());
                 var stream = Files.list(fs.getPath("/" + folder))) {
                return stream
                        .filter(p -> p.toString().endsWith(".prog"))
                        .sorted()
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        try (var stream = Files.list(Path.of(url.toURI()))) {
            return stream
                    .filter(p -> p.toString().endsWith(".prog"))
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}