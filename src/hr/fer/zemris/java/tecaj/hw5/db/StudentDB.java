package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw5.db.commands.*;

/**
 * Scans the user's input and searches for a matching command. Some commands
 * require arguments, so the user must input them as well. If the inputed
 * command is found, the command is executed, otherwise an error message is
 * displayed. The program terminates after the user has typed Exit.
 *
 * @author Mario Bobic
 */
public class StudentDB {
	
	/** Name of the file from which all lines are read. */
	private static final String FILENAME = "./database.txt";
	
	/** A map of commands. */
	private static SimpleHashtable<String, AbstractCommand> commands;
	
	static {
		commands = new SimpleHashtable<>();
		AbstractCommand[] cmdArr = {
				new QueryCommand(),
				new IndexQueryCommand()
		};
		for (AbstractCommand cmd : cmdArr) {
			commands.put(cmd.getCommandName(), cmd);
		}
	}
	
	/** A database of student records. */
	private static StudentDatabase database;
	/** Output generated from a command or an error message. */
	private static String output;

	/**
	 * Program entry point.
	 * 
	 * @param args not used in this program
	 * @throws IOException if an IO exception occurs while reading the input
	 */
	public static void main(String[] args) throws IOException {
		try {
			List<String> lines = Files.readAllLines(
				Paths.get(FILENAME),
				StandardCharsets.UTF_8
			);
			database = new StudentDatabase(lines);
		} catch (NoSuchFileException exc) {
			System.err.println("File " + FILENAME + " not found");
			System.exit(1);
		} catch (MalformedLineError err) {
			System.err.println("In file " + FILENAME + ", " + err.getMessage());
			System.exit(2);
		} catch (IOException exc) {
			System.err.println("Error reading " + FILENAME + ": " + exc.getMessage());
			System.exit(3);
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			System.out.print("> ");
			String line = reader.readLine().trim();
			String cmd;
			String arg;
			int splitter = indexOfWhitespace(line);
			if (splitter != -1) {
				cmd = line.substring(0, splitter);
				arg = line.substring(splitter+1).trim();
			} else {
				cmd = line;
				arg = null;
			}
			if (cmd.equalsIgnoreCase("exit")) {
				break;
			}
			
			AbstractCommand command = commands.get(cmd);
			if (command == null) {
				output = "Unknown command!";
				System.out.println(output);
				continue;
			}
			
			try {
				command.execute(arg);
			} catch (Exception e) {
				output = e.getMessage();
			}
			
			System.out.println(output);
		}
		
		System.out.println("Goodbye!");
	}
	
	/**
	 * Returns the database that is used by this program.
	 * 
	 * @return the database that is used by this program
	 */
	public static StudentDatabase getDatabase() {
		return database;
	}
	
	/**
	 * Returns the last output generated from a command or an error message.
	 * 
	 * @return the last output generated from a command or an error message
	 */
	public static String getOutput() {
		return output;
	}
	
	/**
	 * Sets the current output to the specified output.
	 * 
	 * @param output output to be set as current
	 */
	public static void setOutput(String output) {
		StudentDB.output = output;
	}
	
	/**
	 * Returns the index within the specified string <tt>str</tt> of the first
	 * occurrence of a whitespace character determined by the
	 * {@linkplain Character#isWhitespace(char)} method.
	 * 
	 * @param str string whose index of the first whitespace is to be returned
	 * @return the index of the first occurrence of a whitespace character
	 */
	private static int indexOfWhitespace(String str) {
		for (int i = 0, n = str.length(); i < n; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return i;
			}
		}
		return -1;
	}

}
