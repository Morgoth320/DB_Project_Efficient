import java.text.SimpleDateFormat;
import java.util.*;

public class QueryProcessor {
    private Table dataTable;
    private String[] fieldNames;
    private String[] typeNames;

    private SimpleDateFormat format;

    public QueryProcessor(String path){
        DataLoader dataLoader = new DataLoader(path);
        this.dataTable = dataLoader.getDataWithTable();
        this.format = new SimpleDateFormat("dd/MM/yyyy");
        this.fieldNames = this.getFieldNames();
        this.typeNames = this.getTypeNames();
    }

    /**
     * Calls the method that will process a simple query
     * @param query Query Data Type built with the user's requirements
     * @return String that contains the query's answer
     */

    public String processSimpleQuery(Query query){
        return this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getOperationValue());
    }

    /**
     * Calls all the methods that make the comparisons between the data file and the user's input
     * @param comparisonColumn int containing the datafile's column to be compared
     * @param operation String containing the operation (<, >, <=, >=< =)
     * @param operationValue String with the value to be compared
     * @return String containing the query's result
     */

    private String processSimpleQuery(int comparisonColumn, String operation, String operationValue){
        String queryResult = "";
        switch(typeNames[comparisonColumn]){
            case "String":
                queryResult = this.compareStrings(operationValue, comparisonColumn);
                break;
            case "bool":
                queryResult = this.compareBooleans(Boolean.parseBoolean(operationValue), comparisonColumn);
                break;
            case "int":
                queryResult = this.compareIntegers(operation, Integer.parseInt(operationValue), comparisonColumn);
                break;
            case "double":
                queryResult = this.compareDoubles(operation, Double.parseDouble(operationValue), comparisonColumn);
                break;
            case "date":
                try{
                    queryResult = this.compareDates(operation, format.parse(operationValue), comparisonColumn);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return queryResult;
    }

    /**
     * Generates the complex query's answer, using two simple queries and a logical operator
     * @param query ComplexQuery Data Tyoe containing the user's requirements
     * @return String with a complex query's answer
     */

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
                    if(firstResult.equals("No matches were found") || secondResult.equals("No matches were found")) {
                        finalResult = "No matches were found";
                    }else {
                        for (int i = 0; i < firstResultValues.length; ++i) {
                            for (int j = 0; j < secondResultValues.length; ++j) {
                                if (firstResultValues[i].equals(secondResultValues[j]) && !finalResult.contains(firstResultValues[i])) {
                                    finalResult += firstResultValues[i] + "\n";
                                }
                            }
                        }
                    }
                    break;
                case OR:
                    if(!firstResult.equals("No matches were found")) {
                        for (int i = 0; i < firstResultValues.length; ++i) {
                            finalResult += firstResultValues[i] + "\n";
                        }
                    }

                    if(!secondResult.equals("No matches were found")) {
                        for (int j = 0; j < secondResultValues.length; ++j) {
                            if (!finalResult.contains(secondResultValues[j])) {
                                finalResult += secondResultValues[j] + "\n";
                            }
                        }
                    }
                    break;
            }
        }
        return finalResult;
    }

    private String compareIntegers(String comparison, int toCompare, int column){
        String result = "";
        Set<Integer> keySet = dataTable.getKeys(fieldNames[column]);
        int currentKey = 0;
        Iterator<Integer> keyIterator = keySet.iterator();
        switch (comparison){
            case "=":
                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], toCompare));
                break;
            case ">": {

                while (keyIterator.hasNext() && currentKey <= toCompare)
                    currentKey = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                while (keyIterator.hasNext()) {
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }
                break;
            case "<":{
                while(keyIterator.hasNext() && currentKey < toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey < toCompare)
                        result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                }
            }
                break;
            case ">=": {
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                while (keyIterator.hasNext()) {
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }
                break;
            case "<=":{
                while(keyIterator.hasNext() && currentKey <= toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey <= toCompare)
                        result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                }
            }
                break;
        }
        return result;
    }

    private String compareDoubles(String comparison, double toCompare, int column){
        String result = "";
        Set<Double> keySet = dataTable.getKeys(fieldNames[column]);
        double currentKey = 0;
        Iterator<Double> keyIterator = keySet.iterator();
        switch (comparison){
            case "=":
                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], toCompare));
                break;
            case ">":{
                while (keyIterator.hasNext() && currentKey <= toCompare)
                    currentKey = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                while (keyIterator.hasNext()) {
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }

                break;
            case "<":{
                while(keyIterator.hasNext() && currentKey < toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey < toCompare)
                        result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                }
            }
                break;
            case ">=":{
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                while (keyIterator.hasNext()) {
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }
                break;
            case "<=":{
                while(keyIterator.hasNext() && currentKey <= toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey <= toCompare)
                        result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey));
                }
            }
                break;
        }
        return result;
    }

    private String compareDates(String comparison, Date toCompare, int column){
        String result = "";
        Set<Date> keySet = dataTable.getKeys(fieldNames[column]);
        Iterator<Date> keyIterator = keySet.iterator();
        Date currentDate = null;

        switch (comparison){
            case "=":
                result = this.getResults(dataTable.getByIndex(fieldNames[column], toCompare));
                break;
            case ">": {
                while(keyIterator.hasNext() && currentDate.compareTo(toCompare) <= 0)
                    currentDate = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate));
                while(keyIterator.hasNext()){
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }
                break;
            case "<":{
                while (keyIterator.hasNext() && currentDate.compareTo(toCompare) < 0){
                    currentDate = keyIterator.next();
                    if(currentDate.compareTo(toCompare) < 0)
                        result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate));

                }
            }
                break;
            case ">=": {
                while(keyIterator.hasNext() && currentDate.compareTo(toCompare) < 0)
                    currentDate = keyIterator.next();

                result = this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate));
                while(keyIterator.hasNext()){
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next()));
                }
            }
                break;
            case "<=":while (keyIterator.hasNext() && currentDate.compareTo(toCompare) <= 0){
                currentDate = keyIterator.next();
                if(currentDate.compareTo(toCompare) <= 0)
                    result += this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate));

            }
                break;
        }
        return result;
    }

    private String compareStrings(String toCompare, int column){
        return this.getResults(dataTable.getByIndex(fieldNames[column], toCompare));
    }

    private String compareBooleans(boolean toCompare, int column){
        return this.getResults(dataTable.getByIndex(fieldNames[column], toCompare));
    }

    private String getResults(List<String> results){
        String result = "";
        if(results != null) {
            for (int i = 0; i < results.size(); i++)
                result += results.get(i) + "\n";

        }else
            result = "No matches were found";


        return result;
    }

    private String[] getFieldNames(){
        Set<String> fields = dataTable.getKeyMap().keySet();
        String[] returnFields = new String[fields.size()];
        int counter = 0;
        Iterator<String> fieldIterator = fields.iterator();
        while(fieldIterator.hasNext()){
            returnFields[counter] = fieldIterator.next();
            counter++;
        }
        return  returnFields;
    }

    private String[] getTypeNames(){
        Set<Map.Entry<String, String>> fieldTypes = dataTable.getKeyMap().entrySet();
        String[] returnTypes = new String[fieldTypes.size()];
        int counter = 0;
        Iterator<Map.Entry<String, String>> entryIterator = fieldTypes.iterator();
        while(entryIterator.hasNext()){
            Map.Entry<String, String> currentEntry = entryIterator.next();
            returnTypes[counter] = currentEntry.getValue();
            counter++;
        }
        return returnTypes;
    }

    public String[] getPublicFieldNames(){
        return this.fieldNames;
    }

    public String[] getPublicTypeNames(){
        return this.typeNames;
    }
}
