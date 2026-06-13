package com.inm.helper;

import com.inm.analyzer.CompilationExecutor;

public class Printer {

    public static void printInstructions() {
        CompilationExecutor.context.threeAddressCode()
                .getInstructions().forEach(System.out::println);
    }
}
