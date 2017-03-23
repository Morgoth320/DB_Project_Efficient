import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryProcessor {
    private Table dataTable;
    private String[][] data;
    private SimpleDateFormat format;

    public QueryProcessor(String path){
        DataLoader dataLoader = new DataLoader(path);
        this.dataTable = dataLoader.getDataWithTable();
        this.data = dataLoader.getData();
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
        int counter = 0;
        int[] matchingRows = new int[this.data.length];
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
        return this.getResults(matchingRows, counter);
    }

    private String compareDoubles(String comparison, double toCompare, int column){
        int counter = 0;
        int[] matchingRows = new int[this.data.length];
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
        return this.getResults(matchingRows, counter);
    }

    private String compareDates(String comparison, Date toCompare, int column){
        int counter = 0;
        int[] matchingRows = new int[this.data.length];
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
        return this.getResults(matchingRows, counter);
    }

    private String compareStrings(String toCompare, int column){
        int counter = 0;
        int[] matchingRows = new int[this.data.length];
        for(int i = 2; i < this.data.length; i++){
            if(toCompare.equals(this.data[i][column])){
                matchingRows[counter] = i;
                counter++;
            }
        }
        return "";
    }

    private String compareBooleans(boolean toCompare, int column){
        int counter = 0;
        int[] matchingRows = new int[this.data.length];
        for(int i = 2; i < this.data.length; i++){
            if(toCompare == Boolean.parseBoolean(this.data[i][column])){
                matchingRows[counter] = i;
                counter++;
            }
        }
        return "";
    }

    private String getResults(int[] matchingRows, int numberOfMatchingRows){
        String result = "";
        for(int i = 0; i < numberOfMatchingRows; i++){
            for(int j = 0; j < this.data[i].length; j++){
                result += this.data[matchingRows[i]][j] + " ";
            }
            result += "\n";
        }
        if(result.equals(""))
            result = "No matches were found";

        return result;
    }

    public String[] getFieldNames(){
        return this.data[0];
    }

    public String[] getTypeNames(){
        return this.data[1];
    }
}
