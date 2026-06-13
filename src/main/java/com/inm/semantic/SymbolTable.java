package com.inm.semantic;

import com.inm.helper.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    private final Map<String, Symbol> table = new HashMap<>();
    private final SymbolTable parent;
    private int currentOffset;

    private final SymbolTable root;
    public static final String prefix = "v_";

    public SymbolTable() {
        this.parent = null;
        this.currentOffset = 0;
        this.root = this;
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.currentOffset = parent.currentOffset;
        this.root = parent.root;
    }

    public static String getPrefixedName(String varName) {
        if (varName.startsWith(prefix)) return varName;
        return prefix + varName.toLowerCase();
    }

    public void declare(String name, String type, int line, int col) {
        String key = name.toLowerCase();
        if (table.containsKey(key)) {
            throw new RuntimeException(
                    "Erro Semântico [Linha " + line + ":" + col + "]: Variável '" + name + "' já declarada neste escopo."
            );
        }
        Symbol s = new Symbol(getPrefixedName(name), type, currentOffset);
        currentOffset += s.size();

        if (parent != null) {
            parent.currentOffset = currentOffset;
        }

        table.put(key, s);
        root.table.put(key, s);
    }

    public String getType(String name, int line, int col) {
        String key = name.toLowerCase();
        if (table.containsKey(key))
            return table.get(key).type();
        if (parent != null)
            return parent.getType(name, line, col);
        throw new RuntimeException(
                "Erro Semântico [Linha " + line + ":" + col + "]: Variável '" + name + "' não foi declarada."
        );
    }

    public SymbolTable getParent() { return parent; }

    public List<Symbol> getAllSymbols() {
        return new ArrayList<>(table.values());
    }
}