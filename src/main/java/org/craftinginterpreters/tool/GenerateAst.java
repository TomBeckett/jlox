package org.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GenerateAst {
    /**
     * Generates the abstract syntax tree code.
     * Expects one argument - the full path to 'src/main/java/org/craftinginterpreters/lox'.
     *
     * @param args arguments passed from the commandline.
     * @throws IOException due to issue generating file.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast < output directory");
            System.exit(64);
        }
        final var outputDir = args[0];

        defineAst(outputDir, "Expr", List.of(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));

        defineAst(outputDir, "Stmt", List.of(
                "Expression : Expr expression",
                "Print      : Expr expression"
        ));

        System.out.println("Expr.java generated successfully: " + outputDir + "/Expr.java");
        System.exit(0);
    }

    private static void defineAst(final String outputDir,
                                  final String baseName,
                                  final List<String> types) throws IOException {
        final var path = "%s/%s.java".formatted(outputDir, baseName);
        try (var writer = new PrintWriter(path, StandardCharsets.UTF_8)) {
            writer.println("// This file is autogenerated by src/main/java/org/craftinginterpreters/tool/GenerateAst.java. Do not edit by hand!");
            writer.println("package org.craftinginterpreters.lox;");
            writer.println();
            writer.printf("abstract class %s {%n", baseName);

            defineVisitor(writer, baseName, types);

            types.stream()
                    .map(type -> type.split(":"))
                    .forEach(parts -> {
                        final var className = parts[0].trim();
                        final var fields = parts[1].trim();
                        defineType(writer, baseName, className, fields);
                    });

            // The base accept() method.
            writer.println("    abstract <R> R accept(final Visitor<R> visitor);");

            writer.println("}");
            writer.println();
        }
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        types.stream()
                .map(type -> type.split(":")[0].trim())
                .forEach(typeName ->
                        writer.printf("        R visit%s%s(final %s %s);%n",
                                typeName,
                                baseName,
                                typeName,
                                baseName.toLowerCase(Locale.ROOT)
                        )
                );

        writer.println("    }");
        writer.println();
    }

    private static void defineType(final PrintWriter writer,
                                   final String baseName,
                                   final String className,
                                   final String fieldsList) {
        writer.printf("    static class %s extends %s {%n", className, baseName);

        // Constructor
        writer.printf("        %s(final %s) {%n", className, fieldsList);

        // Store parameters
        final var fields = fieldsList.split(", ");
        Arrays.stream(fields)
                .map(field -> field.split(" ")[1])
                .forEach(name -> writer.printf("            this.%s = %s;%n", name, name));

        writer.println("        }");

        // Visitor pattern.
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(final Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");

        // Fields
        writer.println();
        Arrays.stream(fields).forEach(field -> writer.printf("        final %s;%n", field));

        writer.println("    }");
        writer.println();
    }
}
