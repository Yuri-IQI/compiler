package com.inm.analyzer;

import com.inm.antlr4.ProgramParser;
import com.inm.semantic.SymbolTable;
import com.inm.semantic.ThreeAddressCode;
import org.antlr.v4.runtime.tree.ParseTree;

public class ExecutionContext {

    private String programName;
    private ParseTree tree;
    private ProgramParser parser;
    private SymbolTable symbolTable;
    private ThreeAddressCode threeAddressCode;
    private String finalCode;

    public ExecutionContext() {
        this.symbolTable = new SymbolTable();
        this.threeAddressCode = new ThreeAddressCode();
    }

    public ExecutionContext(String programName, ParseTree tree, ProgramParser parser) {
        this.programName = programName;
        this.tree = tree;
        this.parser = parser;
        this.symbolTable = new SymbolTable();
        this.threeAddressCode = new ThreeAddressCode();
    }

    public String programName() { return programName; }
    public ParseTree tree() { return tree; }
    public ProgramParser parser() { return parser; }
    public SymbolTable symbolTable() { return symbolTable; }
    public ThreeAddressCode threeAddressCode() { return threeAddressCode; }
    public String finalCode() { return finalCode; }

    public void setSymbolTable(SymbolTable symbolTable) { this.symbolTable = symbolTable; }
    public void setThreeAddressCode(ThreeAddressCode tac) { this.threeAddressCode = tac; }
    public void setFinalCode(String finalCode) { this.finalCode = finalCode; }
}