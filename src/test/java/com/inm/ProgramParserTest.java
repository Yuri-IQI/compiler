package com.inm;

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
        "scripts/valid/if-aninhado.prog"
    })
    void acceptsValidScripts(String path) throws Exception {
        var result = ParseHelper.parse(load(path));
        assertTrue(
            result.isValid(),
        "Script deveria ser válido, mas ocorreram erros em "
                + path
                + ": "
                + result.errors()
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
        var result = ParseHelper.parse(load(path));

        assertFalse(
            result.isValid(),
            "Script deveria ser inválido, mas foi aceito: " + path
        );
    }

    @Test
    void rejectsEmptyScript() {
        var result = ParseHelper.parse("");
        assertFalse(result.isValid());
    }
}