package hr.fer.zemris.java.tecaj.hw5.db.fieldgetters;

import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * This class is one of the {@linkplain IFieldValueGetter} implementations.
 * Calling the method {@linkplain #get(StudentRecord)} returns the JMBAG of the
 * student specified by his record.
 *
 * @author Mario Bobic
 */
public class GetterJmbag implements IFieldValueGetter {

    /**
     * Returns the JMBAG of the student specified by the record.
     */
    @Override
    public String get(StudentRecord record) {
        return record.getJmbag();
    }

}
