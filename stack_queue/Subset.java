import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset{
    public static void main(String[] args){
        RandomizedQueue<String> q=new RandomizedQueue<String>();
        //int k= StdIn.readInt();
        int k = Integer.parseInt(args[0]);
        while(!StdIn.isEmpty()){
            String s=StdIn.readString();
            if(q.size()>k-1){
                q.enqueue(s);
                q.dequeue();
            }
            else{
                q.enqueue(s);
            }
            
            //q.enqueue(s);
        }
        for(int i=0;i<k;i++){
            StdOut.println(q.dequeue());
        }
    }
}