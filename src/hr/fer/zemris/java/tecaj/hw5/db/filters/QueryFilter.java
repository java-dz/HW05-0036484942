package hr.fer.zemris.java.tecaj.hw5.db.filters;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.ConditionalExpression;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;
import hr.fer.zemris.java.tecaj.hw5.db.operators.IComparisonOperator;
import hr.fer.zemris.java.tecaj.hw5.db.parser.QueryParser;

/**
 * This class is used for filtering out student records specified in a list of
 * query expressions.
 *
 * @author Mario Bobic
 */
public class QueryFilter implements IFilter {

    /** List of query expressions. */
    private List<ConditionalExpression> expressionList;

    /**
     * Constructs an instance of QueryFilter object with the specified query to
     * be parsed into an expression list.
     *
     * @param queryString string to be parsed into a list of expressions
     */
    public QueryFilter(String queryString) {
        expressionList = new QueryParser(queryString).getExpressionList();
    }

    @Override
    public boolean accepts(StudentRecord record) {
        return satisfiesAllExpressions(record);
    }

    /**
     * Returns true if the given <tt>record</tt> satisfies all query expressions
     * stored in a list that was returned as a result of parsing the expressions.
     *
     * @param record a student record
     * @return true if the specified record satisfies all query expressions
     */
    private boolean satisfiesAllExpressions(StudentRecord record) {
        for (ConditionalExpression expression : expressionList) {
            IComparisonOperator op = expression.getComparisonOperator();
            String fieldValue = expression.getFieldGetter().get(record);
            String stringLiteral = expression.getStringLiteral();

            if (!op.satisfied(fieldValue, stringLiteral)) {
                return false;
            }
        }

        return true;
    }

}
