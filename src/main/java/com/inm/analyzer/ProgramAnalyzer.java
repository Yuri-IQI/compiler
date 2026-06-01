package com.inm.analyzer;

import com.inm.helper.ParseHelper;
import org.antlr.v4.gui.Trees;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgramAnalyzer {

    public void run(boolean shouldReadFile, boolean showTree) throws IOException {
        String source;

        try (Scanner scanner = new Scanner(System.in)) {
            source = shouldReadFile
                    ? readFile(scanner) : readScript(scanner);
        }

        analyze(source, showTree);
    }

    private static String readScript(Scanner scanner) {
        System.out.println("Escreva o script (digite . em nova linha para analisar):");

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("@")) break;
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private static Map<String, List<Path>> mapFiles() throws IOException {
        Map<String, List<Path>> fileMap = new LinkedHashMap<>();
        fileMap.put("Válidos", listResourceFiles("scripts/valid"));
        fileMap.put("Inválidos", listResourceFiles("scripts/invalid"));

        boolean empty = fileMap.values().stream().allMatch(List::isEmpty);
        if (empty) throw new IOException("Nenhum arquivo encontrado em resources/scripts");

        System.out.println("Arquivos disponíveis:");
        fileMap.forEach((key, paths) -> {
            System.out.println(key);
            for (int i = 0; i < paths.size(); i++) {
                System.out.printf("  [%c%d] %s%n",
                        key.charAt(0), i + 1, paths.get(i).getFileName());
            }
        });

        return fileMap;
    }

    private static String readFile(Scanner scanner) throws IOException {
        Map<String, List<Path>> fileMap = mapFiles();

        System.out.print("\nEscolha um código (ex: V1, I2) ou digite o caminho (ex: scripts/valid/expr-ari.prog): ");
        String input = scanner.nextLine().trim();

        try {
            char prefix = Character.toUpperCase(input.charAt(0));
            int index = Integer.parseInt(input.substring(1)) - 1;

            String key = prefix == 'V' ? "Válidos"
                    : prefix == 'I' ? "Inválidos"
                      : null;

            if (key == null) {
                throw new IOException("Prefixo inválido: " + prefix + " (use V ou I)");
            }

            List<Path> paths = fileMap.get(key);
            if (index < 0 || index >= paths.size()) {
                throw new IOException("Índice fora do intervalo: " + (index + 1));
            }

            String folder = key.equals("Válidos") ? "scripts/valid/" : "scripts/invalid/";
            String resourcePath = folder + paths.get(index).getFileName();

            try (var is = ProgramAnalyzer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (is == null) throw new IOException("Recurso não encontrado: " + resourcePath);
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (NumberFormatException e) {
            try (var is = ProgramAnalyzer.class.getClassLoader().getResourceAsStream(input)) {
                if (is != null) return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            try {
                return Files.readString(Path.of(input), StandardCharsets.UTF_8);
            } catch (NoSuchFileException ex) {
                throw new IOException("Arquivo não encontrado: " + input, ex);
            }
        }
    }

    private static List<Path> listResourceFiles(String folder) throws IOException {
        var url = ProgramAnalyzer.class.getClassLoader().getResource(folder);
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

    public static void analyze(String source, boolean showTree) {
        var result = ParseHelper.parse(source);

        System.out.println("Programa: " + result.programName());

        if (showTree) Trees.inspect(
            result.tree(),
            result.parser()
        );
    }
}