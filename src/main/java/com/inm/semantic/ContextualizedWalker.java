package com.inm.semantic;

import com.inm.analyzer.CompilationExecutor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ContextualizedWalker extends ParseTreeWalker {

    private final SemanticAndIntermediateListener listener;

    public ContextualizedWalker(SemanticAndIntermediateListener listener) {
        this.listener = listener;
    }

    public void walk() {
        walk(listener, CompilationExecutor.context.tree());
    }
}
