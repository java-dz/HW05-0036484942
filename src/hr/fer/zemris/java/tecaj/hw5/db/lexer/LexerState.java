package hr.fer.zemris.java.tecaj.hw5.db.lexer;

/**
 * This enumeration describes the Lexer's state. The string-reading state of
 * Lexer should be set when Lexer has encountered a string-beginning character
 * and reads a sequence of character until Lexer encounters a string-ending
 * character. The basic state is the default Lexer state that reads attributes
 * and operators.
 *
 * @author Mario Bobic
 */
public enum LexerState {

	/**
	 * The default Lexer state. Reads attributes and operators.
	 */
	BASIC,

	/**
	 * This state of Lexer should be set when Lexer has encountered a
	 * string-beginning character and reads a sequence of character until Lexer
	 * encounters a string-ending character.
	 */
	READING_STRING
}
