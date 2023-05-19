import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class topten{


   public static void main (String args[]){


      String TEXT="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";


      System.out.println(TEXT);

      String[] words = TEXT.split(" ");

      int count=0;
      HashMap<String,Integer> map = new HashMap<String,Integer>();

      for (int i=0; i<words.length;i++){
         count=CountWords(words[i], words);
         System.out.println("Word " + words[i] + " count: " + count);

         map.put(words[i],count); 
      }

      sortByValue(false, map);


   }

   private static int CountWords(String word, String[] words){

      int count=0;

      for (int i=0; i<words.length; i++){
          if (words[i].equals(word))
             count++;

      } 
      
      return count;

   }


  private static void sortByValue(boolean order, HashMap map) {
    // convert HashMap into List
    List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());
    
    // sorting the list elements
    Collections.sort(list, new Comparator<Entry<String, Integer>>() {
      public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
        if (order) {
          // compare two object and return an integer
          return o1.getValue().compareTo(o2.getValue());
        } else {
          return o2.getValue().compareTo(o1.getValue());
        }
      }
    });

    // prints the sorted HashMap
    int i=0;
    Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
    for (Entry<String, Integer> entry : list) {
      if (i>9) break;
      sortedMap.put(entry.getKey(), entry.getValue());
      i++;
    }
    printMap(sortedMap);
  }

  private static void printMap(Map<String, Integer> map) {

    for (Entry<String, Integer> entry : map.entrySet()) {
      System.out.println(entry.getKey() + "\t" + entry.getValue());
    }
    System.out.println("\n");
  }

}



