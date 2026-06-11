package com.inm.semantic;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCode {
    private final List<String> instructions = new ArrayList<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    // Gera o nome de um novo temporário abstrato
    public String newTemp() {
        return "t" + (tempCounter++);
    }

    // Gera um novo Label de desvio estruturado
    public String newLabel() {
        return "L" + (labelCounter++);
    }

    // Adiciona uma linha de instrução 3AC
    public void emit(String instruction) {
        instructions.add(instruction);
    }

    // Retorna todo o código intermediário gerado como String
    public String getCode() {
        return String.join("\n", instructions);
    }
}
