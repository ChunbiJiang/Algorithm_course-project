import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue <Item> implements Iterable <Item>{
    private Item[] q; //queue elements
    private int N; //number of elements on queue
    private int first; //indext of first element of queue
    private int last; //indext of next available slot
    
    public RandomizedQueue(){
        //construct an empty randomized queue
        q=(Item[]) new Object[2];
        //StdOut.println("initialized size: "+q.length);
        N=0;
        first=0;
        last=0;
    }
    
    public boolean isEmpty(){
        //is the queue empty?
        return N==0;
    }
    
    public int size(){
        //return the number of items on the queue
        return N;
    }
    
    private void resize(int max){
        //resize the underlying array
        assert max>=N;
        Item[] temp=(Item[]) new Object[max];
        for(int i=0;i<N;i++){
            temp[i]=q[(first+i)%q.length];
        }
        q=temp;
        first=0;
        last=N;
    }
    
    
    public void enqueue(Item item){
        //double size of array if necessary and recopy to front of array
        if (item==null){
            throw new NullPointerException();
        }
        if(N==q.length) resize(2*q.length); //double size of array if necessary
        q[last++]=item;
        if(last==q.length) last=0; //wrap-around
        N++;
        
    }
    
    public Item dequeue(){
        //remove and return a random item
        if(isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item;
        if (N==1){
            item=q[first];
            first=0;
            q[first]=null;
        }
        else{
            int index_q=(int)(StdRandom.uniform()*N);
            item=q[index_q];   
            /**
            for(int i=index_q;i<N-1;i++){
                q[i]=q[i+1];
            }
            */
            q[index_q]=q[N-1]; // move the last item to fill the gap
            q[N-1]=null;
        }
        
        if (last==0){
            last=q.length-1;
        }
        else{
            last--;
        }
        N--;
        if(N>0 && N==q.length/4) resize(q.length/2);
        return item;
    }
    
    public Item sample(){
        //return (but do not remove) a random item
        if(isEmpty()) throw new NoSuchElementException("Queue underflow");
        
        int index_q=(int)(StdRandom.uniform()*N);//randomly selected index between 0 and N.
        return q[index_q];
        
    }
    
    public Iterator<Item> iterator(){
        //return an independent iterator over items in rando order
        return new ArrayIterator();
    }
       
    private class ArrayIterator implements Iterator<Item>{
        private int [] q_i;
        private int i=0;       
        private ArrayIterator(){
            q_i=new int[N];
            for(int ii=0;ii<N;ii++){
                q_i[ii]=ii;
            }
            StdRandom.shuffle(q_i);
            
        }
        public boolean hasNext(){
            return i<N;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            Item item=q[(q_i[i]+first)%q.length];
            i++;
            return item;
        }
    }
    
    public static void main(String [] args){
        //unit testing
        RandomizedQueue<String> q=new RandomizedQueue<String>();
        String item;
        //test2
        item=StdIn.readString();
        q.enqueue(item);  
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());   

        StdOut.println("dequeue: "+q.dequeue());
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());  
        
        item=StdIn.readString();
        q.enqueue(item);  
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());    
        StdOut.println("dequeue: "+q.dequeue());
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());  
        
        item=StdIn.readString();
        q.enqueue(item);  
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());    
        StdOut.println("dequeue: "+q.dequeue());
        StdOut.println("isEmpty? "+q.isEmpty());
        StdOut.println("size: "+q.size());          
    }
}