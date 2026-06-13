package com.inm.terminal;

public record ExecutionParams(
        ExecutionMode mode,
        boolean showTree,
        String flag,
        boolean execute,
        String output,
        String filePath
) {
    public static ExecutionParams buildParams() {
        String modeStr = System.getProperty("mode", "SCRIPT").toUpperCase();
        ExecutionMode mode = ExecutionMode.valueOf(modeStr);
        boolean showTree = Boolean.parseBoolean(System.getProperty("showTree", "false"));
        String flag = System.getProperty("flag", "#");
        boolean execute = Boolean.parseBoolean(System.getProperty("exec", "false"));
        String output = System.getProperty("output", "output");
        String filePath = System.getProperty("filePath", "./");

        return new ExecutionParams(mode, showTree, flag, execute, output, filePath);
    }
}