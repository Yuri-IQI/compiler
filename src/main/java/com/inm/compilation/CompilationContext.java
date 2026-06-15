package com.inm.compilation;

import com.inm.antlr4.ProgramParser;
import com.inm.semantic.SymbolTable;
import com.inm.generator.ThreeAddressCode;
import com.inm.terminal.ExecutionParams;
import org.antlr.v4.runtime.tree.ParseTree;

public class CompilationContext {
    private String programName;
    private ParseTree tree;
    private ProgramParser parser;
    private SymbolTable symbolTable;
    private ThreeAddressCode threeAddressCode;
    private String asmPath;
    private ExecutionParams executionParams;
    private String stdInContent = "";

    public CompilationContext() {
        this.symbolTable = new SymbolTable();
        this.threeAddressCode = new ThreeAddressCode();
    }

    public CompilationContext(String programName, ParseTree tree, ProgramParser parser, ExecutionParams executionParams) {
        this.programName = programName;
        this.tree = tree;
        this.parser = parser;
        this.symbolTable = new SymbolTable();
        this.threeAddressCode = new ThreeAddressCode();
        this.executionParams = executionParams;
    }

    public String programName() { return programName; }
    public ParseTree tree() { return tree; }
    public ProgramParser parser() { return parser; }
    public SymbolTable symbolTable() { return symbolTable; }
    public ThreeAddressCode threeAddressCode() { return threeAddressCode; }
    public String asmPath() { return asmPath; }
    public ExecutionParams executionParams() { return executionParams; }
    public String stdInContent() { return stdInContent; }

    public void setSymbolTable(SymbolTable symbolTable) { this.symbolTable = symbolTable; }
    public void setThreeAddressCode(ThreeAddressCode tac) { this.threeAddressCode = tac; }
    public void setAsmPath(String asmPath) { this.asmPath = asmPath; }
    public void setProgramName(String progName) {programName = progName;}
    public void setStdInContent(String content) {stdInContent = content;}
}