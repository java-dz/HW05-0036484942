package hr.fer.zemris.java.tecaj.hw5.db.operators;

/**
 * Implements the {@linkplain IComparisonOperator} to form a <tt>like</tt>
 * operator that compares strings lexicographically, with an exception of an
 * asterisk symbol than represents 0 or more characters that should be ignored
 * while making a comparison.
 *
 * @author Mario Bobic
 */
public class OperatorLike implements IComparisonOperator {

	/**
	 * Returns true if <tt>value1</tt> lexicographically matches <tt>value2</tt>.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		return matches(value1, value2);
	}
	
	/**
	 * Returns true if the specified <tt>name</tt> matches the <tt>pattern</tt>.
	 * The <tt>pattern</tt> can contain an asterisk character '*' that
	 * represents 0 or more characters that should not be considered while
	 * matching.
	 * 
	 * @param name name that is being examined
	 * @param pattern a pattern that may contain the asterisk character
	 * @return true if <tt>name</tt> matches the <tt>pattern</tt>. False otherwise
	 */
	private static boolean matches(String name, String pattern) {
		if (countOccurrencesOf(pattern, '*') > 1) {
			throw new IllegalArgumentException(
				"Pattern of LIKE operator may contain at most 1 asterisk symbol."
				+ " Your input: " + pattern);
		}
		
		if (pattern.contains("*")) {
			int r = pattern.indexOf("*");
			String start = pattern.substring(0, r);
			String end = pattern.substring(r+1);
			if (name.startsWith(start) && name.endsWith(end))
				return true;
		} else if (name.equals(pattern)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Counts the number of occurrences of the specified char in a string.
	 * 
	 * @param string string from which the occurrences are counted
	 * @param c character whose occurrences are to be counted
	 * @return the number of occurences of the specified char in a string
	 */
	private static int countOccurrencesOf(String string, char c) {
		return string.length() - string.replace(c + "", "").length();
	}

}
