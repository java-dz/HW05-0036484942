package hr.fer.zemris.java.tecaj.hw5.db;

import hr.fer.zemris.java.tecaj.hw5.db.fieldgetters.IFieldValueGetter;
import hr.fer.zemris.java.tecaj.hw5.db.operators.IComparisonOperator;

/**
 * A conditional expression represents an expression used by the
 * {@linkplain StudentDB} and its commands. The expression has a field value
 * getter on the left-hand side, a comparison operator and a string literal on
 * the right-hand side.
 *
 * @author Mario Bobic
 */
public class ConditionalExpression {

    /** A field value getter. */
    private IFieldValueGetter fieldGetter;
    /** A string literal. */
    private String stringLiteral;
    /** A comparison operator for comparing field and string. */
    private IComparisonOperator comparisonOperator;

    /**
     * Constructs a new conditional expression with the specified arguments.
     *
     * @param fieldGetter field value getter
     * @param stringLiteral string literal
     * @param comparisonOperator comparison operator for comparing field and string
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter,
            String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Returns a field value getter of this conditional expression.
     *
     * @return a field value getter of this conditional expression
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Returns a string literal of this conditional expression.
     *
     * @return a string literal of this conditional expression
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * Returns a comparison operator that is used for comparing the field and
     * string of this conditional expression.
     *
     * @return a comparison operator of this conditional expression
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

}
