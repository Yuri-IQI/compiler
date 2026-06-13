package com.inm.helper;

import com.inm.compilation.CompilationContext;

public class Printer {

    public static void printInstructions(CompilationContext context) {
        System.out.println("\n--- Instruções Otimizadas (3AC) ---");
        context.threeAddressCode()
                .getInstructions()
                .forEach(System.out::println);
        System.out.println("-----------------------------------");
    }
}