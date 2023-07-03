package org.craftinginterpreters.lox;

/**
 * Represents an error that has occurred during runtime.
 * We throw a custom exception that can be handled using our own logic.
 */
public class RuntimeError extends RuntimeException{
    final Token token;

    RuntimeError(final Token token, final String message) {
        super(message);
        this.token = token;
    }
}
