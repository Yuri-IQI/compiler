package com.inm.helper;

public class ExecutionEnvHelper {
    public static final String PROJECT_IMAGE = "compiler-test";

    public static boolean isAvailable(String executor) {
        try {
            return new ProcessBuilder(executor, "--version")
                    .start()
                    .waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDockerImageAvailable() {
        try {
            int exit = new ProcessBuilder(
                    "docker", "image", "inspect", PROJECT_IMAGE
            ).start().waitFor();
            return exit == 0;
        } catch (Exception e) {
            return false;
        }
    }
}