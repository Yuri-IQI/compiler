package com.inm.compilation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessRunner {
    public static int run(String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(false)
                .start();

        Thread stdoutReader = pipeAsync(process, false);
        Thread stderrReader = pipeAsync(process, true);
        stdoutReader.start();
        stderrReader.start();

        int exitCode = process.waitFor();

        stdoutReader.join(5000);
        stderrReader.join(5000);
        return exitCode;
    }
    
    public static int runWithInput(String stdInContent, String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(false)
                .start();

        Thread stdoutReader = pipeAsync(process, false);
        Thread stderrReader = pipeAsync(process, true);
        stdoutReader.start();
        stderrReader.start();

        if (stdInContent != null && !stdInContent.isEmpty()) {
            try (var writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(process.getOutputStream()))) {
                writer.write(stdInContent);
                writer.flush();
            }
        }

        boolean terminado = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
        if (!terminado) {
            process.destroyForcibly();
            throw new IOException("Timeout: O programa travou aguardando entradas (Stdin) ou entrou em loop.");
        }

        int exitCode = process.exitValue();

        stdoutReader.join(1000);
        stderrReader.join(1000);
        return exitCode;
    }

    public static int runInheritIO(String... command) throws IOException, InterruptedException {
        return new ProcessBuilder(command)
                .inheritIO()
                .start()
                .waitFor();
    }

    private static Thread pipeAsync(Process process, boolean isError) {
        return new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    isError ? process.getErrorStream() : process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (isError) System.err.println(line);
                    else         System.out.println(line);
                }
            } catch (IOException ignored) {}
        });
    }
}