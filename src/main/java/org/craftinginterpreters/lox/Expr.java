// This file is autogenerated by src/main/java/org/craftinginterpreters/tool/GenerateAst.java. Do not edit by hand!
package org.craftinginterpreters.lox;

abstract class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(final Binary expr);
        R visitGroupingExpr(final Grouping expr);
        R visitLiteralExpr(final Literal expr);
        R visitUnaryExpr(final Unary expr);
    }

    static class Binary extends Expr {
        Binary(final Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Grouping extends Expr {
        Grouping(final Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expr expression;
    }

    static class Literal extends Expr {
        Literal(final Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;
    }

    static class Unary extends Expr {
        Unary(final Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(final Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expr right;
    }

    abstract <R> R accept(final Visitor<R> visitor);
}

