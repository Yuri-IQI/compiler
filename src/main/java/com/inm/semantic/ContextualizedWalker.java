package com.inm.semantic;

import com.inm.analyzer.ExecutionContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ContextualizedWalker extends ParseTreeWalker {

    private final SemanticAndIntermediateListener listener;
    private final ExecutionContext context;

    public ContextualizedWalker(SemanticAndIntermediateListener listener, ExecutionContext context) {
        this.listener = listener;
        this.context = context;
    }

    public void walk() {
        walk(listener, context.tree());
    }
}