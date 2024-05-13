import java.io.*;
import java.util.Iterator;

/**
 * Class for a most anagrams finder
 * @author Anna Sun
 * @version 1.0 May 7, 2024
 */
public class MostAnagramsFinder {
    /**
     * Reads the dictionary line by line and inserts the corresponding
     * key-value pair into the map.
     * @param map the map used to store the words in the dictionary
     * @param path the String path for the dictionary provided by the user
     */
    public static void solve(MyMap<String, MyList<String>> map, String path) {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            //iterate through the text file line by line
            while((line = reader.readLine()) != null) {
                //the key in a char array
                char[] ch = line.toLowerCase().toCharArray();
                ch = insertionSort(ch);
                String key = String.valueOf(ch);
                MyList<String> values = map.get(key);

                //the key is a new entry
                if(values == null) {
                    values = new MyLinkedList<>();
                    values.add(line);
                    map.put(key, values);
                } else {
                    values.add(line);
                }
            }
        } catch(IOException ioe) {
            System.err.println("Error: An I/O error occurred reading '" +  path + "'.");
            System.exit(1);
        }
    }

    /**
     * Sorts an array of chars so that it is in lexicographical order starting from index 0.
     * @param c the array of chars that needs to be sorted
     * @return the sorted array of chars
     */
    public static char[] insertionSort(char[] c) {
        for(int i = 1; i < c.length; i++) {
            int k;
            char current = c[i];
            for(k = i - 1; k >= 0 && ((int)c[k] > (int)current); k--) {
                c[k + 1] = c[k];
            }
            c[k + 1] = current;
        }

        return c;
    }

    /**
     * Sorts a MyList<String> so that it is in lexicographical order starting from index 0.
     * @param list the list> that needs to be sorted
     * @return the sorted MyList<String> list
     */
    public static MyList<String> insertionSort(MyList<String> list) {
        for(int i = 1; i < list.size(); i++) {
            int k;
            String current = list.get(i);
            for(k = i - 1; k >= 0 && list.get(k).compareTo(current) > 0; k--) {
                list.set(k + 1, list.get(k));
            }
            list.set(k + 1, current);
        }

        return list;
    }

    /**
     * Sorts a MyList<MyList<String>> so that it is in lexicographical order starting from index 0.
     * @param list the list to be sorted
     * @param output an empty list to add the outputs to
     * @return a sorted MyList<MyList<String>> where the groups are in lexicographical order
     */
    public static MyList<MyList<String>> insertionSort(MyList<MyList<String>> list, MyList<MyList<String>> output) {
        //adding the groups to the output list
        for(int i = 0; i < list.size(); i++) {
            MyList<String> toAdd = new MyLinkedList<>();
            for(int j = 0; j < list.get(i).size(); j++) {
                toAdd.add(list.get(i).get(j));
            }
            output.add(toAdd);
        }

        //sorts the groups
        for(int i = 1; i < output.size(); i++) {
            int k;
            MyList<String> current = output.get(i);
            for(k = i - 1; k >= 0 && output.get(k).get(0).compareTo(current.get(0)) > 0; k--) {
                output.set(k + 1, output.get(k));
            }
            output.set(k + 1, current);
        }

        return output;
    }

    /**
     * Iterates through the map and prints the sorted number of groups and the sorted number of anagrams.
     * @param map the map where the key-value pairs for words in the dictionary are stored
     */
    public static void printResult(MyMap<String, MyList<String>> map) {
        //https://www.w3schools.com/java/java_iterator.asp
        Iterator<Entry<String, MyList<String>>> it = map.iterator();
        MyLinkedList<String> keys = new MyLinkedList<>();
        int size = 0;

        while(it.hasNext()) {
            Entry<String, MyList<String>> next = it.next();
            if(next.value.isEmpty()) { //size of the value list - means there's no anagrams for that key
                continue;
            }
            if(next.value.size() == size) { //means there's another group of anagrams at the max size so far
                keys.add(next.key);
            } else if(next.value.size() > size) { //means this group of anagrams is the new largest anagram count
                size = next.value.size();
                keys.clear();
                keys.add(next.key);
            }
        }

        if(size < 2) {
            System.out.println("No anagrams found.");
            return;
        }

        System.out.println("Groups: " + keys.size() + ", Anagram count: " + size);
        MyList<MyList<String>> groups = new MyLinkedList<>();

        //sorts the anagrams in the groups in lexicographical order
        for(int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            MyList<String> value = insertionSort(map.get(key));
            groups.add(value);
        }

        //sorts the groups in lexicographical order
        MyList<MyList<String>> sortedGroups = new MyLinkedList<>();
        insertionSort(groups, sortedGroups);
        Iterator<MyList<String>> groupsIterator = sortedGroups.iterator();

        //prints the groups
        while(groupsIterator.hasNext()) {
            MyList<String> value = groupsIterator.next();
            System.out.print("[");

            for(int j = 0; j < value.size(); j++) {
                if(j != value.size() - 1) {
                    System.out.print(value.get(j) + ", ");
                } else {
                    System.out.println(value.get(j) + "]");
                }
            }
        }
    }

    /**
     * The main method. Checks for errors in the command line arguments before starting to find
     * the most anagrams.
     * @param args the command line arguments, to run the program properly the arguments should
     *             be <dictionary file> <bst|rbt|hash>
     * @throws FileNotFoundException when the named file is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length > 2) {
            System.err.println("Usage: java MostAnagramsFinder <dictionary file> <bst|rbt|hash>");
            System.exit(1);
        }
        File f = new File(args[0]);
        if(!f.exists()) {
            System.err.println("Error: Cannot open file '" + args[0] + "' for input.");
            System.exit(1);
        }
        if(!args[1].equals("bst") && !args[1].equals("rbt") && !args[1].equals("hash")) {
            System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
            System.exit(1);
        }

        MyMap<String, MyList<String>> map;

        if(args[1].equals("bst")) {
            map = new BSTreeMap<>();
        }
        else if(args[1].equals("rbt")) {
            map = new RBTreeMap<>();
        } else {
            map = new MyHashMap<>();
        }

        solve(map, args[0]);
        printResult(map);
    }
}