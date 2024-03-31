package org.tinyinterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static boolean hadError;

    public static void main(String[] args) throws Exception {
        if (args.length > 1) {
            System.out.println("Usage: lox [example.lox]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    public static void runFile(String filename) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            System.exit(65);
        }
    }
    public static void runPrompt() throws Exception {

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(input);

        for (;;) {
            System.out.print(">");
            String line = bufferedReader.readLine();
            if (line == null) break;
            run(line);

            hadError = false;
        }
    }
    private static void run(String source) throws Exception {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token: tokens) {
            System.out.println(token);
        }
    }
}