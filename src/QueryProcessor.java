import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryProcessor {
    private Table dataTable;
    private SimpleDateFormat format;

    public QueryProcessor(String path){
        DataLoader dataLoader = new DataLoader(path);
        this.dataTable = dataLoader.getDataWithTable();
        this.format = new SimpleDateFormat("dd/MM/yyyy");
    }

    public String processSimpleQuery(Query query){
        return this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getOperationValue());
    }

    private String processSimpleQuery(int comparisonColumn, String operation, String operationValue){
        String queryResult = "";

        return queryResult;
    }

    public String processComplexQuery(ComplexQuery query){
        String finalResult = "";
        String firstResult = this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getOperationValue());
        String[] firstResultValues = firstResult.split("\n");
        String secondResult = this.processSimpleQuery(query.getSecondComparisonColumn(), query.getSecondOperation(), query.getSecondOperationValue());
        String[] secondResultValues = secondResult.split("\n");
        if(firstResult.equals("No matches were found") && secondResult.equals("No matches were found")) {
            finalResult = firstResult;
        }else {
            switch (query.getLogicalOperator()) {
                case AND:

                    break;
                case OR:

                    break;
            }
        }
        return finalResult;
    }

    private String compareIntegers(String comparison, int toCompare, int column){
        switch (comparison){
            case "=":

                break;
            case ">":

                break;
            case "<":

                break;
            case ">=":

                break;
            case "<=":

                break;
        }
        return "";
    }

    private String compareDoubles(String comparison, double toCompare, int column){
        switch (comparison){
            case "=":

                break;
            case ">":

                break;
            case "<":

                break;
            case ">=":

                break;
            case "<=":

                break;
        }
        return "";
    }

    private String compareDates(String comparison, Date toCompare, int column){
        switch (comparison){
            case "=":

                break;
            case ">":

                break;
            case "<":

                break;
            case ">=":

                break;
            case "<=":

                break;
        }
        return "";
    }

    private String compareStrings(String toCompare, int column){
        return "";
    }

    private String compareBooleans(boolean toCompare, int column){
        return "";
    }

    private String getResults(int[] matchingRows, int numberOfMatchingRows){
        String result = "";
        return result;
    }
}
