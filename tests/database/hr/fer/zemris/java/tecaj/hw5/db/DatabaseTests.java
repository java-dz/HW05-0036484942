package hr.fer.zemris.java.tecaj.hw5.db;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class DatabaseTests {

    /**
     * Sets the System.out to a dummy print stream that does nothing to prevent
     * console printing when running the main program.
     */
    static {
        PrintStream emptyOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        });
        System.setOut(emptyOut);
    }

    /* ------------------------------ Hard core string matching tests ------------------------------ */

    @Test
    public void testIndexQuery1() {
        String expected = "Using index for record retrieval.\n"
                        + "+============+===========+=======+===+\n"
                        + "| 0000000001 | Akšamović | Marin | 2 |\n"
                        + "+============+===========+=======+===+\n"
                        + "Records selected: 1";
        String actual = getActual("indexquery jmbag=\"0000000001\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testIndexQuery2() {
        String expected = "Using index for record retrieval.\n"
                        + "+============+===========+=======+===+\n"
                        + "| 0000000002 | Bakamović | Petra | 3 |\n"
                        + "+============+===========+=======+===+\n"
                        + "Records selected: 1";
        String actual = getActual("indexquery jmbag=\"0000000002\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testIndexQuery3() {
        String expected = "Using index for record retrieval.\n"
                        + "+============+========+========+===+\n"
                        + "| 0000000003 | Bosnić | Andrea | 4 |\n"
                        + "+============+========+========+===+\n"
                        + "Records selected: 1";
        String actual = getActual("indexquery jmbag=\"0000000003\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testIndexQuery4() {
        String expected = "Using index for record retrieval.\n"
                        + "+============+=======+=======+===+\n"
                        + "| 0000000004 | Božić | Marin | 5 |\n"
                        + "+============+=======+=======+===+\n"
                        + "Records selected: 1";
        String actual = getActual("indexquery jmbag=\"0000000004\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testIndexQuery5() {
        String expected = "Using index for record retrieval.\n"
                        + "+============+==========+===========+===+\n"
                        + "| 0000000005 | Brezović | Jusufadis | 2 |\n"
                        + "+============+==========+===========+===+\n"
                        + "Records selected: 1";
        String actual = getActual("indexquery jmbag=\"0000000005\"");

        assertEquals(expected, actual);
    }


    /* ------------------------------ Regular contains-based tests ------------------------------ */

    /* ---------- Double surname tests ---------- */

    @Test
    public void testIndexQueryDoubleSurname1() {
        String jmbag = "0000000015";
        String lastName = "Glavinić Pecotić";
        String firstName = "Kristijan";
        String finalGrade = "4";

        String actual = getActual("indexquery jmbag=\"0000000015\"");
        assertEquals(true, actual.contains(jmbag));
        assertEquals(true, actual.contains(lastName));
        assertEquals(true, actual.contains(firstName));
        assertEquals(true, actual.contains(finalGrade));
    }

    @Test
    public void testIndexQueryDoubleSurname2() {
        String jmbag = "0000000031";
        String lastName = "Krušelj Posavec";
        String firstName = "Bojan";
        String finalGrade = "4";

        String actual = getActual("indexquery jmbag=\"0000000031\"");
        assertEquals(true, actual.contains(jmbag));
        assertEquals(true, actual.contains(lastName));
        assertEquals(true, actual.contains(firstName));
        assertEquals(true, actual.contains(finalGrade));
    }


    /* ---------- Operator result tests ---------- */

    @Test
    public void testOperatorGreaterThan() {
        List<String> jmbagList = new ArrayList<>();
        jmbagList.add("0000000056");
        jmbagList.add("0000000058");
        jmbagList.add("0000000061");
        jmbagList.add("0000000063");
        String actual = getActual("query firstName>\"Mirko\" AND lastName>\"Ši\"");

        for (String jmbag : jmbagList) {
            assertEquals(true, actual.contains(jmbag));
        }
    }

    @Test
    public void testOperatorLessThan() {
        List<String> jmbagList = new ArrayList<>();
        jmbagList.add("0000000003");
        jmbagList.add("0000000021");
        jmbagList.add("0000000023");
        jmbagList.add("0000000025");
        jmbagList.add("0000000030");
        String actual = getActual("query jmbag<\"0000000031\" AND firstName < \"Đive\" AND lastName    <\"Orešković\"");

        for (String jmbag : jmbagList) {
            assertEquals(true, actual.contains(jmbag));
        }
    }

    @Test
    public void testOperatorGreaterOrEqual() {
        List<String> jmbagList = new ArrayList<>();
        jmbagList.add("0000000052");
        jmbagList.add("0000000054");
        jmbagList.add("0000000056");
        jmbagList.add("0000000058");
        jmbagList.add("0000000059");
        jmbagList.add("0000000061");
        jmbagList.add("0000000062");
        jmbagList.add("0000000063");
        String actual = getActual("query jmbag>=\"0000000050\" AND firstName >=\"Ivo\"    AND    lastName>=\"Kos-Grabar\"");

        for (String jmbag : jmbagList) {
            assertEquals(true, actual.contains(jmbag));
        }
    }

    @Test
    public void testOperatorLessOrEqual() {
        String jmbag = "0000000003";
        String lastName = "Bosnić";
        String firstName = "Andrea";
        String finalGrade = "4";

        String actual = getActual("query      jmbag         <=    \"0000000010\"        AND   lastName<=     \"Ć\"    AND    firstName <=\"I\"");
        assertEquals(true, actual.contains(jmbag));
        assertEquals(true, actual.contains(lastName));
        assertEquals(true, actual.contains(firstName));
        assertEquals(true, actual.contains(finalGrade));
    }

    @Test
    public void testOperatorEqual() {
        String jmbag = "0000000003";
        String lastName = "Bosnić";
        String firstName = "Andrea";
        String finalGrade = "4";

        String actual = getActual("query    lastName     =\"Bosnić\"");
        assertEquals(true, actual.contains(jmbag));
        assertEquals(true, actual.contains(lastName));
        assertEquals(true, actual.contains(firstName));
        assertEquals(true, actual.contains(finalGrade));
    }

    @Test
    public void testOperatorNotEqual() {
        String actual = getActual("query  jmbag!=\"0000000001\"");

        /* Total number of students in database is 63.
         * When the OperatorNotEqual is used on one JMBAG,
         * the expected number of students will be 62.
         * I added 2 extra rows because of printing table borders. */
        assertEquals(62+2, countOccurrencesOf(actual, '\n'));
    }

    @Test
    public void testOperatorLike1() {
        List<String> jmbagList = new ArrayList<>();
        jmbagList.add("0000000002");
        jmbagList.add("0000000003");
        jmbagList.add("0000000004");
        jmbagList.add("0000000005");

        String actual = getActual("query lastName LIKE \"B*\"");

        for (String jmbag : jmbagList) {
            assertEquals(true, actual.contains(jmbag));
        }
    }

    @Test
    public void testOperatorLike2() {
        String expected = "Records selected: 0";
        String actual = getActual("query lastName LIKE \"Be*\"");

        assertEquals(expected, actual);
    }


    /**
     * Returns a trimmed output of the main program {@linkplain StudentDB} for
     * the input query as specified by the given <tt>input</tt>.
     * <p>
     * The input query may or may not contain a line with an <tt>exit</tt>
     * command specified as this method concatenates a <tt>\nexit</tt> to the
     * end of the input.
     *
     * @param input input from which query is read
     * @return an output of the main program for the input query in file
     */
    private static String getActual(String input) {
        input += "\nexit";
        try {
            InputStream in = new ByteArrayInputStream(input.getBytes());

            System.setIn(in);


            StudentDB.main(null);
            return StudentDB.getOutput().trim();
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException: " + e.getMessage());
        }
    }

    /**
     * Counts the number of occurrences of the specified char in a string.
     */
    private static int countOccurrencesOf(String string, char c) {
        return string.length() - string.replace(c + "", "").length();
    }

}
