package hr.fer.zemris.java.tecaj.hw5.db.lexer;

/**
 * This enumeration describes the type of a token. A token may be an attribute,
 * an operator, a keyword or a literal which all contain a value, or
 * <tt>EOF</tt> that indicates the end of data and does not contain a value.
 *
 * @author Mario Bobic
 */
public enum TokenType {

    /**
     * Indicates the end of data and does not contain a value.
     */
    EOF,

    /**
     * Represents a token that consists of 1 or more characters and is most
     * commonly used as a variable.
     */
    ATTRIBUTE,

    /**
     * Represents a token that is an operator.
     */
    OPERATOR,

    /**
     * Represents a token that is an AND keyword.
     */
    KEYWORD_AND,

    /**
     * Represents a token that is either a beginning of string or its ending.
     */
    QUOT_MARK,

    /**
     * Represents a token that is a string literal.
     */
    STRING_LITERAL
}
