public class Query {
    private String operation;
    private String operationValue;
    private int comparisonColumn;

    public Query(String operation, String operationValue, int comparisonColumn){
        this.operation = operation;
        this.operationValue = operationValue;
        this.comparisonColumn = comparisonColumn;
    }

    public String getOperation() {
        return operation;
    }

    public String getOperationValue() {
        return operationValue;
    }

    public int getComparisonColumn() {
        return comparisonColumn;
    }
}
