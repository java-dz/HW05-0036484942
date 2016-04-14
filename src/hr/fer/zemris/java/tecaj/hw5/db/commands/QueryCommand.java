package hr.fer.zemris.java.tecaj.hw5.db.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.StudentDB;
import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentPrinter;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;
import hr.fer.zemris.java.tecaj.hw5.db.filters.QueryFilter;

/**
 * The <tt>query</tt> command is the command used for sequential record
 * filtering using complex expressions. Filtering expressions are built using
 * <tt>jmbag</tt>, <tt>lastName</tt> and <tt>firstName</tt> attributes. No other
 * attributes are allowed in query. Filtering expression consists from multiple
 * comparison expressions. If more than one expression is specified, all of them
 * must be composed by logical AND operator.
 *
 * @author Mario Bobic
 */
public class QueryCommand extends AbstractCommand {

	/** Defines the proper syntax for using this command */
	private static final String SYNTAX = "query expression1 AND expression2 AND...";
	
	/**
	 * Constructs a new command object of type {@code QueryCommand}.
	 */
	public QueryCommand() {
		super("query");
	}

	@Override
	public void execute(String s) {
		if (s == null) {
			throwSyntaxException(SYNTAX);
		}
		
		QueryFilter filter = new QueryFilter(s);
		StudentDatabase database = StudentDB.getDatabase();
		
		List<StudentRecord> filtered = database.filter(filter);
		String output = StudentPrinter.print(filtered);
		
		StudentDB.setOutput(output);
	}

}
