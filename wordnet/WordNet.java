import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.LinearProbingHashST;
//import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.List;
import java.util.ArrayList;
//import java.util.iterator;
public class WordNet {
    //constructor takes the name of the two input files
    private SAP sap;
    private LinearProbingHashST<Integer, List<String>> hashset = new LinearProbingHashST<Integer, List<String>>();
    private LinearProbingHashST<String, List<Integer>> hashset1 = new LinearProbingHashST<String, List<Integer>>();
    
    public WordNet(String synsets, String hypernyms) {
        In in1 = new In(synsets);
        In in2 = new In(hypernyms);
        String str;
        String[] strArray;
        int key;
        String[] value;
        int count = 0;
        while (!in1.isEmpty()) {
            str = in1.readLine();
            strArray = str.split(",");
            key = Integer.parseInt(strArray[0]);
            value = strArray[1].split(" ");
            //SET<String> temp_set = new SET<String>();
            List<String> temp_set = new ArrayList<String>();
            List<Integer> temp_set1; 
            for (int i = 0; i < value.length; i++) {
                temp_set.add(value[i]);
                if (hashset1.contains(value[i])) {
                    temp_set1 = hashset1.get(value[i]);
                    temp_set1.add(key);
                    hashset1.put(value[i], temp_set1);
                }
                else {
                    temp_set1 = new ArrayList<Integer>();
                    temp_set1.add(key);
                    hashset1.put(value[i], temp_set1);
                }
                    
            }
            hashset.put(key, temp_set);  
            count++;
        }
        Digraph digraph = new Digraph(count);
        
        int v;
        int w;
        int count1 = 0;
        while (!in2.isEmpty()) {
            str = in2.readLine();
            strArray = str.split(",");
            v = Integer.parseInt(strArray[0]);
            for (int i = 1; i < strArray.length; i++) {
                w = Integer.parseInt(strArray[i]);
                digraph.addEdge(v, w);
                count1++;
            }  
        }
        int outdegreeN = 0;
        
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.indegree(i) != 0 && digraph.outdegree(i) == 0)
                outdegreeN++;
        }
        if (outdegreeN != 1)
            throw new IllegalArgumentException();
        sap = new SAP(digraph);
            
    }
    
    //returns all WordNet nouns
    public Iterable<String> nouns() {

        return hashset1.keys();
    }
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return hashset1.contains(word);
    }
    //distance between nounA and nounB (defined below)
    
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        /*
        SET<Integer> setA = new SET<Integer>();
        SET<Integer> setB = new SET<Integer>();
        for (int k: hashset.keys()) {
            if (hashset.get(k).contains(nounA))
                setA.add(k);
            if (hashset.get(k).contains(nounB))
                setB.add(k);
        }
        //StdOut.println(sap.length(setA, setB));
        return sap.length(setA, setB);
        */
        return sap.length(hashset1.get(nounA), hashset1.get(nounB));
    }
    
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        /*
        SET<Integer> setA = new SET<Integer>();
        SET<Integer> setB = new SET<Integer>();
        for (int k: hashset.keys()) {
            if (hashset.get(k).contains(nounA))
                setA.add(k);
            if (hashset.get(k).contains(nounB))
                setB.add(k);
        }

        
        int ancestor = sap.ancestor(setA, setB);
                */
        int ancestor = sap.ancestor(hashset1.get(nounA), hashset1.get(nounB));
        
       List<String> temp_n = hashset.get(ancestor);
       StringBuilder result = new StringBuilder();
           //StdOut.println("size of the noun: " + temp_n.size());
       for (int i = 0; i < temp_n.size(); i++) {
           result.append(temp_n.get(i));
           result.append(" ");
       }
       return result.toString();
    }
    //do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.distance("tummy", "pot"));
        StdOut.println(wordnet.sap("tummy", "pot"));
        
    }       
}