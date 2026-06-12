package com.inm;

import com.inm.analyzer.ProgramAnalyzer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            System.setProperty(
                    arg.split("=")[0].replace("-D", ""),
                    arg.split("=")[1]
            );
        }

        boolean shouldReadFile = Boolean.parseBoolean(System.getProperty("readFile", "true"));
        boolean showTree = Boolean.parseBoolean(System.getProperty("showTree", "false"));
        String flag = System.getProperty("flag", "#");

        new ProgramAnalyzer().run(
            shouldReadFile,
            showTree,
            flag
        );
    }
}