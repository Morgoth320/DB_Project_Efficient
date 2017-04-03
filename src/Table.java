import java.util.*;

public class Table {
    private Map<String, String> keyMap;
    private Map<String, Map> indexes;

    public Table(){
        indexes = new HashMap<>();
        keyMap = new HashMap<>();
    }

    /**
     * Creates a new index for a Table column. First it puts the field name and it's class in the
     * keyMap, and then it creates an entry for that field by mapping the field name to a new map
     * @param fieldName name of the new field
     * @param fieldClass object class of the values of the field
     */
    public void createIndex(String fieldName, String fieldClass){
        keyMap.put(fieldName, fieldClass);
        switch(fieldClass){
            case "bool":
                indexes.put(fieldName, new TreeMap<Boolean, List<String>>());
                break;
            case "String":
                indexes.put(fieldName, new TreeMap<String, List<String>>());
                break;
            case "int":
                indexes.put(fieldName, new TreeMap<Integer, List<String>>());
                break;
            case "double":
                indexes.put(fieldName, new TreeMap<Double, List<String>>());
                break;
            case "date":
                indexes.put(fieldName, new TreeMap<Date, List<String>>());
                break;
        }
    }

    /**
     * Returns a list of Strings that contains all the entries associated to the value passed as parameter.
     * It does so by getting first, a map associated with the field name, then, in that map, getting the list
     * mapped to the value passed as parameter
     * @param fieldName name of the field being queried
     * @param value value that we're looking for in the table
     * @return a list of Strings that contains all the entries associated to the value passed as parameter
     */
    public List<String> getByIndex(String fieldName, Object value){
        List<String> toReturn = null;
        switch (keyMap.get(fieldName)){
            case "bool":
                {
                    Map<Boolean, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "String":
                {
                    Map<String, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "int":
                {
                    Map<Integer , List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "double":
                {
                    Map<Double , List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "date":
                {
                    Map<Date, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
        }
        return toReturn;
    }

    /**
     * Inserts an entry into the table. First the map associated to a field name is retrieved, then, if there
     * already is one or more entries mapped to the value passed, then the list of them is obtained and then
     * the new entry inserted in that list. If there aren't entries mapped to the value, it creates a new list
     * mapped to the value and then the entry is inserted.
     * @param fieldName name of the field that will contain the new entry
     * @param key object mapped to the new entry
     * @param value entry to insert in the table
     */
    public void insertWithIndex(String fieldName, Object key, String value){
        Map map = indexes.get(fieldName);
        switch(keyMap.get(fieldName)){
            case "bool": {
                if (map.get((boolean) key) == null)
                    map.put((boolean) key, new LinkedList<String>());

                List<String> list = (List<String>) map.get((boolean) key);
                list.add(value);
            }
                break;
            case "String": {
                if (map.get((String) key) == null)
                    map.put((String) key, new LinkedList<String>());

                List<String> list = (List<String>) map.get((String) key);
                list.add(value);
            }
                break;
            case "int": {
                if (map.get((int) key) == null)
                    map.put((int) key, new LinkedList<String>());

                List<String> list = (List<String>) map.get((int) key);
                list.add(value);
            }
                break;
            case "double":{
                if (map.get((double) key) == null)
                    map.put((double) key, new LinkedList<String>());

                List<String> list = (List<String>) map.get((double) key);
                list.add(value);
            }
                break;
            case "date":{
                if (map.get((Date) key) == null)
                    map.put((Date) key, new LinkedList<String>());

                List<String> list = (List<String>) map.get((Date) key);
                list.add(value);
            }
                break;
            }
    }

    /**
     * Returns a set of all the values associated to a single field. Used to get all the values mapped to entries
     * in order to do the less, greater, less or equal and greater or equal operations more efficiently, since with
     * this only values that already exist are being iterated over
     * @param fieldName name of the field
     * @return a set of all the values associated to a single field
     */
    public Set getKeys(String fieldName){
        Map map = indexes.get(fieldName);
        Set s = map.keySet();
        return s;
    }

    /**
     * Returns the map that associates the names of the fields to their classes. Used to get the names of the fields
     * in order to display them to the user
     * @return the map that associates the names of the fields to their classes
     */
    public Map<String, String> getKeyMap(){
        return keyMap;
    }
}
