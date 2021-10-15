package byow.Core;

import java.util.*;
import java.util.HashMap;


/** @Source https://www.geeksforgeeks.org/implementing-generic-graph-in-java/
 * https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
 * */
public class Graph<T> {
    // We use Hashmap to store the edges in the graph. Stores vertex -> other vertices.
    private Map<T, List<T>> map = new HashMap<>();

    // This function adds a new vertex to the graph
    public void addVertex(T s) {
        map.put(s, new LinkedList<T>());
    }

    // This function adds the edge
    // between source to destination
    public void addEdge(T source, T destination, boolean bidirectional) {
        if (this.getMap().containsKey(destination)
                && this.getMap().get(destination).contains(source)) {
            return;
        }
        if (source.equals(destination)) {
            return;
        }
        if (!map.containsKey(source)) {
            addVertex(source);
        }
        if (!map.containsKey(destination)) {
            addVertex(destination);
        }
        map.get(source).add(destination);
        if (bidirectional) {
            map.get(destination).add(source);
        }
    }

    public Map<T, List<T>> getMap() {
        return map;
    }

    // function to sort hashmap by values
    public static HashMap<ArrayList<Room>, Integer> sortByValue(HashMap<ArrayList<Room>,
            Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<ArrayList<Room>, Integer>> list =
                new LinkedList<Map.Entry<ArrayList<Room>, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<ArrayList<Room>, Integer>>() {
            public int compare(Map.Entry<ArrayList<Room>, Integer> o1,
                               Map.Entry<ArrayList<Room>, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<ArrayList<Room>, Integer> temp = new LinkedHashMap<ArrayList<Room>, Integer>();
        for (Map.Entry<ArrayList<Room>, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
