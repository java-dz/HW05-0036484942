package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

/**
 * This class offers two public static methods for printing student records. The
 * {@linkplain #print(StudentRecord)} returns a string representation of a
 * single student record while the {@linkplain #print(List)} returns a string
 * representation of a list of student records. The returned string will contain
 * in a table of records where each record will have its row and the table will
 * be circulated with two borders, one from the north side and one from the
 * south side.
 *
 * @author Mario Bobic
 */
public class StudentPrinter {
	
	/**
	 * Length of the JMBAG attribute,
	 * used for printing the JMBAG column.
	 */
	private static final int JMBAG_LENGTH = 10;
	
	/**
	 * Length of the final grade attribute,
	 * used for printing the final grade column.
	 */
	private static final int GRADE_LENGTH = 1;
	
	/**
	 * Length of the longest last name,
	 * used for printing the last name column.
	 */
	private static int lastNameLength;
	/**
	 * Length of the longest first name,
	 * used for printing the first name column.
	 */
	private static int firstNameLength;
	
	/**
	 * StringBuilder used for creating a whole table of records.
	 */
	private static StringBuilder sb;

	/**
	 * Returns a string representation of the specified list of <tt>records</tt>
	 * in a table of records. Each record will have its row and the table will
	 * be circulated with two borders, one from the north side and one from the
	 * south side.
	 * 
	 * @param records list of records to be returned as a string
	 * @return a string representation of the specified list of <tt>records</tt>
	 */
	public static String print(List<StudentRecord> records) {
		if (records.size() == 0) {
			return "Records selected: 0";
		}
		
		sb = new StringBuilder();
		lastNameLength = 0;
		firstNameLength = 0;
		
		for (StudentRecord record : records) {
			int lastLen = record.getLastName().length();
			if (lastLen > lastNameLength) {
				lastNameLength = lastLen;
			}
			
			int firstLen = record.getFirstName().length();
			if (firstLen > firstNameLength) {
				firstNameLength = firstLen;
			}
		}
		
		printBorder();
		for (StudentRecord record : records) {
			printRecord(record);
		}
		printBorder();
		sb.append("Records selected: " + records.size());
		
		return sb.toString();
	}

	/**
	 * Returns a string representation of the specified <tt>record</tt> in a
	 * table of records. The table will be circulated with two borders, one from
	 * the north side and one from the south side.
	 * 
	 * @param record record to be returned as a string
	 * @return a string representation of the specified <tt>record</tt>
	 */
	public static String print(StudentRecord record) {
		List<StudentRecord> oneRecord = new ArrayList<>();
		oneRecord.add(record);
		return print(oneRecord);
	}
	
	/**
	 * Appends the border of the table of records considering the maximum
	 * length for all attributes and their padding from both sides.
	 */
	private static void printBorder() {
		char corner = '+';
		char edge = '=';
		
		sb.append(corner);
		appendEdge(JMBAG_LENGTH+2, edge);
		sb.append(corner);
		appendEdge(lastNameLength+2, edge);
		sb.append(corner);
		appendEdge(firstNameLength+2, edge);
		sb.append(corner);
		appendEdge(GRADE_LENGTH+2, edge);
		sb.append(corner);
		sb.append('\n');
	}
	
	/**
	 * Appends the specified <tt>edge</tt> character <tt>n</tt> times to the
	 * <tt>StringBuilder sb</tt>.
	 * 
	 * @param n times the specified character will be appended
	 * @param edge character that is to be appended
	 */
	private static void appendEdge(int n, char edge) {
		for (int i = 0; i < n; i++) {
			sb.append(edge);
		}
	}
	
	/**
	 * Appends the specified record padding the strings with space characters
	 * to match the maximum length attributes.
	 * 
	 * @param record record to be printed
	 */
	private static void printRecord(StudentRecord record) {
		char separator = '|';
		
		sb.append(separator).append(' ');
		sb.append(rightPad(record.getJmbag(), JMBAG_LENGTH)).append(' ');
		sb.append(separator).append(' ');
		sb.append(rightPad(record.getLastName(), lastNameLength)).append(' ');
		sb.append(separator).append(' ');
		sb.append(rightPad(record.getFirstName(), firstNameLength)).append(' ');
		sb.append(separator).append(' ');
		sb.append(rightPad(record.getFinalGrade().toString(), GRADE_LENGTH)).append(' ');
		sb.append(separator);
		sb.append('\n');
	}
	
	/**
	 * Right pad a String with spaces (' ').
	 * <p>
	 * The String is padded to the size of <tt>size</tt>.
	 * 
	 * <pre>
	 * rightPad(null, *)   = null
	 * rightPad("", 3)     = "   "
	 * rightPad("bat", 3)  = "bat"
	 * rightPad("bat", 5)  = "bat  "
	 * rightPad("bat", 1)  = "bat"
	 * rightPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str the String to pad out
	 * @param size the size to pad to
	 * @return right padded String or original String if no padding is necessary
	 */
	// https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/src-html/org/apache/commons/lang/StringUtils.html#line.4829
	public static String rightPad(String str, int size) {
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		
		final char[] buf = new char[pads];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = ' ';
		}
		
		return str.concat(new String(buf));
	}
	
}
