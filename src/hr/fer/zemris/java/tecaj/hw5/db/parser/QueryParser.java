package hr.fer.zemris.java.tecaj.hw5.db.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw5.db.fieldgetters.*;
import hr.fer.zemris.java.tecaj.hw5.db.lexer.*;
import hr.fer.zemris.java.tecaj.hw5.db.operators.*;

import static hr.fer.zemris.java.tecaj.hw5.db.lexer.TokenType.*;

/**
 * This class is used for parsing queries with a certain set of rules. It parses
 * the query using {@linkplain Lexer} as a lexical analysis tool by calling the
 * {@linkplain Lexer#nextToken} method for new tokens until the end of the query
 * is reached. If an exception is thrown while calling the nextToken method, it
 * is caught and rethrown as a {@linkplain QueryParserException}. A parsing
 * error may also throw a QueryParserException with an appropriate detail
 * message. Parsing error may occur:
 * <ul>
 * <li>if an expression has an invalid number of <tt>tokens</tt>,
 * <li>if the left-hand side of an expression is not a field,
 * <li>if the specified field does not exist
 * <li>if the expression does not contain an operator in the middle or
 * <li>if the right-hand side is not a string literal.
 * </ul>
 * <p>
 * Rules for basic mode parsing:<br>
 * By using Lexer, the parser is sure that all field names are valid (as in they
 * do not contain illegal characters), all operators are existent and all string
 * literals are properly made. These types of tokens are safe to be retrieved.
 * If Lexer can not return a proper token, it throws a LexerException which is
 * caught and rethrown as a QueryParserException.
 * <p>
 * Rules for string parsing:<br>
 * A string literal begins with a quotation mark symbol, which when occurs and
 * is returned in the Lexer's basic state, this Parser is safe to switch the
 * state of Lexer to {@link LexerState#READING_STRING READING_STRING}. When
 * reading a string, all characters are permitted except a quotation mark
 * character. When Lexer encounters another quotation mark character in the
 * string-reading state, it stops reading the string and returns it as a
 * {@link TokenType#STRING_LITERAL STRING_LITERAL}. The expected character after
 * that is another quotation mark symbol which tells the Parser to switch
 * Lexer's state.
 * 
 * @author Mario Bobic
 */
public class QueryParser {

	/** A reference to a Lexer used for tokenizing the query. */
	private Lexer lexer;
	/** A list of expressions separated by the AND logical operator. */
	private List<ConditionalExpression> expressionList;
	
	/**
	 * Constructs an instance of a QueryParser with the specified query text and
	 * parses it into a list of expressions.
	 * 
	 * @param text query text to be parsed
	 * @throws QueryParserException if a parsing error occurs
	 */
	public QueryParser(String text) {
		lexer = new Lexer(text);
		expressionList = new ArrayList<>();
		
		parse();
	}
	
	/**
	 * Returns a list of expressions parsed from the query text.
	 * 
	 * @return a list of expressions parsed from the query text
	 */
	public List<ConditionalExpression> getExpressionList() {
		return expressionList;
	}

	/**
	 * Parses the query using {@linkplain Lexer} as a lexical analysis tool by
	 * calling the {@linkplain Lexer#nextToken} method for new tokens until the
	 * end of the query is reached. If an exception is thrown while calling the
	 * nextToken method, it is caught and rethrown as a
	 * {@linkplain QueryParserException}. A parsing error may also throw a
	 * QueryParserException with an appropriate detail message. Parsing error
	 * may occur:
	 * <ul>
	 * <li>if an expression has an invalid number of <tt>tokens</tt>,
	 * <li>if the left-hand side of an expression is not a field,
	 * <li>if the specified field does not exist
	 * <li>if the expression does not contain an operator in the middle or
	 * <li>if the right-hand side is not a string literal.
	 * </ul>
	 * 
	 * @throws QueryParserException if a parsing error occurs
	 */
	private void parse() {
		Token token;
		List<Token> tokenList = new ArrayList<>();
		
		while (true) {
			try {
				token = lexer.nextToken();
			} catch (LexerException e) {
				throw new QueryParserException(e.getMessage(), e.getCause());
			}
			
			TokenType tokenType = token.getType();
			
			if (tokenType == KEYWORD_AND) {
				createExpression(tokenList);
				tokenList.clear();
			} else if (tokenType == QUOT_MARK) {
				switchLexerState();
			} else if (tokenType == EOF) {
				createExpression(tokenList);
				break;
			} else {
				tokenList.add(token);
			}
		}
	}
	
	/**
	 * Switches, or <tt>toggles</tt> the Lexer state, considering only two
	 * cases:
	 * <ul>
	 * <li>If the Lexer state was {@link LexerState#BASIC BASIC}, the state is
	 * then switched to {@link LexerState#READING_STRING READING_STRING}.
	 * <li>If the Lexer state was {@link LexerState#READING_STRING
	 * READING_STRING}, the state is then switched to {@link LexerState#BASIC
	 * BASIC}.
	 * </ul>
	 */
	private void switchLexerState() {
		if (lexer.getState() == LexerState.BASIC) {
			lexer.setState(LexerState.READING_STRING);
		} else {
			lexer.setState(LexerState.BASIC);
		}
	}
	
	/**
	 * Creates an expression from the specified token list, expecting to have
	 * three elements in the list:
	 * <ol>
	 * <li>the field token,
	 * <li>the operator token and
	 * <li>the literal token.
	 * </ol>
	 * If there are not three elements in the list, or the tokens can not be
	 * properly parsed, a {@linkplain QueryParserException} is thrown with the
	 * specific detail message.
	 * <p>
	 * If the parsing is successful, an instance of
	 * {@linkplain ConditionalExpression} is created and added to the
	 * <tt>expressionList</tt>.
	 * 
	 * @param tokenList list of tokens to be parsed into an expression
	 * @throws QueryParserException if the list contains invalid elements
	 */
	private void createExpression(List<Token> tokenList) {
		if (tokenList.size() != 3) {
			throw new QueryParserException("Invalid expression: " + tokenList);
		}
		
		Token fieldToken = tokenList.get(0);
		if (fieldToken.getType() != TokenType.ATTRIBUTE) {
			throw new QueryParserException("The left-hand side must be a field: " + fieldToken);
		}
		
		Token operatorToken = tokenList.get(1);
		if (operatorToken.getType() != TokenType.OPERATOR) {
			throw new QueryParserException("The middle token must be an operator: " + operatorToken);
		}
		
		Token literalToken = tokenList.get(2);
		if (literalToken.getType() != TokenType.STRING_LITERAL) {
			throw new QueryParserException("The right-hand side must be a literal: " + literalToken);
		}
		
		IFieldValueGetter fieldGetter = getFieldGetter(fieldToken.getValue());
		IComparisonOperator comparisonOperator = getComparisonOperator(operatorToken.getValue());
		String stringLiteral = literalToken.getValue();
		
		ConditionalExpression expression =
				new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator);
		
		expressionList.add(expression);
	}

	/**
	 * Returns an instance of a class implementing the
	 * {@linkplain IFieldValueGetter} if the specified <tt>field</tt> can be
	 * parsed as one of the three fields:
	 * <ul>
	 * <li>JMBAG,
	 * <li>last name or
	 * <li>first name.
	 * </ul>
	 * If the specified field can not be parsed, a
	 * {@linkplain QueryParserException} is thrown.
	 * 
	 * @param field text to be parsed as a field
	 * @return instance of a class implementing IFieldValueGetter
	 * @throws QueryParserException if the specified field can not be parsed
	 */
	private static IFieldValueGetter getFieldGetter(String field) {
		switch (field) {
		case "jmbag":     return new GetterJmbag();
		case "lastName":  return new GetterLastName();
		case "firstName": return new GetterFirstName();
		default:
			throw new QueryParserException("Invalid field on left-hand side: " + field);
		}
	}
	
	/**
	 * Returns an instance of a class implementing the
	 * {@linkplain IComparisonOperator} of the specified <tt>operator</tt>.
	 * 
	 * @param operator text to be parsed as an operator
	 * @return of a class implementing IComparisonOperator
	 */
	private IComparisonOperator getComparisonOperator(String operator) {
		switch (operator) {
		case ">":    return new OperatorGreaterThan();
		case "<":    return new OperatorLessThan();
		case ">=":   return new OperatorGreaterOrEqual();
		case "<=":   return new OperatorLessOrEqual();
		case "=":    return new OperatorEqual();
		case "!=":   return new OperatorNotEqual();
		case "LIKE": return new OperatorLike();
		default:
			throw new QueryParserException("Invalid conditional operator: " + operator);
		}
	}
	
}
