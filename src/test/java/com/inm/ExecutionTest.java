package com.inm;

import com.inm.compilation.CompilationPipeline;
import com.inm.compilation.CompilationContext;
import com.inm.compilation.Executor;
import com.inm.helper.ExecutionEnvHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class ExecutionTest {

    static Stream<Path> asmFiles() throws IOException, URISyntaxException {
        var url = ExecutionTest.class.getClassLoader().getResource("output");
        if (url == null) {
            System.err.println("Caminho 'output' não mapeado no ClassLoader.");
            return Stream.empty();
        }

        Path outputDir = Path.of(url.toURI());
        if (!Files.isDirectory(outputDir)) {
            return Stream.empty();
        }

        List<Path> filesFound;
        try (Stream<Path> stream = Files.walk(outputDir)) {
            filesFound = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".asm"))
                    .sorted()
                    .toList();
        }

        if (filesFound.isEmpty()) {
            System.err.println("Pasta encontrada em '" + outputDir.toAbsolutePath() + "', mas não contém arquivos .asm");
        }

        return filesFound.stream();
    }

    @ParameterizedTest()
    @MethodSource("asmFiles")
    void executesAssembly(Path asmPath) throws Exception {
        File masmDir = new File("C:\\masm32\\bin\\ml.exe");
        assumeTrue(masmDir.exists(), "MASM32 SDK não encontrado em C:\\masm32");

        CompilationContext context = new CompilationContext();
        context.setAsmPath(asmPath.toAbsolutePath().toString());

        String filename = asmPath.getFileName().toString();
        int dot = filename.lastIndexOf('.');
        String progName = dot > 0 ? filename.substring(0, dot) : filename;
        context.setProgramName(progName);

        assertDoesNotThrow(
                () -> Executor.runExecution(context),
                "Falha ao executar: " + asmPath.getFileName()
        );
    }
}