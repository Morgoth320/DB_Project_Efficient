import java.util.*;

public class Table {
    private Map<String, String> keyMap;
    private Map<String, TreeMap> indexes;

    public Table(){
        indexes = new HashMap<>();
        keyMap = new HashMap<>();
    }

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

    public List<String> getByIndex(String fieldName, Object value){
        List<String> toReturn = null;
        switch (keyMap.get(fieldName)){
            case "bool":
                {
                    TreeMap<Boolean, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "String":
                {
                    TreeMap<String, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "int":
                {
                    TreeMap<Integer , List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "double":
                {
                    TreeMap<Double , List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
            case "date":
                {
                    TreeMap<Date, List<String>> map = indexes.get(fieldName);
                    toReturn = map.get(value);
                }
                break;
        }
        return toReturn;
    }

    public void insertWithIndex(String fieldName, Object key, String value){
        TreeMap map = indexes.get(fieldName);
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

    public Set getKeys(String fieldName){
        TreeMap map = indexes.get(fieldName);
        Set s = map.keySet();
        return s;
    }

    public Map<String, String> getKeyMap(){
        return keyMap;
    }

    public static void main(String... args){
        Table t = new Table();
        t.createIndex("names", "String");
        t.createIndex("single", "bool");
        t.createIndex("salary", "double");
        t.insertWithIndex("names", "Satan", "Info");
        t.insertWithIndex("names", "Satan", "Other info");
        t.insertWithIndex("names", "Satan", "Other second info");
        t.insertWithIndex("names", "Satan", "Other third info");
        t.insertWithIndex("single", true, "Is single");
        t.insertWithIndex("single", true, "Is second single");
        t.insertWithIndex("single", true, "Is third single");
        t.insertWithIndex("single", true, "Is fourth single");
        /*List<String> completeInfo = t.getByIndex("single", true);
        for(int i = 0; i < completeInfo.size(); i++){
            System.out.println(completeInfo.get(i));
        }*/
        Set<String> set = t.getKeys("names");
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }
}
