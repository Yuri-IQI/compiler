package com.inm.helper;

import com.inm.analyzer.ExecutionContext;

public class Printer {

    public static void printInstructions(ExecutionContext context) {
        System.out.println("\n--- Instruções Otimizadas (3AC) ---");
        context.threeAddressCode()
                .getInstructions()
                .forEach(System.out::println);
        System.out.println("-----------------------------------");
    }
}