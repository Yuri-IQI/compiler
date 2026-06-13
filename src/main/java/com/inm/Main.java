package com.inm;

import com.inm.terminal.ExecutionParams;
import com.inm.terminal.TerminalHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            for (String arg : args) {
                System.setProperty(
                        arg.split("=")[0].replace("-D", ""),
                        arg.split("=")[1]
                );
            }
        }

        new TerminalHandler()
                .run(ExecutionParams.buildParams());
    }
}