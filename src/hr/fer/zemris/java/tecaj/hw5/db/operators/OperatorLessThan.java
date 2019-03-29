package hr.fer.zemris.java.tecaj.hw5.db.operators;

/**
 * Implements the {@linkplain IComparisonOperator} to form a
 * <tt>less than</tt> operator that compares strings lexicographically.
 *
 * @author Mario Bobic
 */
public class OperatorLessThan implements IComparisonOperator {

    /**
     * Returns true if <tt>value1</tt> is lexicographically less than
     * <tt>value2</tt>.
     */
    @Override
    public boolean satisfied(String value1, String value2) {
        return HR_COLLATOR.compare(value1, value2) < 0;
    }

}
