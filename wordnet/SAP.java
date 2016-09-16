import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.SET;
public class SAP {

   // constructor takes a digraph (not necessarily a DAG)
   private Digraph digraph;
   public SAP(Digraph G) {
       digraph = new Digraph(G);     
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
       if (v == w) 
           return 0;
       int distance_min = -1;
       BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(digraph, v);
       BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(digraph, w);
       for (int i = 0; i < digraph.V(); i++) {
           if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
               if (distance_min == -1) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
               }
               else if (distance_min > bfs1.distTo(i) + bfs2.distTo(i)) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
               }
           }
       }
       return distance_min;
   }
   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
       if (v == w) 
           return v;
       int distance_min = -1;
       int ancestor_min = -1;
       BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(digraph, v);
       BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(digraph, w);
       for (int i = 0; i < digraph.V(); i++) {
           if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
               if (distance_min == -1) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
                   ancestor_min = i;
               }
               else if (distance_min > bfs1.distTo(i) + bfs2.distTo(i)) {                   
                   
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
                   ancestor_min = i;
               }
           }
       }
       return ancestor_min;
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
       int distance_min = -1;
       BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(digraph, v);
       BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(digraph, w);
       for (int i = 0; i < digraph.V(); i++) {
           if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
               //StdOut.println("haha: " +i);
               if (distance_min == -1) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
               }
               else if (distance_min > bfs1.distTo(i) + bfs2.distTo(i)) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
               }
           }
       }
       return distance_min;  
   }
   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
       
       int distance_min = -1;
       int ancestor_min = -1;
       
       BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(digraph, v);
       BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(digraph, w);
       for (int i = 0; i < digraph.V(); i++) {
           if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
               //StdOut.println("hehe: " +i);
               if (distance_min == -1) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
                   ancestor_min = i;
               }
               else if (distance_min > bfs1.distTo(i) + bfs2.distTo(i)) {
                   distance_min = bfs1.distTo(i) + bfs2.distTo(i);
                   ancestor_min = i;
               }
           }
       }
       return ancestor_min;      
   }
   
   // do unit testing of this class
   public static void main(String[] args) {
       In in = new In(args[0]);
       Digraph G = new Digraph(in);
       SAP sap = new SAP(G);
       /*
       while (!StdIn.isEmpty()) {
           int v = StdIn.readInt();
           int w = StdIn.readInt();
           int length   = sap.length(v, w);
           int ancestor = sap.ancestor(v, w);
           StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
       */
       SET<Integer> set1 = new SET<Integer>();
       SET<Integer> set2 = new SET<Integer>();
       set1.add(0);
       set1.add(1);
       set1.add(2);
       set2.add(7);
       set2.add(8);
       set2.add(12);
       int length   = sap.length(set1, set2);
       int ancestor = sap.ancestor(set1, set2);
       StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       
   }
}