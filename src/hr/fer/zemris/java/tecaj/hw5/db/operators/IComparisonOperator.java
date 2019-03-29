package hr.fer.zemris.java.tecaj.hw5.db.operators;

import java.text.Collator;
import java.util.Locale;

/**
 * An interface representing a comparison operator. Implementing classes must
 * override the <tt>satisfied</tt> method to provide a comparison result of the
 * operation they are doing.
 *
 * @author Mario Bobic
 * @see OperatorGreaterThan
 * @see OperatorLessThan
 * @see OperatorGreaterOrEqual
 * @see OperatorLessOrEqual
 * @see OperatorEqual
 * @see OperatorNotEqual
 * @see OperatorLike
 */
public interface IComparisonOperator {

    /**
     * A {@linkplain Collator} that is used to obtain a locale-sensitive String
     * comparison in Croatian.
     */
    public static Collator HR_COLLATOR = Collator.getInstance(new Locale("hr", "HR"));

    /**
     * Returns true if the comparison result of the operation the implementing
     * class is doing is satisfied. False otherwise.
     *
     * @param value1 a value
     * @param value2 a value
     * @return the comparison result
     */
    public boolean satisfied(String value1, String value2);

}
