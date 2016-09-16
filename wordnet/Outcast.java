import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Outcast {
    private WordNet wordnet;
    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordnet = wordnet;
    }
   public String outcast(String[] nouns) {
       int d_max = 0;
       String noun_obj = ""; 
       for (int i = 0; i < nouns.length; i++) {
           int d_temp = 0;
           for (int j = 0; j < nouns.length; j++) {
               d_temp  += wordnet.distance(nouns[i], nouns[j]);
           }
           if (d_max < d_temp) {
               d_max = d_temp;
               noun_obj = nouns[i];
           }
       }
       return noun_obj;
       
   }
   
   // given an array of WordNet nouns, return an outcast
   public static void main(String[] args) {
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int t = 2; t < args.length; t++) {
           In in = new In(args[t]);
           String[] nouns = in.readAllStrings();
           StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
   }
}