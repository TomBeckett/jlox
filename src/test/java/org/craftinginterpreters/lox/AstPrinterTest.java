package org.craftinginterpreters.lox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AstPrinterTest {
    @Test
    @DisplayName("Print returns correct syntax tree")
    void printReturnsCorrectSyntaxTree() {
        // Given
        final var printer = new AstPrinter();
        final var expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)
                )
        );

        // When
        final var tree = printer.print(expression);

        // Then
        assertEquals("(* (- 123) (group 45.67))", tree);
    }
}
