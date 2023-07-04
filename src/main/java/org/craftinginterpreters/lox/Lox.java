package org.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * Run using the provided file.
     *
     * @param path The full path to the file.
     * @throws IOException Error reading file.
     */
    private static void runFile(String path) throws IOException {
        final var bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate any errors in the exit code.
        if (hadError) {
            System.exit(65);
        }
        if (hadRuntimeError) {
            System.exit(70);
        }
    }

    /**
     * Run in interactive mode.
     *
     * @throws IOException Error reading input from terminal.
     */
    private static void runPrompt() throws IOException {
        final var input = new InputStreamReader(System.in);
        final var reader = new BufferedReader(input);

        for (; ; ) {
            System.out.println("> ");
            final var line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(final String source) {
        final var scanner = new Scanner(source);
        final var tokens = scanner.scanTokens();

        final var parser = new Parser(tokens);
        final var statements = parser.parse();

        // Stop if there was an error
        if (hadError) return;

        interpreter.interpret(statements);
    }

    /**
     * Print an error message to the console. Used during scanning.
     *
     * @param line    The current line of the file being scanned.
     * @param message A message hint on what went wrong.
     */
    static void error(final int line, final String message) {
        report(line, "", message);
    }

    /**
     * Print to console.
     *
     * @param line    Line of error.
     * @param where   Location of error.
     * @param message User friendly message.
     */
    private static void report(final int line, final String where, final String message) {
        System.err.printf("[line %d] Error %s: %s%n", line, where, message);
    }

    /**
     * Print an error message to the console. Used during parsing.
     *
     * @param token   The current token that caused the error.
     * @param message A message hint on what went wrong.
     */
    static void error(final Token token, final String message) {
        if (token.type() == TokenType.EOF) {
            report(token.line(), " at end", message);
        } else {
            report(token.line(), " at '%s'".formatted(token.lexeme()), message);
        }
    }

    /**
     * Print an error to console. Used during runtime interpreting.
     *
     * @param error The runtime error.
     */
    static void runtimeError(final RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line() + "]");
        hadRuntimeError = true;
    }
}
