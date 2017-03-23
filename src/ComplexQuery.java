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

    public String getSecondOperation() {
        return secondOperation;
    }

    public String getSecondOperationValue() {
        return secondOperationValue;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public int getSecondComparisonColumn() {
        return secondComparisonColumn;
    }
}
