import java.text.SimpleDateFormat;
import java.util.*;

public class QueryProcessor {
    private final String DATE_FORMAT = "dd/mm/yyyy";
    private final String GRINGO_DATE_FORMAT = "mm/dd/yyyy";
    private Table dataTable;
    private String[] fieldNames;
    private String[] typeNames;

    private SimpleDateFormat format;

    public QueryProcessor(String path){
        DataLoader dataLoader = new DataLoader(path);
        this.dataTable = dataLoader.getDataWithTable();
        this.format = new SimpleDateFormat(GRINGO_DATE_FORMAT);
        this.fieldNames = this.getFieldNames();
        this.typeNames = this.getTypeNames();
    }

    /**
     * Calls the method that will process a simple query
     * @param query Query Data Type built with the user's requirements
     * @return String that contains the query's answer
     */
    public String processSimpleQuery(Query query){
        String toReturn = "";
        if(!query.getOperation().equals("range"))
            toReturn = this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getOperationValue(), "");
        else
            toReturn = this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getLowerLimit(), query.getUpperLimit());
        return toReturn;
    }

    /**
     * Calls all the methods that make the comparisons between the data file and the user's input
     * @param comparisonColumn int containing the datafile's column to be compared
     * @param operation String containing the operation (<, >, <=, >=< =)
     * @param operationValue String with the value to be compared
     * @return String containing the query's result
     */
    private String processSimpleQuery(int comparisonColumn, String operation, String operationValue, String otherOperationValue){
        String queryResult = "";
        switch(typeNames[comparisonColumn]){
            case "String":
                queryResult = this.compareStrings(operationValue, comparisonColumn);
                break;
            case "bool":
                queryResult = this.compareBooleans(Boolean.parseBoolean(operationValue), comparisonColumn);
                break;
            case "int":
                if(!operation.equals("range"))
                    queryResult = this.compareIntegers(operation, Integer.parseInt(operationValue), comparisonColumn, 0);
                else
                    queryResult = this.compareIntegers(operation, Integer.parseInt(operationValue), comparisonColumn, Integer.parseInt(otherOperationValue));

                break;
            case "double":
                if(!operation.equals("range"))
                    queryResult = this.compareDoubles(operation, Double.parseDouble(operationValue), comparisonColumn, 0);
                else
                    queryResult = this.compareDoubles(operation, Double.parseDouble(operationValue), comparisonColumn, Double.parseDouble(otherOperationValue));

                break;
            case "date":
                try{
                    if(!operation.equals("range"))
                        queryResult = this.compareDates(operation, format.parse(operationValue), comparisonColumn, null);
                    else
                        queryResult = this.compareDates(operation, format.parse(operationValue), comparisonColumn, format.parse(otherOperationValue));
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return queryResult;
    }

    /**
     * Generates the complex query's answer, using data equivalent to two simple queries and a logical operator
     * @param query ComplexQuery Data Type containing the user's requirements
     * @return String with a complex query's answer
     */
    public String processComplexQuery(ComplexQuery query){
        StringBuilder finalResult = new StringBuilder();
        String firstResult = this.processSimpleQuery(query.getComparisonColumn(), query.getOperation(), query.getOperationValue(), "");
        String secondResult = this.processSimpleQuery(query.getSecondComparisonColumn(), query.getSecondOperation(), query.getSecondOperationValue(), "");
        String[] firstResultValues = firstResult.split("\n");
        String[] secondResultValues = secondResult.split("\n");
        if(firstResult.equals("No matches were found") && secondResult.equals("No matches were found")) {
            finalResult.append(firstResult);
        }else {
            switch (query.getLogicalOperator()) {
                case AND:
                    if(firstResult.equals("No matches were found") || secondResult.equals("No matches were found")) {
                        finalResult.append("No matches were found");
                    }else {
                        for (int i = 0; i < firstResultValues.length; ++i) {
                            for (int j = 0; j < secondResultValues.length; ++j) {
                                if (firstResultValues[i].equals(secondResultValues[j]) && !finalResult.toString().contains(firstResultValues[i])) {
                                    finalResult.append(firstResultValues[i] + "\n");
                                }
                            }
                        }
                        if(finalResult.equals(""))
                            finalResult.append("No matches were found");
                    }
                    break;
                case OR:
                    if(!firstResult.equals("No matches were found")) {
                        for (int i = 0; i < firstResultValues.length; ++i) {
                            finalResult.append(firstResultValues[i] + "\n");
                        }
                    }

                    if(!secondResult.equals("No matches were found")) {
                        for (int j = 0; j < secondResultValues.length; ++j) {
                            if (!finalResult.toString().contains(secondResultValues[j])) {
                                finalResult.append(secondResultValues[j] + "\n");
                            }
                        }
                    }
                    break;
            }
        }
        return finalResult.toString();
    }

    /**
     * Makes the query comparison when the operands are Integers
     * @param operation operation to make the comparison with (=, <, >, >=, <=, range)
     * @param toCompare value inserted by the user to make the comparison, if the comparison is a range this is the lower limit
     * @param column column that holds the values to be compared with the user input
     * @param upperLimit in the case that the comparison is a ranged, this is the upper limit
     * @return a string with all the entries that matched the query
     */
    private String compareIntegers(String operation, int toCompare, int column, int upperLimit){
        StringBuilder result = new StringBuilder();
        Set<Integer> keySet = dataTable.getKeys(fieldNames[column]);
        int currentKey = 0;
        Iterator<Integer> keyIterator = keySet.iterator();
        switch (operation){
            case "=":
                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], toCompare)));
                break;
            case ">": {
                while (keyIterator.hasNext() && currentKey <= toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext()) {
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }
                break;
            case "<":{
                while(keyIterator.hasNext() && currentKey < toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey < toCompare)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
                break;
            case ">=": {
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext()) {
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }
                break;
            case "<=":{
                while(keyIterator.hasNext() && currentKey <= toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey <= toCompare)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
                break;
            case "range":{
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext() && currentKey <= upperLimit) {
                    currentKey = keyIterator.next();
                    if(currentKey <= upperLimit)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
            break;
        }
        return result.toString();
    }

    /**
     * Makes the query comparison when the operands are Doubles
     * @param operation operation to make the comparison with (=, <, >, >=, <=, range)
     * @param toCompare value inserted by the user to make the comparison, if the comparison is a range this is the lower limit
     * @param column column that holds the values to be compared with the user input
     * @param upperLimit in the case that the comparison is a ranged, this is the upper limit
     * @return a string with all the entries that matched the query
     */
    private String compareDoubles(String operation, double toCompare, int column, double upperLimit){
        StringBuilder result = new StringBuilder();
        Set<Double> keySet = dataTable.getKeys(fieldNames[column]);

        Iterator<Double> keyIterator = keySet.iterator();
        double currentKey = 0;
        switch (operation){
            case "=":
                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], toCompare)));
                break;
            case ">":{
                while (keyIterator.hasNext() && currentKey <= toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext()) {
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }

                break;
            case "<":{
                while(keyIterator.hasNext() && currentKey < toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey < toCompare)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
                break;
            case ">=":{
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext()) {
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }
                break;
            case "<=":{
                while(keyIterator.hasNext() && currentKey <= toCompare){
                    currentKey = keyIterator.next();
                    if(currentKey <= toCompare)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
                break;

            case "range":{
                while (keyIterator.hasNext() && currentKey < toCompare)
                    currentKey = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                while (keyIterator.hasNext() && currentKey <= upperLimit) {
                    currentKey = keyIterator.next();
                    if(currentKey <= upperLimit)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentKey)));
                }
            }
            break;
        }
        return result.toString();
    }

    /**
     * Makes the comparison when the operands are Dates
     * @param operation operation to make the comparison with (=, <, >, >=, <=, range)
     * @param toCompare value inserted by the user to make the comparison, if the comparison is a range this is the lower limit
     * @param column column that holds the values to be compared with the user input
     * @param upperLimit in the case that the comparison is a ranged, this is the upper limit
     * @return a string with all the entries that matched the query
     */
    private String compareDates(String operation, Date toCompare, int column, Date upperLimit){
        StringBuilder result = new StringBuilder();
        Set<Date> keySet = dataTable.getKeys(fieldNames[column]);
        Iterator<Date> keyIterator = keySet.iterator();
        Date currentDate = null;

        switch (operation){
            case "=":
                result.append(this.getResults(dataTable.getByIndex(fieldNames[column], toCompare)));
                break;
            case ">": {
                currentDate = keyIterator.next();

                while(keyIterator.hasNext() && currentDate.compareTo(toCompare) <= 0)
                    currentDate = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));
                while(keyIterator.hasNext()){
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }
                break;
            case "<":{
                currentDate = keyIterator.next();
                while (keyIterator.hasNext() && currentDate.compareTo(toCompare) < 0){
                    currentDate = keyIterator.next();
                    if(currentDate.compareTo(toCompare) < 0)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));

                }
            }
                break;
            case ">=": {
                currentDate = keyIterator.next();
                while(keyIterator.hasNext() && currentDate.compareTo(toCompare) < 0)
                    currentDate = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));
                while(keyIterator.hasNext()){
                    result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], keyIterator.next())));
                }
            }
                break;
            case "<=": {
                currentDate = keyIterator.next();
                while (keyIterator.hasNext() && currentDate.compareTo(toCompare) <= 0) {
                    currentDate = keyIterator.next();
                    if (currentDate.compareTo(toCompare) <= 0)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));

                }
            }
                break;

            case "range":{
                currentDate = keyIterator.next();
                while(keyIterator.hasNext() && currentDate.compareTo(toCompare) < 0)
                    currentDate = keyIterator.next();

                result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));
                while (keyIterator.hasNext() && currentDate.compareTo(upperLimit) <= 0) {
                    currentDate = keyIterator.next();
                    if (currentDate.compareTo(upperLimit) <= 0)
                        result.append(this.getResults(dataTable.getByIndex(this.fieldNames[column], currentDate)));

                }
            }
            break;
        }
        return result.toString();
    }

    /**
     * Makes the comparison when the operands are Strings, since the only available operation with Strings is
     * equality, it only gets the ones that match the one inserted by the user
     * @param toCompare value inserted by the user to make the comparison
     * @param column column that holds the values to be compared with the user input
     * @return a string with all the entries equal to the one inserted by the user
     */
    private String compareStrings(String toCompare, int column){
        return this.getResults(dataTable.getByIndex(fieldNames[column], toCompare));
    }

    /**
     * Makes the comparison when the operands are booleans, since the only available operation with booleans is
     * equality, it only gets the ones that match the one inserted by the user
     * @param toCompare value inserted by the user to make the comparison
     * @param column column that holds the values to be compared with the user input
     * @return s String with all the entries equal to the one inserted by the user
     */
    private String compareBooleans(boolean toCompare, int column){
        return this.getResults(dataTable.getByIndex(fieldNames[column], toCompare));
    }

    /**
     * Converts a list of Strings to a single String, used to display the results of the query
     * to the user in the UI. If the list is null, then there were no matches
     * @param results a list with the matches of a single query
     * @return a single String containing the values inside the list
     */
    private String getResults(List<String> results){
        StringBuilder sb = new StringBuilder();
        if(results != null) {
            for (int i = 0; i < results.size(); i++)
                sb.append(results.get(i) + "\n");

        }else
            sb.append("No matches were found");


        return sb.toString();
    }

    /**
     * Returns an array with the names of the fields inside the file, used to get them for display to the user
     * @return an array with the names of the fields inside the file
     */
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

    /**
     * Returns an array with the types of the fields inside the file, used to make castings to the user's input more easy
     * @return an array with the names of the fields inside the file
     */
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
