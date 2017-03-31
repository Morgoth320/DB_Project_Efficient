public class ComplexQuery extends Query {
    private String secondOperation;
    private String secondOperationValue;
    private int secondComparisonColumn;
    private LogicalOperator logicalOperator;

    public ComplexQuery(Query firstQuery, Query secondQuery, LogicalOperator logicalOperator){
        super(firstQuery.getOperation(), firstQuery.getOperationValue(), firstQuery.getComparisonColumn());
        this.secondOperation = secondQuery.getOperation();
        this.secondOperationValue = secondQuery.getOperationValue();
        this.secondComparisonColumn = secondQuery.getComparisonColumn();
        this.logicalOperator = logicalOperator;
    }

    /**
     * Returns the second query's operation
     * @return the second query's operation
     */
    public String getSecondOperation() {
        return this.secondOperation;
    }

    /**
     * Returns the second query's value
     * @return the second query's value
     */
    public String getSecondOperationValue() {
        return this.secondOperationValue;
    }

    /**
     * Returns the logical operator to be used
     * @return the logical operator to be used
     */
    public LogicalOperator getLogicalOperator(){
        return this.logicalOperator;
    }

    /**
     * Returns the data file's column to be compared in the second simple query
     * @return the data file's column to be compared in the second simple query
     */
    public int getSecondComparisonColumn() {
        return this.secondComparisonColumn;
    }
}
