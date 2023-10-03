package org.example;

import java.util.UUID;

public class ScopeValueDemo {
    public static final ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.runWhere(SCOPED_VALUE, UUID.randomUUID().toString(), () -> {
            System.out.println(SCOPED_VALUE.get());
            ScopedValue.runWhere(SCOPED_VALUE, UUID.randomUUID().toString(), () -> {
                System.out.println(SCOPED_VALUE.get());
            });
            System.out.println(SCOPED_VALUE.get());
        });

    }
}
