package org.craftinginterpreters.lox;

/**
 * Will 'pretty print' a given parsed syntax tree to the console.
 */
public class AstPrinter implements Expr.Visitor<String> {
    String print(final Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(final Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(final Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(final Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(final Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme(), expr.right);
    }

    private String parenthesize(final String name, final Expr... exprs) {
        final var builder = new StringBuilder();

        builder.append("(").append(name);
        for (final Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
