package com.inm;

import com.inm.compilation.CompilationPipeline;
import com.inm.terminal.ExecutionMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PipelineTest {

    public static final String OUTPUT_DIR = resolveOutputDir();

    private static String resolveOutputDir() {
        try {
            var url = PipelineTest.class.getClassLoader().getResource(".");
            if (url == null) return "target/test-output";

            Path targetResources = Path.of(url.toURI());
            Path outputInTarget = targetResources.resolve("output");
            Files.createDirectories(outputInTarget);
            Path projectRoot = targetResources.getParent().getParent();
            Path outputInSrc = projectRoot.resolve("src/test/resources/output");
            Files.createDirectories(outputInSrc);
            return outputInSrc.toString();

        } catch (Exception e) {
            return "target/test-output";
        }
    }

    private String load(String path) throws IOException, URISyntaxException {
        var url = getClass().getClassLoader().getResource(path);
        System.out.println(url);
        assertNotNull(url, "Arquivo não encontrado: " + path);
        return Files.readString(Path.of(url.toURI()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "scripts/valid/atribuicao.prog",
            "scripts/valid/comentario-e-div.prog",
            "scripts/valid/dangling-else.prog",
            "scripts/valid/escreve-teste.prog",
            "scripts/valid/expr-ari.prog",
            "scripts/valid/nome-identificador.prog",
            "scripts/valid/variaveis.prog",
            "scripts/valid/while-script.prog",
            "scripts/valid/condicional.prog",
            "scripts/valid/if-aninhado.prog",
            "scripts/valid/projeto-compiladores.prog",
            "scripts/valid/calculo-desconto.prog",
            "scripts/valid/teste-while.prog",
            "scripts/valid/super-expr.prog",
    })
    void acceptsValidScripts(String path) throws Exception {
        assertDoesNotThrow(
                () -> new CompilationPipeline().compile(load(path), ExecutionMode.TEST, false, OUTPUT_DIR),
                "Script deveria ser válido, mas ocorreram erros em " + path
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "scripts/invalid/attr-sem-expr.prog",
            "scripts/invalid/if-sem-then.prog",
            "scripts/invalid/read-sem-pars.prog",
            "scripts/invalid/sem-begin.prog",
            "scripts/invalid/sem-ponto.prog",
            "scripts/invalid/sem-pvig.prog",
            "scripts/invalid/tipo-invalido.prog",
            "scripts/invalid/var-sem-tipo.prog",
            "scripts/invalid/while-sem-do.prog",
            "scripts/invalid/id-reservado.prog",
            "scripts/invalid/erro-duplo-semantico.prog",
            "scripts/invalid/attr-wrong-type.prog",
            "scripts/invalid/ops-diferentes-tipos.prog",
            "scripts/invalid/if-not-boolean.prog",
            "scripts/invalid/tamanho-constante.prog",
            "scripts/invalid/ops-bool-int.prog",
    })
    void rejectsInvalidScripts(String path) throws Exception {
        assertThrows(
                Exception.class,
                () -> new CompilationPipeline().compile(load(path), ExecutionMode.TEST, false, OUTPUT_DIR),
                "Script deveria lançar exceção, mas foi aceito: " + path
        );
    }

    @Test
    void rejectsEmptyScript() {
        assertThrows(
                Exception.class,
                () -> new CompilationPipeline().compile("", ExecutionMode.SCRIPT, false, OUTPUT_DIR),
                "Script vázio deveria lançar exceção, mas foi aceito"
        );
    }
}