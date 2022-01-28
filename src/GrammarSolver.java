import java.util.*;

/**
 * TODO: 
 * Comments
 * Deal with adjective's redundance/ period separated adjectives.
 * Figure out unmodifiable list bug in main
 *
 */

public class GrammarSolver{

   private final static int MAX_NUM_ADJECTIVE = 3;
   private final static String ADJECTIVE_PHRASE_SYMBOL = "<adjp>";
   private final static String ADJECTIVES_SYMBOL = "<adj>";
   
   private Random r;
   private SortedMap<String, List<String>> map;
   
   GrammarSolver(List<String> grammar){
      r = new Random();
      map = new TreeMap<String, List<String>>();
      
      List<String> grammarCopy = new ArrayList<>();
      grammarCopy.addAll(grammar);
      
      for(String part : grammarCopy){
         String[] nonTerminalAndRules = part.split(":");
         String nonTerminal = nonTerminalAndRules[0];
         String expression = nonTerminalAndRules[1];
         String[] rawRules = expression.split("\\|");
         
         List<String> rules = new ArrayList<>();
         for(String rawRule : rawRules){
            rules.add(rawRule);
         }
         map.put(nonTerminal, rules);
      }
   }
   
   // returns true if the given symbol is a nonterminal in the grammar and false otherwise.
   public boolean grammarContains(String symbol){
      Set<String> keys = map.keySet();
      return keys.contains(symbol);
   }
   
   public String[] generate(String symbol, int times){
      String[] results = new String[times];
      for(int i = 0; i < times; i++){
         results[i] = "";
         
         String result = possibilitiesExplorer(symbol);
         char firstLetter = result.charAt(0);
         result = Character.toUpperCase(firstLetter) + result.substring(1, result.length() - 1) + ".";
         results[i] += result;
      }
      return results;
   }
   
   private String possibilitiesExplorer(String symbol){
      List<String> options = map.get(symbol);
      int randomIndex = r.nextInt(options.size());
      String choice = options.get(randomIndex);
      String[] parts = choice.split("\\s+");
      
      String result = "";
      for(String part : parts){
         if(part.equals(ADJECTIVE_PHRASE_SYMBOL)){
            Random r = new Random();
            int randomLifetime = 1 + r.nextInt(MAX_NUM_ADJECTIVE);
            result += adjectivePhraseExplorer(ADJECTIVE_PHRASE_SYMBOL, randomLifetime);
         } else {
            if(grammarContains(part) == false){
               result += part + " ";
            } else {
               result += possibilitiesExplorer(part);
            }
         }
      }
      return result;
   }
   
   private String adjectivePhraseExplorer(String symbol, int lifetime){
      String result = "";
      if(lifetime <= 0){
         List<String> options = map.get(ADJECTIVES_SYMBOL);
         int randomIndex = r.nextInt(options.size());
         String choice = options.get(randomIndex);
         return choice + " ";
      } else {
         List<String> options = map.get(symbol);
         int randomIndex = r.nextInt(options.size());
         String choice = options.get(randomIndex);
         String[] parts = choice.split("\\s+"); 
         
         for(String part : parts){
            if(grammarContains(part) == false){
               result += part + " ";
            } else {
               result += adjectivePhraseExplorer(part, lifetime - 1);
            }
         }
      }
      return result;
   }
   
   //return a String representation of all the nonterminal symbols in the grammar as a sorted, 
   // comma-separated list in square brackets, like "[<np>, <s>, <vp>]".
   public String getSymbols(){
      Set<String> keys = map.keySet();
      Iterator<String> itr = keys.iterator();
      String symbols = "[" + itr.next();
      while(itr.hasNext()){
         symbols += ", " + itr.next(); 
      }
      return symbols += "]";
   }
}