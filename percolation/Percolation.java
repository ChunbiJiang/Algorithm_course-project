import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation{
    //private int [] status; // status[i]=1 if site  is open, 0 if it is blocked
    private byte[] status;// status [i]=1 if site is open, 0 if it is blocked
    private int NN; // number of components
    private int v_id_top;
    private int v_id_down;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf_c; // no virtual down site, only used for checking full status
    
    public Percolation (int N){
        if (N<=0) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }
        status=new byte[N*N+2];
        NN=N;
        v_id_top=N*N;
        v_id_down=N*N+1;
        //create N-by-N grid, with all sites blocked
        uf=new WeightedQuickUnionUF(N*N+2);
        uf_c=new WeightedQuickUnionUF(N*N+2);
        for (int i=0; i<N*N+2;i++){
            status[i]=0; // initiallly blocked
        }
    }
    
    public void open(int i, int j){
        // open site (row i, column j) if it is not open already
        validate(i,j);
        int id=xyTo1D(i, j);
        if (!isOpen(i,j)) status[id]=1;// 1 is open, 0 is for blocked
        
        //check if it is connected to the top virtual site
        if(id<NN && !uf.connected(id,v_id_top)){
            uf.union(id,v_id_top);
            uf_c.union(id,v_id_top);
            //System.out.println("("+i+","+j+") has been connected to the top virtual site");
        }
        //check if it is connected to the down virtual site
        if(id>=NN*(NN-1) && !uf.connected(id,v_id_down)){
            uf.union(id,v_id_down);
            // for the uf_c, no down virtual site
        }
               
        //check its neighboring sites, if the neighboring site is also open
        // they have to be connected.
        if (j>1 && isOpen(i,j-1)) {
            //left neighboring site is open, connect them
            int id_l=xyTo1D(i,j-1);
            if (!uf.connected(id,id_l))
                uf.union(id,id_l);
                uf_c.union(id,id_l);
        }
        if(j<NN && isOpen(i,j+1)) {
            //right neighboring site is open, connect them
            int id_r=xyTo1D(i,j+1);
            if (!uf.connected(id,id_r))
                uf.union(id,id_r);
                uf_c.union(id,id_r);
        }
        if(i>1 && isOpen(i-1,j)){
            //up neighboring site is open, connect them    
            int id_u=xyTo1D(i-1,j);
            if (!uf.connected(id,id_u))
                uf.union(id,id_u);
                uf_c.union(id,id_u);
        }
        if(i<NN && isOpen(i+1,j)){
            //down neighboring site is open, connect them
            int id_d=xyTo1D(i+1,j);
            if (!uf.connected(id,id_d))
                uf.union(id,id_d);
                uf_c.union(id,id_d);
        }
        

    }
    
    //validate that p is a valid index
    private void validate(int i,int j){
        if ((i <= 0 || i > NN) ||(j<=0 || j>NN)) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }

    }
        
    
    //map from a 2-dimentional (row,column) to 1D
    private int xyTo1D(int i, int j){
        return j-1+(i-1)*NN;     
    }
    
    
    public boolean isOpen(int i, int j){
        //is site (row i, column j) open?
        validate(i,j);
        int id=xyTo1D(i,j);
        return status[id]==1;    
    }
    
    public boolean isFull(int i, int j) {
        //is site (row i, column j) full?
        validate(i,j);
        int id=xyTo1D(i,j);
        return (uf_c.connected(id,v_id_top) && isOpen(i, j));
    }
    
    public boolean percolates(){
        //does the system percolate?
        return uf.connected(v_id_top,v_id_down);
    }
    
    public static void main(String [] args){
        //test client (optional)
        int count;
        int ri;
        int ci;
        double f;
        int N=StdIn.readInt();
        Percolation per=new Percolation(N);
        count=0;
        while(!per.percolates()){
            ri=(int)(StdRandom.uniform()*N)+1;
            ci=(int)(StdRandom.uniform()*N)+1;
            //ri=StdIn.readInt();
            //ci=StdIn.readInt();
            if(per.isOpen(ri,ci)) continue;
            per.open(ri,ci);
            count++;
        }
        //StdOut.println(count + " sites opened");
        f=(count*1.0)/(N*N);
        StdOut.println("f: "+f);
    }
}