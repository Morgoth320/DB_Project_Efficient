public class ComplexQuery extends Query {
    private String secondOperation;
    private String secondOperationValue;
    private int secondComparisonColumn;
    private LogicalOperator logicalOperator;

    public ComplexQuery(String operation, String operationValue, int comparisonColumn, String secondOperation, String secondOperationValue, LogicalOperator logicalOperator, int secondComparisonColumn){
        super(operation, operationValue, comparisonColumn);
        this.secondOperation = secondOperation;
        this.secondOperationValue = secondOperationValue;
        this.logicalOperator = logicalOperator;
        this.secondComparisonColumn = secondComparisonColumn;
    }

    public ComplexQuery(Query firstQuery, Query secondQuery, LogicalOperator logicalOperator){
        super(firstQuery.getOperation(), firstQuery.getOperationValue(), firstQuery.getComparisonColumn());
        this.secondOperation = secondQuery.getOperation();
        this.secondOperationValue = secondQuery.getOperationValue();
        this.secondComparisonColumn = secondQuery.getComparisonColumn();
        this.logicalOperator = logicalOperator;
    }

    /**
     * Returns the second query's operation
     * @return String
     */

    public String getSecondOperation() {
        return secondOperation;
    }

    /**
     * Returns the second simple query's value
     * @return String
     */

    public String getSecondOperationValue() {
        return secondOperationValue;
    }

    /**
     * Returns the logical operator to e used
     * @return Tipo LogicalOperator
     */

    public LogicalOperator getLogicalOperator()

    /**
     * Returns the data file's column to be compared in the second simple query
     * @return int
     */

    public int getSecondComparisonColumn() {
        return secondComparisonColumn;
    }
}
