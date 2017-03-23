import java.util.Scanner;

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
            int correct = 0;
            System.out.println(path + "\nIs this path correct?\n1- Yes\nAny other number- No");
            correct = scanner.nextInt();
            if(correct == 1){
                queryProcessor = new QueryProcessor(path);
                //this.fields = queryProcessor.getFieldNames();
                //this.types = queryProcessor.getTypeNames();
                this.fieldNumber = fields.length;
                this.queryTypeChoice();
            }else{
                path = "";
            }
        }
    }

    public void queryTypeChoice(){
        int choice = 0;
        while(choice < 3){
            printFields();
            System.out.println("Enter the type of query to be made\n1- Simple Query\n2- Complex Query\nAny other number- Exit");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch(choice){
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
        int choice = 0;
        while(choice == 0){
            this.printFields();
            System.out.println("Choose field to make the comparison");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            if((choice - 1) > fieldNumber || (choice - 1) < 0){
                System.out.println("The number entered is not valid");
                choice = 0;
                continue;
            }
            String fieldType = types[choice - 1];
            switch(fieldType){
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
                default:
                    choice = 0;
                    break;
            }
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
        int operator = 0;
        while(operator == 0 && logicalOperator == null){
            System.out.println("Select the logical operator to bind the queries\n1- And\n2- Or");
            Scanner scanner = new Scanner(System.in);
            operator = scanner.nextInt() - 1;
            if(operator < 0 || operator > 1){
                System.out.println("Invalid parameter passed");
                operator = 0;
                continue;
            }
            switch(operator){
                case 0:
                    logicalOperator = LogicalOperator.AND;
                    break;
                case 1:
                    logicalOperator = LogicalOperator.OR;
                    break;
            }
        }
        Query secondQuery = this.makeSimpleQuery();
        return new ComplexQuery(firstQuery, secondQuery, logicalOperator);
    }

    public String getOperation(){
        int choice = 0;
        String operation = "";
        System.out.println("1- Equals (=)\n2- Greater than (>)\n3- Lesser than (<)\n4- Greater of equal than (>=)\n5- Lesser or equal than (<=)");
        while(operation.equals("")){
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch(choice){
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
                default:
                    System.out.println("The number chosen is not valid");
                    break;
            }
        }
        return operation;
    }

    public void printFields(){
        System.out.println("Fields:");
        for(int i = 0; i < fields.length; i++)
            System.out.println((i + 1) + "- " + fields[i]);
    }
}
