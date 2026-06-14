package com.inm;

import com.inm.terminal.ExecutionParams;
import com.inm.terminal.TerminalHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args != null) {
            for (String arg : args) {
                if (arg.contains("=")) {
                    String[] parts = arg.split("=", 2);
                    String key = parts[0].replace("-D", "").replace("-", "").trim();
                    String value = parts[1].trim();
                    System.setProperty(key, value);
                }
            }
        }

        new TerminalHandler()
                .run(ExecutionParams.buildParams());
    }
}