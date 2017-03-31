import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    private QueryProcessor queryProcessor;
    private String[] fields;
    private String[] types;
    private int fieldNumber;

    /**
     * It starts the program, asks the file path and calls the
     * methods required to process the query, if the path is right
     */

    public void initialize(){
        String path = "";
        while(path.equals("")){
            System.out.println("Enter the path of the data file");
            Scanner scanner = new Scanner(System.in);
            path = scanner.nextLine();
            System.out.println(path + "\nIs this path correct?\n1- Yes\n2- No");
            int correct = Integer.parseInt(this.validateInput("(^[1-2]${1})"));
            if(correct == 1){
                queryProcessor = new QueryProcessor(path);
                this.fields = queryProcessor.getPublicFieldNames();
                this.types = queryProcessor.getPublicTypeNames();
                this.fieldNumber = fields.length;
                this.queryTypeChoice();
            }else{
                path = "";
            }
        }
    }

    /**
     * Asks the user what kind of query they want to do and calls the methods required
     * to process the queries depending on the user's choice, then shows the result
     */

    public void queryTypeChoice(){
        int choice = 0;
        while(choice < 3) {
            printFields();
            System.out.println("Enter the type of query to be made\n1- Simple Query\n2- Complex Query\n3- Exit");
            choice = Integer.parseInt(this.validateInput("(^[1-3]${1})"));
            switch (choice) {
                case 1:
                    Query query = makeSimpleQuery();
                    System.out.println();
                    System.out.println(this.queryProcessor.processSimpleQuery(query));
                    System.out.println();
                    break;
                case 2:
                    ComplexQuery complexQuery = makeComplexQuery();
                    System.out.println();
                    System.out.println(this.queryProcessor.processComplexQuery(complexQuery));
                    System.out.println();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Asks the user wich field they want to use to make the query, calls the method that asks for the operation
     * and then wich value they want to compare, it also builds a Query type object with the information
     *  @return Query Data Type
     */

    public Query makeSimpleQuery(){
        String operation = "";
        this.printFields();
        System.out.println("Choose field to make the comparison");
        int choice = this.fieldNumber + 1;
        while(choice > this.fieldNumber){
            choice = Integer.parseInt(this.validateInput("^[1-9]+$"));
        }
        String fieldType = types[choice - 1];
        String parameter = "";
        String lowerLimit = "";
        String upperLimit = "";

        switch (fieldType) {
            case "String":
                System.out.println("The only available operation for " + fields[choice - 1] + " is equality");
                operation = "=";
                System.out.println("Chose the value to be compared");
                parameter = this.validateInput(".*");
                break;

            case "bool":
                System.out.println("The only available operation for " + fields[choice - 1] + " is equality");
                operation = "=";
                System.out.println("Chose the value to be compared");
                parameter = this.validateInput("(^true$|^false$)");
                break;

            case "int":
                System.out.println("Available operations for " + fields[choice - 1] + " :");
                operation = this.getOperation();
                if(operation.equals("range")){
                    System.out.println("Set the range's lower limit");
                    lowerLimit = this.validateInput("^[0-9]+$");
                    System.out.println("Set the range's upper limit");
                    upperLimit = this.validateInput("^[0-9]+$");
                }else {
                    System.out.println("Chose the value to be compared");
                    parameter = this.validateInput("^[0-9]+$");
                }
                break;

            case "double":
                System.out.println("Available operations for " + fields[choice - 1] + " :");
                operation = this.getOperation();
                if(operation.equals("range")){
                    System.out.println("Set the range's lower limit");
                    lowerLimit = this.validateInput("^([0-9]|\\.)+$");
                    System.out.println("Set the range's upper limit");
                    upperLimit = this.validateInput("([0-9]|\\.)+");
                }else {
                    System.out.println("Chose the value to be compared");
                    parameter = this.validateInput("^([0-9]|\\.)+$");
                }
                break;

            case "date":
                System.out.println("Available operations for " + fields[choice - 1] + " :");
                operation = this.getOperation();
                if(operation.equals("range")) {
                    System.out.println("Set the range's lower limit");
                    lowerLimit = this.validateInput("^(([1-9]|1[0-9]|2[0-9]|3[0-1])/([1-9]|1[0-2])/[0-9]{4})$");
                    System.out.println("Set the range's upper limit");
                    upperLimit = this.validateInput("^(([1-9]|1[0-9]|2[0-9]|3[0-1])/([1-9]|1[0-2])/[0-9]{4})$");
                }else {
                    System.out.println("Chose the value to be compared");
                    parameter = this.validateInput("^(([1-9]|1[0-9]|2[0-9]|3[0-1])/([1-9]|1[0-2])/[0-9]{4}$)");
                }
                break;
        }
        Query newQuery = null;
        if(operation.equals("range")){
            newQuery = new Query(operation, lowerLimit, choice - 1, upperLimit);
        }else{
            newQuery = new Query(operation, parameter, choice - 1);
        }
        return newQuery;
    }

    /**
     * Porcess a simple query calling the makeSimpleQuery method, also asks the user what kind
     * of logical operator they wnat to use to make the complex query, and then process another simple query.
     * Builds a ComplexQuery type object with the information inserted by the user
     * @return ComplexQuery Data Type
     */

    public ComplexQuery makeComplexQuery(){
        Query firstQuery = this.makeSimpleQuery();
        LogicalOperator logicalOperator = null;
        System.out.println("Select the logical operator to bind the queries\n1- And\n2- Or");
        int operator = Integer.parseInt(this.validateInput("^[1-2]$"));
        switch (operator) {
            case 1:
                logicalOperator = LogicalOperator.AND;
                break;
            case 2:
                logicalOperator = LogicalOperator.OR;
                break;
        }

        Query secondQuery = this.makeSimpleQuery();
        return new ComplexQuery(firstQuery, secondQuery, logicalOperator);
    }

    /**
     * Asks the user the operation type (<, >, =, <=, >=)
     * @return String, user's choice
     */

    public String getOperation(){
        String operation = "";
        System.out.println("1- Equals (=)\n2- Greater than (>)\n3- Lesser than (<)\n4- Greater of equal than (>=)\n5- Lesser or equal than (<=)\n6- A range between two values");
        int choice = Integer.parseInt(this.validateInput("(^[1-6]$)"));
        switch (choice) {
            case 1:
                operation = "=";
                break;
            case 2:
                operation = ">";
                break;
            case 3:
                operation = "<";
                break;
            case 4:
                operation = ">=";
                break;
            case 5:
                operation = "<=";
                break;
            case 6:
                operation = "range";
                break;
        }
        return operation;
    }

    /**
     * Doesn't allow the user to enter wrong input, shows a message when it happens, let the user try again
     * It is used by all the methods that asks the user for information.
     * @param regex String that indicates wich input the user can make
     * @return String that contains the user's answer after it passes the validation process
     */

    private String validateInput(String regex){
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile(regex);
        String strChoice = scanner.nextLine();
        Matcher matcher = pattern.matcher(strChoice);
        while(!matcher.find()){
            System.out.println("Invalid Input");
            strChoice = scanner.nextLine();
            matcher = pattern.matcher(strChoice);
        }
        return strChoice;
    }

    /**
     * Shows the fields the user can use to make the query
     */

    public void printFields(){
        System.out.println("Fields:");
        for(int i = 0; i < fields.length; i++)
            System.out.println((i + 1) + "- " + fields[i]);
    }
}
