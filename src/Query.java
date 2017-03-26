public class Query {
    private String operation;
    private String operationValue;
    private int comparisonColumn;

    public Query(String operation, String operationValue, int comparisonColumn){
        this.operation = operation;
        this.operationValue = operationValue;
        this.comparisonColumn = comparisonColumn;
    }

    /**
     * Returns the query's operation
     * @return String
     */
    public String getOperation() {
        return this.operation;
    }

    /**
     * Returns the value to be compared
     * @return String
     */
    public String getOperationValue() {
        return this.operationValue;
    }

    /**
     * Returns the data file's column to be compared
     * @return int
     */
    public int getComparisonColumn() {
        return this.comparisonColumn;
    }
}
