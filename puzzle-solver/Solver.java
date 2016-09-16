import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class Solver {
    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    private Board initialBoard;
    private Board initialBoardTw;
    private int moves;
    private boolean flag; // flag to check if the puzzle is solvable
    private Stack<Board> solutionArray = new Stack<Board>();  

            
    private static class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;
        private int step;
        private SearchNode previous = null;
        
        private SearchNode(Board y, int move) {
            board = y;
            step = move;
            priority = move + y.manhattan();
        }       
        private int getPriority() {
            return priority;
        }
        public int compareTo(SearchNode other) {
            if (other.getPriority() > this.getPriority()) {
                return -1;
            }
            if (other.getPriority() < this.getPriority()) {
                return 1;
            }
            else {
                return 0;
            }
        }               
        private boolean equalTo(SearchNode other) {
            return this.board.equals(other.board);
        }
    }

    public Solver (Board initial) {
        //find a solution to the initial board using the A* algorithm
        initialBoard = initial;
        initialBoardTw = initial.twin();
        SearchNode dummynode;
        SearchNode searchnode;
        moves = 0;
        
        dummynode = new SearchNode(initial, moves);
        pq.insert(dummynode); 
        
        dummynode = new SearchNode(initialBoardTw, moves);
        pq.insert(dummynode); //insert the twin board into the twin priority queue
        
        searchnode = pq.delMin();
        int cc=0; 
        while (!searchnode.board.isGoal()) {  
            moves++;
            for (Board item:searchnode.board.neighbors()) {
                if (searchnode.previous == null) {
                    dummynode = new SearchNode(item, moves);
                    dummynode.previous = searchnode;
                    pq.insert(dummynode);
                }
                else if (!item.equals(searchnode.previous.board)) {
                    dummynode = new SearchNode(item, moves);
                    dummynode.previous = searchnode;
                    pq.insert(dummynode);
                }
            }
            
            searchnode = pq.delMin();
            moves=searchnode.step;
        }
        while (searchnode.previous != null) {
            solutionArray.push(searchnode.board);
            searchnode = searchnode.previous;
        }
        solutionArray.push(searchnode.board);
        if (searchnode.board.equals(initialBoard)) {
            flag = true;
        }
        else {
            flag = false;
            moves = -1; 
        }
    }

    public boolean isSolvable() {
        //is the initial board solverable
        return flag;
    }
        
    public int moves() {
        //min number of moves
        return moves;
    }
    
    public Iterable<Board> solution() {
        //sequence of moves
        if (isSolvable()) {
            return solutionArray;
        }
        else
            return null;        
    }
   
    public static void main(String[] args) {

    // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

    // solve the puzzle
        StdOut.println("solving for the solution:");
        Solver solver = new Solver(initial);

    // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
            StdOut.println("moves: "+solver.moves());
        }
        else {
            StdOut.println("solvable");
            StdOut.println("moves: "+solver.moves());
            StdOut.println("Minimum number of moves = " + solver.moves());
            StdOut.println("steps:");
            int count = 0;
            StdOut.println("    *    *   ");
            for (Board board : solver.solution()) {
                StdOut.println("step:"+count+" ");
                StdOut.println(board);
                count++;
            }
        }
    }
}