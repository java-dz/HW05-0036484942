package hr.fer.zemris.java.tecaj.hw5.db.lexer;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * Lexer is a program that performs lexical analysis. Lexer is combined with a
 * parser, which together analyze the syntax and extract tokens from the input
 * text.
 * <p>
 * Lexer provides one constructor which accepts an input text to be tokenized.
 * The input text is analyzed and tokens are made depending on the token type.
 * It also provides a method for generating the next token and a method that
 * returns the last generated token.
 * <p>
 * Lexer has two states, {@link LexerState#BASIC basic} and
 * {@link LexerState#READING_STRING reading string}. The string-reading state of
 * Lexer should be set when Lexer has encountered a string-beginning character
 * and reads a sequence of character until Lexer encounters a string-ending
 * character. The basic state is the default Lexer state that reads attributes
 * and operators.
 *
 * @author Mario Bobic
 * @see TokenType
 * @see LexerState
 */
public class Lexer {

    /** An underscore character used for getting attributes. */
    private static final Character UNDERSCORE = '_';
    /** A quotation mark used for representing strings. */
    private static final Character QUOT_MARK = '"';
    /** The AND keyword used for expression separation. */
    private static final String AND = "AND";
    /** A special kind of operator. */
    private static final String LIKE = "LIKE";

    /** Operator tokens used by this Lexer. */
    private static SimpleHashtable<String, Token> operators;
    static {
        operators = new SimpleHashtable<>();
        Token[] tokenArr = {
                new Token(TokenType.OPERATOR, ">"),
                new Token(TokenType.OPERATOR, "<"),
                new Token(TokenType.OPERATOR, ">="),
                new Token(TokenType.OPERATOR, "<="),
                new Token(TokenType.OPERATOR, "="),
                new Token(TokenType.OPERATOR, "!=")
        };
        for (Token token : tokenArr) {
            operators.put(token.getValue(), token);
        }
    }

    /** Input text for tokenization. */
    private final char[] data;
    /** Current token. */
    private Token token;
    /** Index of the first character to process. */
    private int currentIndex;
    /** Current state of Lexer. */
    private LexerState state;

    /**
     * Constructs an instance of Lexer with the given input text to be
     * tokenized.
     *
     * @param text text for tokenization
     * @throws IllegalArgumentException if the input text is null
     */
    public Lexer(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null.");
        }

        data = text.toCharArray();
        currentIndex = 0;
        state = LexerState.BASIC;
    }

    /**
     * Generates and returns the next token. Tokens are generated from the input
     * text and their type depends on the input text. If the input text is
     * exhausted, a {@linkplain LexerException} is thrown.
     *
     * @return the next token generated from the input text
     * @throws LexerException if there is no next token to generate
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No next token after EOF.");
        }

        skipWhitespaces();

        if (isDataEnd()) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        if (state == LexerState.BASIC) {
            token = getBasicToken();
        } else if (state == LexerState.READING_STRING) {
            token = getStringToken();
        } else {
            throw new LexerException("Illegal state: " + state);
        }

        return token;
    }

    /**
     * Returns the last generated token. This method may be called multiple
     * times because it does not generate the next token.
     *
     * @return the last generated token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Returns the current state of Lexer.
     *
     * @return the current state of Lexer
     */
    public LexerState getState() {
        return state;
    }

    /**
     * Sets the current state of Lexer.
     *
     * @param state state to be set
     * @throws IllegalArgumentException if the given state is <tt>null</tt>
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be null.");
        }
        this.state = state;
    }

    /**
     * Returns a basic token that may be either an {@link TokenType#ATTRIBUTE
     * attribute}, an {@link TokenType#OPERATOR operator}, an
     * {@link TokenType#KEYWORD_AND and keyword} or even a
     * {@link TokenType#QUOT_MARK quotation mark} token starting from the
     * <tt>currentIndex</tt>. This method increases the currentIndex variable as
     * it goes. This method may throw a {@linkplain LexerException} if the token
     * is unknown.
     *
     * @return a token created in this method
     * @throws LexerException if the token is unknown
     */
    private Token getBasicToken() {
        Character firstChar = data[currentIndex];

        if (Character.isLetter(firstChar) || firstChar.equals(UNDERSCORE)) {
            return readAttributeOrKeyword();
        } else if (isOperator(firstChar)) {
            return readOperator();
        } else if (firstChar.equals(QUOT_MARK)) {
            currentIndex++;
            return new Token(TokenType.QUOT_MARK, null);
        } else {
            String invalid = scanInvalidToken();
            throw new LexerException("Unknown token: " + invalid);
        }
    }

    /**
     * A string token is generated when the state of this Lexer is set to
     * {@linkplain LexerState#READING_STRING}. This state should be set when
     * Lexer has encountered a quotation mark symbol. This method increases the
     * currentIndex variable as it goes and stops when it encounters another
     * quotation mark symbol. If the text data has ended during the string
     * extraction, the string is invalid and a {@linkplain LexerException} is
     * thrown.
     *
     * @return a token of type {@link TokenType#STRING_LITERAL literal}
     * @throws LexerException if the string is invalid
     */
    private Token getStringToken() {
        StringBuilder sb = new StringBuilder();

        while (true) {
            Character symbol = data[currentIndex];

            if (!symbol.equals(QUOT_MARK)) {
                sb.append(symbol);
                currentIndex++;
            } else {
                break;
            }

            if (isDataEnd()) {
                throw new LexerException("String has no ending: " + sb.toString());
            }
        }

        if (sb.length() == 0) {
            currentIndex++; // move from the " symbol
            return new Token(TokenType.QUOT_MARK, null);
        }

        return new Token(TokenType.STRING_LITERAL, sb.toString());
    }

    /**
     * Reads the text data starting from the currentIndex until it encounters a
     * character that is not either a letter or digit, or an {@link #UNDERSCORE
     * underscore} character. In case the string that was read is a keyword,
     * this method returns an {@link TokenType#KEYWORD_AND and keyword} token.
     * In case the string that was read is a LIKE operator, a regular
     * {@link TokenType#OPERATOR operator} token is returned. Else, an
     * {@link TokenType#ATTRIBUTE attribute} token is returned.
     *
     * @return a token that is either a keyword or an attribute
     */
    private Token readAttributeOrKeyword() {
        StringBuilder sb = new StringBuilder();

        while (true) {
            Character symbol = data[currentIndex];

            if (Character.isLetterOrDigit(symbol) || symbol.equals(UNDERSCORE)) {
                sb.append(symbol);
                currentIndex++;
            } else {
                break;
            }

            if (isDataEnd()) break;
        }

        String result = sb.toString();
        if (result.toUpperCase().equals(AND)) {
            return new Token(TokenType.KEYWORD_AND, null);
        } else if (result.equals(LIKE)) {
            return new Token(TokenType.OPERATOR, LIKE);
        } else {
            return new Token(TokenType.ATTRIBUTE, result);
        }
    }

    /**
     * Reads an operator starting from the currentIndex and reading a maximum of
     * two slots. If the read operator does not exist, a
     * {@linkplain LexerException} is thrown.
     *
     * @return a token of type {@linkplain TokenType#OPERATOR}
     */
    private Token readOperator() {
        String operator = "";

        operator += data[currentIndex];
        currentIndex++;

        /* If the next character belong to this operator. */
        if (!isDataEnd() && isOperator(data[currentIndex])) {
            operator += data[currentIndex];
            currentIndex++;
        }

        Token token = operators.get(operator);
        if (token == null) {
            throw new LexerException("Invalid operator: " + operator);
        }

        return token;
    }

    /**
     * Returns true if the {@linkplain #operators} map contains a mapping of the
     * specified character. Furthermore, the character may be any part of the
     * operator.
     *
     * @param c an operator or part of the operator
     * @return true if the specified character is an operator or a part of it
     */
    private static boolean isOperator(char c) {
        for (SimpleHashtable.TableEntry<String, Token> entry : operators) {
            String operator = entry.getKey();
            if (operator.contains(c+"")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans and returns a string representation of the invalid token that was
     * encountered during the reading.
     *
     * @return a string representation of the invalid token
     */
    private String scanInvalidToken() {
        StringBuilder sb = new StringBuilder();

        while (true) {
            Character symbol = data[currentIndex++];
            sb.append(symbol);

            if (Character.isWhitespace(symbol) || isDataEnd()) {
                break;
            }
        }

        return sb.toString();
    }

    /**
     * Skips all whitespace characters by moving the <tt>currentIndex</tt>
     * variable to the next non-whitespace character.
     */
    private void skipWhitespaces() {
        int i;
        for (i = currentIndex; !isDataEnd() && Character.isWhitespace(data[i]); i++);
        currentIndex = i;
    }

    /**
     * Returns true if the input text has been exhausted, or more formally if
     * <tt>currentIndex == data.length</tt>. False otherwise.
     *
     * @return true if the input text has been exhausted
     */
    private boolean isDataEnd() {
        return currentIndex == data.length;
    }

}
