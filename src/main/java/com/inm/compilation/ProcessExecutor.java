package com.inm.compilation;

import java.io.IOException;
import java.nio.file.Path;

import static com.inm.compilation.ProcessRunner.run;

public class ProcessExecutor {
    private static final String MASM32_DIR = "C:\\masm32";
    private static final String MASM_EXECUTABLE = MASM32_DIR + "\\bin\\ml.exe";
    private static final String LINK_EXECUTABLE = MASM32_DIR + "\\bin\\link.exe";
    private static final String WIN32_KERNEL_LIB = "kernel32.lib";

    public static int mount(Path asmFile, Path objFile) throws IOException, InterruptedException {
        return run(
                MASM_EXECUTABLE,
                "/c", "/coff", "/W3", "/Zi",
                "/I" + MASM32_DIR + "\\include",
                "/Fo", objFile.toAbsolutePath().toString(),
                asmFile.toAbsolutePath().toString()
        );
    }

    public static int link(Path objFile, Path exeFile) throws IOException, InterruptedException {
        return run(
                LINK_EXECUTABLE, "/SUBSYSTEM:CONSOLE", "/MACHINE:X86",
                "/LIBPATH:" + MASM32_DIR + "\\lib",
                "/OUT:" + exeFile.toAbsolutePath(),
                objFile.toAbsolutePath().toString(),
                WIN32_KERNEL_LIB
        );
    }

    public static int execute(Path exeFile) throws IOException, InterruptedException {
        return ProcessRunner.runInheritIO(exeFile.toAbsolutePath().toString());
    }
}