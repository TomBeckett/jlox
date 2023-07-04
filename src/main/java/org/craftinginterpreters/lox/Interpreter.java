package org.craftinginterpreters.lox;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    /**
     * Takes a syntax tree for a list of statements to be evaluated and printed to console.
     *
     * @param statements The statements to evaluate.
     */
    void interpret(final List<Stmt> statements) {
        try {
            for (final var statement : statements) {
                execute(statement);
            }
        } catch (final RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    /**
     * Interpret a infix arithmetic (+, -, *, /) or logic operator (==, !=, <, <=, >, >=).
     *
     * @param expr The given expression.
     * @return The interpreted expression.
     */
    @Override
    public Object visitBinaryExpr(final Expr.Binary expr) {
        final var left = evaluate(expr.left);
        final var right = evaluate(expr.right);

        return switch (expr.operator.type()) {
            case MINUS -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left - (double) right;
            }
            case SLASH -> {
                if (right instanceof Double && (double) right == 0) {
                    throw new RuntimeError(expr.operator, "Cannot divide by zero.");
                }
                yield (double) left / (double) right;
            }
            case STAR -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left * (double) right;
            }
            case PLUS -> {
                if (left instanceof Double && right instanceof Double) {
                    // Number addition.
                    yield (double) left + (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    // String concat.
                    yield left + (String) right;
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
            }
            case GREATER -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left >= (double) right;
            }
            case LESS -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left < (double) right;
            }
            case LESS_EQUAL -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left <= (double) right;
            }
            case BANG_EQUAL -> !isEqual(left, right);
            case EQUAL_EQUAL -> isEqual(left, right);
            default -> null; // Unreachable code.
        };
    }

    /**
     * Interpret a grouped expression - a node which uses explicit parenthesis.
     *
     * @param expr The given expression.
     * @return The interpreted expression.
     */
    @Override
    public Object visitGroupingExpr(final Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    /**
     * Interpret a literal value (numbers, booleans, strings, and nil).
     *
     * @param expr The given expression.
     * @return The interpreted expression.
     */
    @Override
    public Object visitLiteralExpr(final Expr.Literal expr) {
        return expr.value;
    }

    /**
     * Interprets a unary (- or !) expression.
     *
     * @param expr The given expression.
     * @return The interpreted expression.
     */
    @Override
    public Object visitUnaryExpr(final Expr.Unary expr) {
        final var right = evaluate(expr.right);
        return switch (expr.operator.type()) {
            case BANG -> !isTruthy(right);
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                yield -(double) right;
            }
            default -> null; // Unreachable code.
        };
    }

    /**
     * Evaluate the statement's inner expression.
     *
     * @param stmt The statement.
     * @return Nothing. Not required.
     */
    @Override
    public Void visitExpressionStmt(final Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    /**
     * Print a statement to console.
     *
     * @param stmt The statement.
     * @return Nothing. Not required.
     */
    @Override
    public Void visitPrintStmt(final Stmt.Print stmt) {
        final var value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    /**
     * Execute the statement via visitor pattern.
     *
     * @param stmt The statement to execute.
     */
    private void execute(final Stmt stmt) {
        stmt.accept(this);
    }

    /**
     * Recursively evaluate a grouped expression via Visitor pattern.
     *
     * @param expr The grouped expression/node.
     * @return The evaluated expression.
     */
    private Object evaluate(final Expr expr) {
        return expr.accept(this);
    }

    /**
     * Will convert an expression into a boolean.
     * Follows Ruby's simple rule: false and nil are falsey, otherwise is truthy.
     *
     * @param object The dynamic expression.
     * @return Whether the expression is truthy or falsey.
     */
    private boolean isTruthy(final Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    /**
     * Will compare two objects.
     * Note: Lox's equality is slightly different to Java.
     *
     * @param a The first parameter.
     * @param b The second parameter.
     * @return Whether first parameter equals second parameter.
     */
    private boolean isEqual(final Object a, final Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return (a.equals(b));
    }

    /**
     * Checks if the given operand is a number.
     *
     * @param operator The operator.
     * @param operand  The operand.
     */
    private void checkNumberOperand(final Token operator, final Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    /**
     * Checks if the given operands (left and right) are a number.
     *
     * @param operator The operator.
     * @param left     The first operand.
     * @param right    The second operand.
     */
    private void checkNumberOperand(final Token operator, final Object left, final Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be a numbers.");
    }

    /**
     * Will stringify the given Lox object.
     *
     * @param object The expression.
     * @return A string to be printed to console.
     */
    private String stringify(final Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            var text = object.toString();
            if (text.endsWith(".0")) {
                // Remove redundant .0 from numbers.
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }
}
