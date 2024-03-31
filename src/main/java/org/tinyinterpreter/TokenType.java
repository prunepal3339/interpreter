package org.tinyinterpreter;

public enum TokenType {
    //single character token: (){},.-+;/*
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL, // !,!=
    EQUAL, EQUAL_EQUAL, // =,==
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    //Literals
    IDENTIFIER, STRING, NUMBER,

    //Keywords

    AND, OR, NOT,
    IF, ELSE, FOR, WHILE,
    FUN, PRINT, RETURN,
    CLASS, SUPER, THIS,
    VAR, NIL, TRUE, FALSE,
    EOF,
    COMMENT, WHITESPACE, NEWLINE,
}