package org.craftinginterpreters.lox;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IntegrationTest {
    private static final ByteArrayOutputStream SYSTEM_OUT = new ByteArrayOutputStream();

    @BeforeAll
    static void setup() {
        System.setOut(new java.io.PrintStream(SYSTEM_OUT));
    }

    @Test
    void canParseStatementsFile() throws IOException {
        // Given
        final var resource = getClass().getClassLoader().getResource("statements.lox");
        assertNotNull(resource);
        final var filePath = new File(resource.getFile()).getAbsoluteFile();
        final var args = new String[]{filePath.toString()};

        final var expected = """
                one
                true
                3
                """;

        // When
        Lox.main(args);

        // Then
        assertEquals(expected, SYSTEM_OUT.toString());
    }
}
