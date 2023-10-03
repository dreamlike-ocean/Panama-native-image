package org.example;

import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyDemo {

    private static final ScopedValue<String> level = ScopedValue.newInstance();


    public static void main(String[] args) {
        ScopedValue.runWhere(level, "1", () -> {
            forkAndPrintLevel();
            ScopedValue.runWhere(level, "2", () -> {
                forkAndPrintLevel();
            });
            forkAndPrintLevel();
        });
    }


    public static void forkAndPrintLevel() {
        try (StructuredTaskScope<Void> taskScope = new StructuredTaskScope<Void>()) {
            taskScope.fork(() -> {
                System.out.println(STR."Thread: \{Thread.currentThread()} ; level: \{level.get()}");
                return null;
            });
            taskScope.fork(() -> {
                System.out.println(STR."Thread: \{Thread.currentThread()} ; level: \{level.get()}");
                return null;
            });
            taskScope.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
