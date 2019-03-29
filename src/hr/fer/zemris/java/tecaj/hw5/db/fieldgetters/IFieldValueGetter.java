package hr.fer.zemris.java.tecaj.hw5.db.fieldgetters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * An interface representing a getter of a field value from the
 * {@linkplain StudentRecord}. Calling the method
 * {@linkplain #get(StudentRecord)} returns a specific field value of the
 * student specified by his record.
 *
 * @author Mario Bobic
 * @see GetterJmbag
 * @see GetterLastName
 * @see GetterFirstName
 */
public interface IFieldValueGetter {

    /**
     * Returns a specific field value of the student specified by his record.
     *
     * @param record the specified record
     * @return a specific field value
     */
    public String get(StudentRecord record);

}
