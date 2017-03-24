import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI {
    QueryProcessor queryProcessor;
    String[] fields;
    String[] types;
    int fieldNumber;

    public void initialize(){
        String path = "";
        while(path.equals("")){
            System.out.println("Enter the path of the data file");
            Scanner scanner = new Scanner(System.in);
            path = scanner.nextLine();
            System.out.println(path + "\nIs this path correct?\n1- Yes\n2- No");
            int correct = Integer.parseInt(this.validateInput("[1-2]"));
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

    public void queryTypeChoice(){
        int choice = 0;
        while(choice < 3) {
            printFields();
            System.out.println("Enter the type of query to be made\n1- Simple Query\n2- Complex Query\n3- Exit");
            choice = Integer.parseInt(this.validateInput("[1-3]"));
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

    public Query makeSimpleQuery(){
        String operation = "";
        this.printFields();
        System.out.println("Choose field to make the comparison");
        int choice = Integer.parseInt(this.validateInput("[1-" + fieldNumber+ "]"));
        String fieldType = types[choice - 1];
        switch (fieldType) {
            case "String":
            case "bool":
                System.out.println("The only available operation for " + fields[choice - 1] + " is equality");
                operation = "=";
                break;

            case "int":
            case "double":
            case "date":
                System.out.println("Available operations for " + fields[choice - 1] + " :");
                operation = this.getOperation();
                break;
        }
        String parameter = "";
        System.out.println("Chose the value to be compared");
        Scanner scanner = new Scanner(System.in);
        parameter = scanner.nextLine();
        return new Query(operation, parameter, choice - 1);
    }

    public ComplexQuery makeComplexQuery(){
        Query firstQuery = this.makeSimpleQuery();
        LogicalOperator logicalOperator = null;
        System.out.println("Select the logical operator to bind the queries\n1- And\n2- Or");
        int operator = Integer.parseInt(this.validateInput("[1-2]"));
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

    public String getOperation(){
        String operation = "";
        System.out.println("1- Equals (=)\n2- Greater than (>)\n3- Lesser than (<)\n4- Greater of equal than (>=)\n5- Lesser or equal than (<=)");
        int choice = Integer.parseInt(this.validateInput("[1-5]"));
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
        }
        return operation;
    }

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

    public void printFields(){
        System.out.println("Fields:");
        for(int i = 0; i < fields.length; i++)
            System.out.println((i + 1) + "- " + fields[i]);
    }
}
