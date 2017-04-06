import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DataLoader {
    private final String DATE_FORMAT = "dd/mm/yyyy";
    private final String GRINGO_DATE_FORMAT = "mm/dd/yyyy";
    private String path;
    private String regex;
    private BufferedReader reader;
    private SimpleDateFormat format;

    public DataLoader(String path){
        this.path = path;
        format = new SimpleDateFormat(GRINGO_DATE_FORMAT);
        try{
            reader = new BufferedReader(new FileReader(path));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Returns a table that contains the data inside the user's file
     * @return a table that contains the data inside the user's file
     */
    public Table getDataWithTable(){
        Table data = new Table();
        String[][] tempData = new String[this.getRowNumber()][this.getColumnNumber()];
        String[] entries = new String[tempData.length - 2];
        try{
            for(int i = 0; i < tempData.length; i++){
                tempData[i] = reader.readLine().split(regex);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        int counter = 0;
        for(int i = 2; i < tempData.length; i++){
            String entry = "";
            for(int j = 0; j < tempData[i].length; j++){
                entry += tempData[i][j] + " ";
            }
            entry = entry.trim();
            entries[counter] = entry;
            counter++;
        }
        for(int i = 0; i < tempData[0].length; i++){
            data.createIndex(tempData[0][i], tempData[1][i]);
        }

        counter = 0;
        for(int i = 2; i < tempData.length; i++){
            for(int j = 0; j < tempData[i].length; j++){
                switch(tempData[1][j]){
                    case "String":
                        data.insertWithIndex(tempData[0][j], tempData[i][j], entries[counter]);
                        break;
                    case "bool":
                        data.insertWithIndex(tempData[0][j], Boolean.parseBoolean(tempData[i][j]), entries[counter]);
                        break;
                    case "int":
                        data.insertWithIndex(tempData[0][j], Integer.parseInt(tempData[i][j]), entries[counter]);
                        break;
                    case "double":
                        data.insertWithIndex(tempData[0][j], Double.parseDouble(tempData[i][j]), entries[counter]);
                        break;
                    case "date":
                        try{
                            data.insertWithIndex(tempData[0][j], format.parse(tempData[i][j]), entries[counter]);
                        }catch(ParseException e){
                            e.printStackTrace();
                        }
                        break;
                }

            }
            counter++;
        }
        return data;
    }

    /**
     * Returns the number of data rows contained in the file
     * @return the number of data rows contained in the file
     */
    private int getRowNumber(){
        int rows = 0;
        try{
            BufferedReader tempReader = new BufferedReader(new FileReader(this.path));
            while(tempReader.readLine() != null){
                rows++;
            }
            tempReader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Returns the number of columns inside the user's file and sets the regex to separate them, since in some computers the
     * csv is separated by a ';' and in others by a ','
     * @return the number of data columns inside the user's file
     */
    private int getColumnNumber(){
        int columns = 0;
        try{
            BufferedReader tempReader = new BufferedReader(new FileReader(this.path));
            String firstLine = tempReader.readLine();
            if(firstLine.contains(";"))
                regex = ";";

            else
                regex = ",";

            columns = tempReader.readLine().split(regex).length;
            tempReader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return columns;
    }
}
