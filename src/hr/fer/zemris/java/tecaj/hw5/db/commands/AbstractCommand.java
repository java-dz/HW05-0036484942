package hr.fer.zemris.java.tecaj.hw5.db.commands;

/**
 * Used as a superclass for other, usable commands.
 *
 * @author Mario Bobic
 */
public abstract class AbstractCommand {

    /** Name of the command */
    private String commandName;

    /**
     * Generates a new command of a type extending <tt>AbstractCommand</tt>.
     *
     * @param commandName name of the command
     */
    public AbstractCommand(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Returns the name of the command.
     *
     * @return the name of the command
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command. Every command has its own unique way of executing.
     * Most commands write out their steps of executing, or they write out
     * certain errors. Arguments may or may not exist (String has a valid value
     * or <tt>null</tt>). The user is advised to check the implementing class
     * documentation in order to see what this command does.
     *
     * @param s arguments
     */
    public abstract void execute(String s);

    /**
     * Throws a {@linkplain SyntaxException} with the detail message that has
     * what the command expected as arguments.

     * @param syntax the expected syntax
     */
    protected static final void throwSyntaxException(String syntax) {
        throw new SyntaxException("The syntax of the command is incorrect. Expected: " + syntax);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commandName == null) ? 0 : commandName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractCommand))
            return false;
        AbstractCommand other = (AbstractCommand) obj;
        if (commandName == null) {
            if (other.commandName != null)
                return false;
        } else if (!commandName.equals(other.commandName))
            return false;
        return true;
    }

}
