package com.inm.semantic;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, String> table = new HashMap<>();

    // Insere uma variável na tabela e valida se ela já existe
    public void declare(String name, String type, int line, int col) {
        if (table.containsKey(name.toLowerCase())) {
            throw new RuntimeException("Erro Semântico [Linha ] " + line + ":" + col + "]: Variável '" + name + "' já declarada neste escopo.");
        }
        table.put(name.toLowerCase(), type.toUpperCase());
    }

    // Recupera o tipo de uma variável e valida se foi declarada
    public String getType(String name, int line, int col) {
        String type = table.get(name.toLowerCase());
        if (type == null) {
            throw new RuntimeException("Erro Semântico [Linha " + line + ":" + col + "]: Variável '" + name + "' não foi declarada.");
        }
        return type;
    }

    public void clear() {
        table.clear();
    }
}
