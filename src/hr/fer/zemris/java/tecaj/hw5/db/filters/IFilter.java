package hr.fer.zemris.java.tecaj.hw5.db.filters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * An interface whose implementing classes are used for filtering
 * {@link StudentRecord student records}.
 *
 * @author Mario Bobic
 */
public interface IFilter {

    /**
     * Returns true if the filter accepts the specified <tt>record</tt>. False
     * otherwise.
     *
     * @param record record to be tested
     * @return true if the filter accepts the specified <tt>record</tt>
     */
    public boolean accepts(StudentRecord record);

}
