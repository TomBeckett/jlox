package org.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.List;

import static org.craftinginterpreters.lox.TokenType.*;

public class Parser {
    private static class ParserError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    Parser(final List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        final var statements = new ArrayList<Stmt>();
        while (!isAtEnd()) {
            statements.add(statement());
        }
        return statements;
    }

    private Expr expression() {
        return equality();
    }

    private Stmt statement() {
        if (match(PRINT)) return printStatement();
        return expressionStatement();
    }

    private Stmt printStatement() {
        final var value = expression();
        consume(SEMICOLON, "Expect ';' after value");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        final var expr = expression();
        consume(SEMICOLON, "Expect ';' after value");
        return new Stmt.Expression(expr);
    }

    private Expr equality() {
        var expr = comparison();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            final var operator = previous();
            final var right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        var expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            final var operator = previous();
            final var right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        var expr = factor();
        while (match(MINUS, PLUS)) {
            final var operator = previous();
            final var right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        var expr = unary();
        while (match(SLASH, STAR)) {
            final var operator = previous();
            final var right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            final var operator = previous();
            final var right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal());
        }

        if (match(LEFT_PAREN)) {
            final var expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression");
    }

    /**
     * Checks if the current token has any of the given types.
     *
     * @param types The types to check current token against.
     * @return Returns true if there is a match.
     */
    private boolean match(final TokenType... types) {
        for (final TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current token matches the given type.
     * Unlike #match will throw an error if a match is not found.
     *
     * @param type    The expected type of current token.
     * @param message A custom message to be logged.
     * @return The next token if valid.
     */
    private Token consume(final TokenType type, final String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    /**
     * Check if the next token is of the given type.
     *
     * @return Will return true if its match OR we're at the end of the expression.
     */
    private boolean check(final TokenType type) {
        if (isAtEnd()) return true;
        return peek().type() == type;
    }

    /**
     * Consumes the current token and moves to the next token.
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Whether we've run out of tokens to parse.
     */
    private boolean isAtEnd() {
        return peek().type() == EOF;
    }

    /**
     * Returns the current token.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Get previous token.
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * Log the current error to terminal and return a custom error that can either be thrown or ignored.
     *
     * @param token   The token which caused the error.
     * @param message A custom message to be logged.
     * @return A new ParserError.
     */
    private ParserError error(final Token token, final String message) {
        Lox.error(token, message);
        return new ParserError();
    }

    /**
     * Reset the current tree and find next statement.
     */
    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type() == SEMICOLON) return;
            switch (peek().type()) {
                case CLASS, FOR, FUN, IF, PRINT, RETURN, VAR, WHILE -> {
                    return;
                }
                default -> advance();
            }
        }
    }
}
