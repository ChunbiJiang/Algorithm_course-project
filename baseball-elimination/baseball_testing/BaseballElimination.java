import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.LinearProbingHashST;
import java.util.List;
import java.util.ArrayList;
public class BaseballElimination {
    private int N;            
    private List<String> teamList = new ArrayList<String>();
    private LinearProbingHashST<String, List<String>> hashmap = new LinearProbingHashST<String, List<String>>();
    private int[][] wlaArray;
    private int[][] gArray;
    private boolean[] remove;
    private int winMax = 0;   
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = Integer.parseInt(in.readLine());
        wlaArray = new int[N][3];
        gArray = new int[N][N];
        remove = new boolean[N];
        String str;
        String[] strArray;
        int winMaxi = 0;
        for (int i = 0; i < N; i++) {
            str = in.readLine();
            strArray = str.trim().split("\\s+");
            teamList.add(strArray[0]);
            remove[i] = false;
            wlaArray[i][0] = Integer.parseInt(strArray[1]);
            wlaArray[i][1] = Integer.parseInt(strArray[2]);
            wlaArray[i][2] = Integer.parseInt(strArray[3]);
            if (winMaxi < wlaArray[i][0]) {
                winMaxi = wlaArray[i][0];  
                winMax = i;
            }
            for (int j = 4; j < strArray.length; j++) {
                gArray[i][j - 4] = Integer.parseInt(strArray[j]);
            }
        }
        for (String team0: teamList) {
            teamElimination(team0);
        }        
    } 
    
    private void teamElimination(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        int ij = teamList.indexOf(team);
        if (wlaArray[ij][0] + wlaArray[ij][2] < wlaArray[winMax][0]) {
            remove[ij] = true;
            List<String> listtemp = new ArrayList<String>();
            listtemp.add(teamList.get(winMax));
            hashmap.put(team, listtemp);
        }
        else {
            int V = 1 + 1 + N - 1 + (N - 1) * (N - 2) / 2;
            FlowNetwork network = new FlowNetwork(V);
            FlowEdge edgeT; 
            double capacityT =  Double.POSITIVE_INFINITY;
            LinearProbingHashST<Integer, List<String>> vertice = new LinearProbingHashST<Integer, List<String>>();  
            List<String> list1;
            int w;
            int k = teamList.indexOf(team);
            int count = 1;
            int counti = 0;
            int countj;
            for (int i = 0; i < N - 1; i++) {
                if (i == k)
                    continue;
                countj = counti + 1;
                for (int j = i + 1; j < N; j++) {
                    if (j == k)
                        continue;
                    edgeT = new FlowEdge(0, count, (double) gArray[i][j]);
                    network.addEdge(edgeT); 
                    
                    list1 = new ArrayList<String>();
                    list1.add(teamList.get(i));
                    list1.add(teamList.get(j));
                    vertice.put(count, list1);
                    w = (N - 2) * (N - 1) / 2 + 1 + counti;
                    edgeT = new FlowEdge(count, w, capacityT);
                    network.addEdge(edgeT);                
                    w = (N - 2) * (N - 1) / 2 + 1 + countj;                
                    edgeT = new FlowEdge(count, w, capacityT);
                    network.addEdge(edgeT);                
                    count++;  
                    countj++;
                }
                counti++;
            }
            counti = 0;
            int capacity;
            for (int i = 0; i < N; i++) {
                if (i == k) continue;
                w = (N - 2) * (N - 1) / 2 + 1 + counti;
                capacity = wlaArray[k][0] + wlaArray[k][2] - wlaArray[i][0];
                edgeT = new FlowEdge(w, V - 1, (double) capacity);
                network.addEdge(edgeT);
                counti++;
            }          
            FordFulkerson maxflow = new FordFulkerson(network, 0, V - 1);
            //
            List<String> list2;
            List<String> list3 = new ArrayList<String>();
            for (int mm = 1; mm <= (N - 1) * (N - 2) / 2; mm++) {
                if (maxflow.inCut(mm)) {
                    //StdOut.println("vertice: " + mm);
                    list2 = vertice.get(mm);
                    if (!list3.contains(list2.get(0))) {
                        list3.add(list2.get(0));
                        //StdOut.println("list2.get(0): " + list2.get(0));
                    }
                    if (!list3.contains(list2.get(1))) {
                        list3.add(list2.get(1)); 
                        //StdOut.println("list2.get(1): " + list2.get(1));
                    }
                }
            }
            hashmap.put(team, list3);
            //
            double flow1 = 0;
            for (FlowEdge edge: network.edges()) {
                if (edge.from() == 0) {
                    flow1 += edge.capacity();
                }
            }        
            if (flow1 > maxflow.value()) {
                remove[ij] = true;
            }
        
        }
    }
    
    public int numberOfTeams() {
        return N;
    }   
    public Iterable<String> teams() {
        return teamList;
    }
    public int wins(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        int i = teamList.indexOf(team);
        return wlaArray[i][0];
    }
    public int losses(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        int i = teamList.indexOf(team);
        return wlaArray[i][1];
    }
    public int remaining(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        int i = teamList.indexOf(team);
        return wlaArray[i][2];
    }
    //number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamList.contains(team1) || !teamList.contains(team2))
            throw new IllegalArgumentException();
        int i = teamList.indexOf(team1);
        int j = teamList.indexOf(team2);
        return gArray[i][j];
    }    
    //is given team eliminated
    public boolean isEliminated(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        int ij = teamList.indexOf(team);
        return remove[ij];
    }
    //subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamList.contains(team))
            throw new IllegalArgumentException();
        if (isEliminated(team)) {
            return hashmap.get(team);
        }
        return null;
    }   
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]); 
        for (String team: division.teams()) {
            if (division.isEliminated(team)) {               
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }               
                StdOut.println("}");              
            }
        }    
    }
}
