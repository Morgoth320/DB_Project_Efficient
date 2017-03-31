public class Query {
    private String operation;
    private String operationValue;
    private int comparisonColumn;
    private String lowerLimit;
    private String upperLimit;

    public Query(String operation, String operationValue, int comparisonColumn){
        this.operation = operation;
        this.operationValue = operationValue;
        this.comparisonColumn = comparisonColumn;
    }

    public Query(String operation, String lowerLimit, int comparisonColumn, String upperLimit){
        this.operation = operation;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.comparisonColumn = comparisonColumn;
    }

    /**
     * Returns the query's operation
     * @return the query's operation
     */
    public String getOperation() {
        return this.operation;
    }

    /**
     * Returns the value to be compared
     * @return the value to be compared
     */
    public String getOperationValue() {
        return this.operationValue;
    }

    /**
     * Returns the data file's column to be compared
     * @return the data file's column to be compared
     */
    public int getComparisonColumn() {
        return this.comparisonColumn;
    }

    /**
     * Returns the lower limit if the comparison is a range
     * @return the lower limit if the comparison is a range
     */
    public String getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Returns the upper limit if the comparison is a range
     * @return the upper limit if the comparison is a range
     */
    public String getUpperLimit() {
        return upperLimit;
    }
}
