import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class Board {
    private int N; //dimension of the board
    private int [] tiles; //1 D blocks used to store the 2D board
      
    public Board (int[][] blocks) {
        //q_move is the number of moves
        N = blocks.length;
        tiles = new int [N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int po = i * N + j;
                tiles[po] = blocks[i][j];
            }
        }
    }
    
        
    public int dimension() {
        //board dimension N
        return N;
        
    }
    
    public int hamming() {
        // number of blocks out of place
        int count = 0; //initialze number of blocks out of place
        for (int i = 0; i < tiles.length; i++) {
            if ((tiles[i] !=0) && (tiles[i] != ((i + 1) % (N * N))))
                count++;
        }     
        return count;
    }
    
    public int manhattan() {
        //number of blocks distances between blocks and goal
        int count = 0;
        int i0, j0, i1, j1;
        for (int i = 0; i < tiles.length; i++) {
            int dummy = (i + 1) % (N * N); 
            if ((tiles[i] !=0) && (tiles[i] != dummy)) {
                i0 = i / N;
                j0 = i % N; // current coordinate
                i1 = (tiles[i] - 1) / N; // corret corrdinate
                j1= (tiles[i] - 1) % N;
                if (i0 > i1)
                    count += i0 - i1;
                else
                    count += i1 - i0;
                
                if (j0 > j1)
                    count += j0 - j1;
                else
                    count += j1 - j0;  
            }
        }
        return count;
    }
    
    public boolean isGoal() {
        // is this board the goal board?
        for (int i = 0; i < tiles.length; i++) {
            int dummy = (i + 1) % (N * N); 
            if (tiles[i] != dummy)
                return false;
        }
        return true;
    }
    
    public Board twin() {
        int [][] tilesT = new int [N][N];
        int [] position = new int[2]; // used to storage the positions of two nonzero numbers
        int count, i, j, i1, j1, i2, j2;
        count = 0;
        for (int k = 0; k < tiles.length; k++) {
            if (count < 2 && tiles[k] !=0) {
                position[count] = k;
                count++;
            }  
            i = k / N;
            j = k % N;
            tilesT[i][j] = tiles[k];
        }
        //
        i1 = position[0] / N;
        j1 = position[0] % N; 
        i2 = position[1] / N;
        j2 = position[1] % N; 
        
        exch(tilesT, i1, j1, i2, j2);
        return new Board(tilesT);
    }
    
    public boolean equals(Object y) {
        //does this board equal to board y
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int i = 0; i < that.tiles.length; i++) {
            if (this.tiles[i] != that.tiles[i]) {
                return false;
            }
        }
        return true;
    }
    
    private void exch(int[][] boardC,int i1, int j1, int i2, int j2) {
        int swap = boardC[i1][j1];
        boardC[i1][j1] = boardC[i2][j2];
        boardC[i2][j2] = swap;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> nbArray = new Queue<Board>();
        int NN; //number of neighbors
        int [][] tilesC = new int [N][N]; // copy of the tiles
        int i, j, i0, j0, i1, j1;
        NN=2;
        i = 0; j = 0; i0 = 0; j0 = 0; i1 = 0; j1 = 0; 
        for (int k = 0; k < tiles. length; k++) {
            if (tiles[k] == 0) {
                i0 = k / N;
                j0 = k % N;
                if (i0 % (N - 1) == 0 && j0 % (N - 1) == 0) {
                    NN = 2;
                }
                else if (i0 % (N - 1) != 0 && j0 % (N - 1) != 0) {
                    NN = 4;
                }
                else {
                    NN = 3;
                }  
            }
            i = k / N;
            j = k % N;
            tilesC[i][j] = tiles[k];
        }
        if (NN == 2) {
            //first neighbor
            i1 = i0 + 1; 
            if (i1 >= N) i1 = i0 -1;
            j1 = j0;
            
            exch(tilesC, i0, j0, i1, j1);
            nbArray.enqueue(new Board(tilesC));            
            exch(tilesC, i0, j0, i1, j1);
            
            //second neighbor
            i1 = i0;
            j1 = j0 + 1;
            if (j1 >= N) j1 = j0 -1;
            
            exch(tilesC, i0, j0, i1, j1);
            nbArray.enqueue(new Board(tilesC));
            exch(tilesC, i0, j0, i1, j1);   
        }
        else if (NN == 3) {
            if (i0 % (N - 1) == 0) {
                //first neighbor
                i1 = i0 + 1;
                if (i1 >= N) i1 = i0 - 1;
                j1 = j0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //second neighbor
                i1 = i0;
                j1 = j0 - 1;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //third neighbor
                i1 = i0;
                j1 = j0 + 1;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);   
            }
            else {
                //first neighbor
                j1 = j0 + 1;
                if (j1 >= N) j1 = j0 - 1;
                i1 = i0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //second neighbor
                i1 = i0 + 1;
                j1 = j0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //third neighbor
                i1 = i0 - 1;
                j1 = j0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);  
            }
        }
        else {
               //first neighbor
                i1 = i0 -1;
                j1 = j0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1); //swithch back
                //second neighbor
                i1 = i0 + 1;
                j1 = j0;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //third neighbor
                i1 = i0;
                j1 = j0 - 1;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);
                //fourth neighbor
                i1 = i0;
                j1 = j0 + 1;
                exch(tilesC, i0, j0, i1, j1);
                nbArray.enqueue(new Board(tilesC));
                exch(tilesC, i0, j0, i1, j1);   
        }
        return nbArray;   
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i =0; i< tiles.length; i++) {
            s.append(String.format("%2d ", tiles[i]));
            if ((i + 1) % N ==0) 
                s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println("dimension: \n"+initial.dimension());
        StdOut.println("hamming: \n"+initial.hamming());
        StdOut.println("manhattan:\n "+initial.manhattan());
        StdOut.println("isGoal:\n "+initial.isGoal());
        StdOut.println("twin:\n "+initial.twin().toString());
        StdOut.println("toString:\n "+initial.toString());
        StdOut.println("neighbors:");  
        Iterable<Board> nb = initial.neighbors();
            
        for (Board item :nb) {
            StdOut.println(item.toString());
        }
    }
}