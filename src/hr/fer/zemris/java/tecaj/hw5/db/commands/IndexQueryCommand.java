package hr.fer.zemris.java.tecaj.hw5.db.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw5.db.StudentDB;
import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentPrinter;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;
import hr.fer.zemris.java.tecaj.hw5.db.fieldgetters.GetterJmbag;
import hr.fer.zemris.java.tecaj.hw5.db.operators.OperatorEqual;
import hr.fer.zemris.java.tecaj.hw5.db.parser.QueryParser;

/**
 * The <tt>indexquery</tt> command is always specified with attribute JMBAG,
 * operator equals and a string literal. Every other invocation syntax is
 * illegal. For the input of an illegal syntax, a {@linkplain SyntaxException}
 * may be thrown. This command obtains the requested student using the indexing
 * facility of the {@link StudentDatabase database} in O(1) complexity.
 *
 * @author Mario Bobic
 */
public class IndexQueryCommand extends AbstractCommand {

	/** Defines the proper syntax for using this command */
	private static final String SYNTAX = "indexquery jmbag=\"0123456789\"";
	
	/**
	 * Constructs a new command object of type {@code IndexQueryCommand}.
	 */
	public IndexQueryCommand() {
		super("indexquery");
	}

	@Override
	public void execute(String s) {
		if (s == null) {
			throwSyntaxException(SYNTAX);
		}
		
		QueryParser parser = new QueryParser(s);
		List<ConditionalExpression> list = parser.getExpressionList();
		
		if (list.size() != 1) {
			throwSyntaxException(SYNTAX);
		}
		
		ConditionalExpression expression = list.get(0);
		if (!(expression.getFieldGetter() instanceof GetterJmbag)) {
			throwSyntaxException(SYNTAX);
		}
		if (!(expression.getComparisonOperator() instanceof OperatorEqual)) {
			throwSyntaxException(SYNTAX);
		}
		
		StudentDatabase database = StudentDB.getDatabase();
		String jmbag = expression.getStringLiteral();
		StudentRecord record = database.forJMBAG(jmbag);
		
		String output;
		if (record == null) {
			output = "A record with the specified JMBAG does not exist.";
		} else {
			output = "Using index for record retrieval.\n";
			output += StudentPrinter.print(record);
		}

		StudentDB.setOutput(output);
	}

}
