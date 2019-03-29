package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw5.db.filters.IFilter;

/**
 * This class represents a table of student records stored in the database. It
 * acts as a list of all {@link StudentRecord StudentRecords} and is organized
 * to get student records in time complexity of O(1).
 *
 * @author Mario Bobic
 */
public class StudentDatabase {

    /** A splitter used for splitting student record attributes. */
    private static final String SPLITTER = "\\t";

    /** List of student records. */
    private List<StudentRecord> recordsList;
    /** Map of student records. */
    private SimpleHashtable<String, StudentRecord> recordsMap;

    /**
     * Constructs an instance of StudentDatabase object, where the specified
     * list of lines is parsed as students, one student per line and saved.
     *
     * @param lines list of lines that represent student data
     * @throws MalformedLineError if a malformed line is present
     */
    public StudentDatabase(List<String> lines) {
        recordsList = getRecords(lines);

        recordsMap = new SimpleHashtable<>(recordsList.size());
        for (StudentRecord record : recordsList) {
            recordsMap.put(record.getJmbag(), record);
        }
    }

    /**
     * Parses the lines from the list as student data and returns a
     * {@linkplain List} object filled with student data.
     *
     * @param lines list of lines that represent student data
     * @return a List filled with student data
     * @throws MalformedLineError if a malformed line is present
     */
    private static List<StudentRecord> getRecords(List<String> lines) {
        List<StudentRecord> records = new ArrayList<>();

        int lineCount = 1;
        for (String line : lines) {
            try {
                String[] attrs = line.split(SPLITTER);
                String jmbag = attrs[0];
                String lastName = attrs[1];
                String firstName = attrs[2];
                Integer finalGrade = Integer.parseInt(attrs[3]);
                records.add(new StudentRecord(jmbag, lastName, firstName, finalGrade));
            } catch (Exception e) {
                throw new MalformedLineError(
                        "Malformed line " + lineCount + ": " + line);
            }
            lineCount++;
        }

        return records;
    }

    /**
     * Returns the {@linkplain StudentRecord} to which the specified JMBAG is
     * assigned, or <tt>null</tt> if this database contains no mapping for the
     * specified JMBAG.
     *
     * @param jmbag student's JMBAG
     * @return the StudentRecord to which the specified JMBAG is assigned, or
     *         <tt>null</tt> if this JMBAG is not contained in the database
     */
    public StudentRecord forJMBAG(String jmbag) {
        return recordsMap.get(jmbag);
    }

    /**
     * Loops through all student records in this database and calls the
     * {@link IFilter#accepts accepts} method on given filter for all records.
     * Each record for which the called method returns true is added to filtered
     * list and this list is then returned.
     *
     * @param filter filter that filters the records
     * @return a filtered list of records
     */
    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> filteredList = new ArrayList<>();

        for (StudentRecord record : recordsList) {
            if (filter.accepts(record)) {
                filteredList.add(record);
            }
        }

        return filteredList;
    }

}
