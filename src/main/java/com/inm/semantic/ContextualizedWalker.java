package com.inm.semantic;

import com.inm.compilation.CompilationContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ContextualizedWalker extends ParseTreeWalker {

    private final SemanticAndIntermediateListener listener;
    private final CompilationContext context;

    public ContextualizedWalker(SemanticAndIntermediateListener listener, CompilationContext context) {
        this.listener = listener;
        this.context = context;
    }

    public void walk() {
        walk(listener, context.tree());
    }
}