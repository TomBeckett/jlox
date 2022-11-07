package org.craftinginterpreters.lox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScannerTest {
    @Test
    void scanTokensShouldHandlePrint() {
        // Given
        final var scanner = new Scanner("print \"test goes here\"");
        final var expectedTokens = List.of(
                new Token(TokenType.PRINT, "print", null, 1),
                new Token(TokenType.STRING, "\"test goes here\"", "test goes here", 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleNil() {
        // Given
        final var scanner = new Scanner("nil");
        final var expectedTokens = List.of(
                new Token(TokenType.NIL, "nil", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleVar() {
        // Given
        final var scanner = new Scanner("var");
        final var expectedTokens = List.of(
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "TRUE"})
    void scanTokensShouldHandleTrueBooleans(final String value) {
        // Given
        final var scanner = new Scanner(value);
        final var expectedTokens = List.of(
                new Token(TokenType.TRUE, value, null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "FALSE"})
    void scanTokensShouldHandleFalseBooleans(final String value) {
        // Given
        final var scanner = new Scanner(value);
        final var expectedTokens = List.of(
                new Token(TokenType.FALSE, value, null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "12.0"})
    void scanTokensShouldHandleNumbers(final String value) {
        // Given
        final var scanner = new Scanner(value);
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, value, 12.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"I am a string\"", "\"\"", "\"123\""})
    void scanTokensShouldHandleStrings(final String value) {
        // Given
        final var scanner = new Scanner(value);
        final var expectedTokens = List.of(
                new Token(TokenType.STRING, value, value.replaceAll("\"",""), 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleAddition() {
        // Given
        final var scanner = new Scanner("123 + 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleSubtraction() {
        // Given
        final var scanner = new Scanner("123 - 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.MINUS, "-", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleMultiplication() {
        // Given
        final var scanner = new Scanner("123 * 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.STAR, "*", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleDivision() {
        // Given
        final var scanner = new Scanner("123 / 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.SLASH, "/", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleLessThan() {
        // Given
        final var scanner = new Scanner("123 < 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleLessEqual() {
        // Given
        final var scanner = new Scanner("123 <= 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.LESS_EQUAL, "<=", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleGreaterThan() {
        // Given
        final var scanner = new Scanner("123 > 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.GREATER, ">", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleGreaterEqual() {
        // Given
        final var scanner = new Scanner("123 >= 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.GREATER_EQUAL, ">=", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleEqualEqual() {
        // Given
        final var scanner = new Scanner("123 == 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.EQUAL_EQUAL, "==", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleNotEqual() {
        // Given
        final var scanner = new Scanner("123 != 456");
        final var expectedTokens = List.of(
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.BANG_EQUAL, "!=", null, 1),
                new Token(TokenType.NUMBER, "456", 456.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleBangOperator() {
        // Given
        final var scanner = new Scanner("!true");
        final var expectedTokens = List.of(
                new Token(TokenType.BANG, "!", null, 1),
                new Token(TokenType.TRUE, "true", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleAndOperator() {
        // Given
        final var scanner = new Scanner("and");
        final var expectedTokens = List.of(
                new Token(TokenType.AND, "and", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleOrOperator() {
        // Given
        final var scanner = new Scanner("or");
        final var expectedTokens = List.of(
                new Token(TokenType.OR, "or", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleIfElseControlFlow() {
        final var lox = """
                if (condition) {
                    print "yes";
                } else {
                    print "no";
                }""";

        final var scanner = new Scanner(lox);
        final var expectedTokens = List.of(
                new Token(TokenType.IF, "if", null, 1),
                new Token(TokenType.LEFT_PAREN, "(", null, 1),
                new Token(TokenType.IDENTIFIER, "condition", null, 1),
                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.PRINT, "print", null, 2),
                new Token(TokenType.STRING, "\"yes\"", "yes", 2),
                new Token(TokenType.SEMICOLON, ";", null, 2),
                new Token(TokenType.RIGHT_BRACE, "}", null, 3),
                new Token(TokenType.ELSE, "else", null, 3),
                new Token(TokenType.LEFT_BRACE, "{", null, 3),
                new Token(TokenType.PRINT, "print", null, 4),
                new Token(TokenType.STRING, "\"no\"", "no", 4),
                new Token(TokenType.SEMICOLON, ";", null, 4),
                new Token(TokenType.RIGHT_BRACE, "}", null, 5),
                new Token(TokenType.EOF, "", null, 5)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleForLoop() {
        final var lox = """
                for (var a = 1; a < 10; a = a + 1) {
                    print a;
                }""";

        final var scanner = new Scanner(lox);
        final var expectedTokens = List.of(
                new Token(TokenType.FOR, "for", null, 1),
                new Token(TokenType.LEFT_PAREN, "(", null, 1),
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.NUMBER, "1", 1.0, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.NUMBER, "10", 10.0, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Token(TokenType.NUMBER, "1", 1.0, 1),
                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.PRINT, "print", null, 2),
                new Token(TokenType.IDENTIFIER, "a", null, 2),
                new Token(TokenType.SEMICOLON, ";", null, 2),
                new Token(TokenType.RIGHT_BRACE, "}", null, 3),
                new Token(TokenType.EOF, "", null, 3)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandlePrecedenceAndGrouping() {
        // Given
        final var scanner = new Scanner("var average = (min + max) / 2");
        final var expectedTokens = List.of(
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.IDENTIFIER, "average", null, 1),
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.LEFT_PAREN, "(", null, 1),
                new Token(TokenType.IDENTIFIER, "min", null, 1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Token(TokenType.IDENTIFIER, "max", null, 1),
                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                new Token(TokenType.SLASH, "/", null, 1),
                new Token(TokenType.NUMBER, "2", 2.0, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleDeclaringFunction() {
        final var lox = """
                fun printSum(a, b) {
                    print a;
                }""";

        final var scanner = new Scanner(lox);
        final var expectedTokens = List.of(
                new Token(TokenType.FUN, "fun", null, 1),
                new Token(TokenType.IDENTIFIER, "printSum", null, 1),
                new Token(TokenType.LEFT_PAREN, "(", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.COMMA, ",", null, 1),
                new Token(TokenType.IDENTIFIER, "b", null, 1),
                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.PRINT, "print", null, 2),
                new Token(TokenType.IDENTIFIER, "a", null, 2),
                new Token(TokenType.SEMICOLON, ";", null, 2),
                new Token(TokenType.RIGHT_BRACE, "}", null, 3),
                new Token(TokenType.EOF, "", null, 3)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleDeclaringClasses() {
        final var scanner = new Scanner("class Breakfast {}");
        final var expectedTokens = List.of(
                new Token(TokenType.CLASS, "class", null, 1),
                new Token(TokenType.IDENTIFIER, "Breakfast", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.RIGHT_BRACE, "}", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleDeclaringClassInheritance() {
        final var lox = """
                class Brunch < Breakfast {
                   drink() {
                        print "How about a Bloody Mary?"
                   }
                }""";

        final var scanner = new Scanner(lox);
        final var expectedTokens = List.of(
                new Token(TokenType.CLASS, "class", null, 1),
                new Token(TokenType.IDENTIFIER, "Brunch", null, 1),
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.IDENTIFIER, "Breakfast", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.IDENTIFIER, "drink", null, 2),
                new Token(TokenType.LEFT_PAREN, "(", null, 2),
                new Token(TokenType.RIGHT_PAREN, ")", null, 2),
                new Token(TokenType.LEFT_BRACE, "{", null, 2),
                new Token(TokenType.PRINT, "print", null, 3),
                new Token(TokenType.STRING, "\"How about a Bloody Mary?\"", "How about a Bloody Mary?", 3),
                new Token(TokenType.RIGHT_BRACE, "}", null, 4),
                new Token(TokenType.RIGHT_BRACE, "}", null, 5),
                new Token(TokenType.EOF, "", null, 5)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleDeclaringClassInit() {
        final var lox = """
                class Brunch < Breakfast {
                   init(meat, bread, drink) {
                        super.init(meat, bread);
                        this.drink = drink;
                   }
                }""";

        final var scanner = new Scanner(lox);
        final var expectedTokens = List.of(
                new Token(TokenType.CLASS, "class", null, 1),
                new Token(TokenType.IDENTIFIER, "Brunch", null, 1),
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.IDENTIFIER, "Breakfast", null, 1),
                new Token(TokenType.LEFT_BRACE, "{", null, 1),
                new Token(TokenType.IDENTIFIER, "init", null, 2),
                new Token(TokenType.LEFT_PAREN, "(", null, 2),
                new Token(TokenType.IDENTIFIER, "meat", null, 2),
                new Token(TokenType.COMMA, ",", null, 2),
                new Token(TokenType.IDENTIFIER, "bread", null, 2),
                new Token(TokenType.COMMA, ",", null, 2),
                new Token(TokenType.IDENTIFIER, "drink", null, 2),
                new Token(TokenType.RIGHT_PAREN, ")", null, 2),
                new Token(TokenType.LEFT_BRACE, "{", null, 2),
                new Token(TokenType.SUPER, "super", null, 3),
                new Token(TokenType.DOT, ".", null, 3),
                new Token(TokenType.IDENTIFIER, "init", null, 3),
                new Token(TokenType.LEFT_PAREN, "(", null, 3),
                new Token(TokenType.IDENTIFIER, "meat", null, 3),
                new Token(TokenType.COMMA, ",", null, 3),
                new Token(TokenType.IDENTIFIER, "bread", null, 3),
                new Token(TokenType.RIGHT_PAREN, ")", null, 3),
                new Token(TokenType.SEMICOLON, ";", null, 3),
                new Token(TokenType.THIS, "this", null, 4),
                new Token(TokenType.DOT, ".", null, 4),
                new Token(TokenType.IDENTIFIER, "drink", null, 4),
                new Token(TokenType.EQUAL, "=", null, 4),
                new Token(TokenType.IDENTIFIER, "drink", null, 4),
                new Token(TokenType.SEMICOLON, ";", null, 4),
                new Token(TokenType.RIGHT_BRACE, "}", null, 5),
                new Token(TokenType.RIGHT_BRACE, "}", null, 6),
                new Token(TokenType.EOF, "", null, 6)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void scanTokensShouldHandleCallingFunction() {
        // Given
        final var scanner = new Scanner("makeBreakfast(bacon, eggs, toast);");
        final var expectedTokens = List.of(
                new Token(TokenType.IDENTIFIER, "makeBreakfast", null, 1),
                new Token(TokenType.LEFT_PAREN, "(", null, 1),
                new Token(TokenType.IDENTIFIER, "bacon", null, 1),
                new Token(TokenType.COMMA, ",", null, 1),
                new Token(TokenType.IDENTIFIER, "eggs", null, 1),
                new Token(TokenType.COMMA, ",", null, 1),
                new Token(TokenType.IDENTIFIER, "toast", null, 1),
                new Token(TokenType.RIGHT_PAREN, ")", null, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "// A commented out line of code",
            "/* A commented out line of code */",
            "/* Draws two lines which divides the window\ninto four quadrants. First draws a horizontal\nline and then the vertical line */",
    })
    void scanTokensShouldHandleCommentedOutCode(final String testValue) {
        // Given
        final var scanner = new Scanner(testValue);
        final var expectedTokens = Collections.singletonList(
                new Token(TokenType.EOF, "", null, 1)
        );

        // When
        final var tokens = scanner.scanTokens();

        // Then
        assertEquals(expectedTokens, tokens);
    }
}