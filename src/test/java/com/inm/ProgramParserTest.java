package com.inm;

import com.inm.analyzer.CompilationExecutor;
import com.inm.antlr4.ProgramParser;
import com.inm.helper.ParseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramParserTest {

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
        "scripts/valid/projeto-compiladores.prog"
    })
    void acceptsValidScripts(String path) throws Exception {
        assertDoesNotThrow(
                () -> CompilationExecutor.compile(load(path)),
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
        "scripts/invalid/id-reservado.prog"
    })
    void rejectsInvalidScripts(String path) throws Exception {
        assertThrows(
                Exception.class,
                () -> CompilationExecutor.compile(load(path)),
                "Script deveria lançar exceção, mas foi aceito: " + path
        );
    }

    @Test
    void rejectsEmptyScript() {
        assertThrows(
                Exception.class,
                () -> CompilationExecutor.compile(""),
                "Script vázio deveria lançar exceção, mas foi aceito"
        );
    }

    /*
    Testes de casos sendo trabalhados
    TODO: colocar o script em valid ou invalid quando ele for devidamente aceito ou rejeitado
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "scripts/working-set/attr-wrong-type.prog",
            "scripts/working-set/ops-diferentes-tipos.prog",
            "scripts/working-set/if-not-boolean.prog",
            "scripts/working-set/tamanho-constante.prog",
            "scripts/working-set/ops-bool-int.prog",
    })
    void evaluateWorkingSet(String path) throws Exception {
        assertDoesNotThrow(
                () -> CompilationExecutor.compile(load(path)),
                "Script deveria ser válido, mas ocorreram erros em " + path
        );
    }
}