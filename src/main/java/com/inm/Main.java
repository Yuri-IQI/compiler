package com.inm;

import com.inm.analyzer.ProgramAnalyzer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean shouldReadFile = Boolean.parseBoolean(System.getProperty("readFile"));
        boolean showTree = Boolean.parseBoolean(System.getProperty("showTree"));
        String flag = System.getProperty("flag");

        new ProgramAnalyzer().run(
            shouldReadFile,
            showTree,
            flag
        );
    }
}