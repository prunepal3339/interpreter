package org.tinyinterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.tinyinterpreter.TokenType.*;

public class Scanner {

    private final String source;
    private final List<Token> tokens;

    private int current = 0, line = 1, start = 0;


    private static final HashMap<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("not", NOT);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("true", TRUE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);

        keywords.put("nil", NIL);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);

        keywords.put("class", CLASS);

    }

    Scanner(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
    }
    public List<Token> scanTokens() throws Exception {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }
    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() throws Exception {
        char c = advance();
        addToken(
            switch(c) {
                case '(' -> LEFT_PAREN;
                case ')' -> RIGHT_PAREN;
                case '{' -> LEFT_BRACE;
                case '}' -> RIGHT_BRACE;
                case ',' -> COMMA;
                case '.' -> DOT;
                case '-' -> MINUS;
                case '+' -> PLUS;
                case ';' -> SEMICOLON;
                case '*' -> STAR;


                //one or more character situation

                case '!' -> match('=') ? BANG_EQUAL : BANG;
                case '=' -> match('=') ? EQUAL_EQUAL : EQUAL;
                case '<' -> match('=') ? LESS_EQUAL : EQUAL;
                case '>' -> match('=') ? GREATER_EQUAL : GREATER;


                //handling single-line comment
                case '/' -> {
                    if (match('/')) {
                        while(peek() != '\n' && !isAtEnd()) advance();
                        start += 2; //➔ ignore the // from comment
                        yield COMMENT;
                    }else {
                        yield SLASH;
                    }
                }
                case ' ', '\r', '\t' -> WHITESPACE;
                case '\n' -> {
                    line++;
                    yield NEWLINE;
                }
                //handling string literals
                case '"' -> {
                    extractString();
                    yield STRING;
                }
                default -> {
                    if (isDigit(c)) {
                        extractNumber();
                        yield NUMBER;
                    } else if(isAlpha(c)) {
                        extractIdentifier();
                        yield IDENTIFIER;
                    }
                    throw new Exception("Invalid character encountered: " + c);
                }
            }
        );
    }

    private void extractIdentifier() {
        while(isAlphanumeric(peek())) advance();
    }
    private void extractString() {
        int stringStartedAtLine = line;
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            ErrorHandler.error("Unterminated string quote", stringStartedAtLine);
        }
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
    private boolean isAlpha(char ch) {
        return (ch >= 'A' && ch <= 'Z' ) || (ch >= 'a' && ch <= 'z') || (ch == '_');
    }

    private boolean isAlphanumeric(char ch) {
        return isAlpha(ch) || isDigit(ch);
    }

    private void extractNumber() {
        while (isDigit(peek())) advance();
        if (peek() == '.' && isDigit(peekNext())) {
            while(isDigit(peek())) advance();
        }
    }
    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }
    private void addToken(TokenType type) {
        addToken(type, null);
    }
    private void addToken(TokenType type, Object literal) {


        String text = source.substring(start, current);

        if (type == STRING) {
            text = source.substring(start + 1, current - 1);
        } else if (type == NUMBER) {
            literal = Double.parseDouble(text);
        } else if (type == IDENTIFIER) {
            type = keywords.get(text);
            if (type == null) { type = IDENTIFIER;}
        }
        tokens.add(new Token(type, text, literal, line));
    }

}
