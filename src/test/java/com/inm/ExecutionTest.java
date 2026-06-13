package com.inm;

import com.inm.compilation.CompilationPipeline;
import com.inm.compilation.CompilationContext;
import com.inm.helper.ExecutionEnvHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class ExecutionTest {

    static Stream<Path> asmFiles() throws IOException, URISyntaxException {
        var url = ExecutionTest.class.getClassLoader().getResource("output");
        if (url == null) return Stream.empty();

        Path outputDir = Path.of(url.toURI());
        if (!Files.isDirectory(outputDir)) return Stream.empty();

        try (var stream = Files.walk(outputDir)) {
            return stream
                    .filter(p -> p.toString().endsWith(".asm"))
                    .sorted()
                    .toList()
                    .stream();
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("asmFiles")
    void executesAssembly(Path asmPath) throws Exception {
        assumeTrue(ExecutionEnvHelper.isAvailable("nasm") || ExecutionEnvHelper.isAvailable("docker"),
                "Nenhum ambiente de execução disponível (Instale o NASM nativo ou o Docker)");

        CompilationContext context = new CompilationContext();
        context.setAsmPath(asmPath.toAbsolutePath().toString());

        assertDoesNotThrow(
                () -> new CompilationPipeline().runExecution(context),
                "Falha ao executar: " + asmPath.getFileName()
        );
    }
}