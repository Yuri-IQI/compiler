package com.inm.analyzer;

import com.inm.antlr4.ProgramParser;
import com.inm.semantic.SymbolTable;
import com.inm.semantic.ThreeAddressCode;
import org.antlr.v4.runtime.tree.ParseTree;

public record ExecutionContext(
        String programName,
        ParseTree tree,
        ProgramParser parser,
        SymbolTable symbolTable,
        ThreeAddressCode threeAddressCode
) {
    public ExecutionContext() {
        this(null, null, null, new SymbolTable(), new ThreeAddressCode());
    }

    public ExecutionContext(String programName, ParseTree tree, ProgramParser parser) {
        this(programName, tree, parser, new SymbolTable(), new ThreeAddressCode());
    }
}