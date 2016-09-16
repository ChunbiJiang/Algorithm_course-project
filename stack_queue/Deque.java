import java.util.Iterator;
import java.util.NoSuchElementException;
//import java.lang.NullPointerException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque <Item> implements Iterable <Item>{
    
    private Node <Item> first;//beginning of deque
    private Node <Item> last; //end of deque
    private int N;    // number of elements on deque
    
        
    //helper linked list class with double direction 
    private static class Node<Item>{
        private Item item;
        private Node <Item> front;//node in front of it
        private Node <Item> back; // node right after it
    }
    
    
    public Deque(){
        //construct an empty deque
        first=null;
        last=null;
        N=0;
    }
    
    public boolean isEmpty(){
        //is the deque empty
        return N==0;
    }
    
    public int size(){
        //return the number of items on the deque
        return N;
    }
    
    public void addFirst(Item item){
        //add the item to the front
        if(item==null) throw new NullPointerException("no null item added");
        
        Node <Item> oldfirst=first;
        first= new Node <Item> ();
        first.item=item;
        
        first.front=null;
        
        if(isEmpty()) {
            first.back=null;
            last=first;
        }
        else {
            first.back=oldfirst;
            oldfirst.front=first;
        }
        N++;
    }
    
    public void addLast(Item item){
        //add the item to the end
        if(item==null) throw new NullPointerException("no null item added");
        Node <Item> oldlast=last;
        last=new Node <Item> ();
        last.item=item;
        last.back=null;
        
        if(isEmpty()){
            last.front=null;
            first=last;
        }
        else{
            last.front=oldlast;
            oldlast.back=last;
        }
        N++;
        
    }
    
    public Item removeFirst(){
        //remove and return the item from the front
        if(isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item=first.item;       
        
        if (N==1){
            first=null;
            last=null;        
        }
        else{
            first=first.back;
            first.front=null;
        }
        
        N--;
        return item;
    }
    
    public Item removeLast(){
        //remove and return item from the end
        if(isEmpty()) throw new NoSuchElementException("Deque underflow");
        //Node<Item> oldlast=last;
        Item item=last.item;
        if(N==1){
            last=null;
            first=null;
        }
        else{
            last=last.front;
            last.back=null;
        }
        N--;
        return item;
    }
    
    /**
        //Return a string representation of this queue
    private String toString(){
        StringBuilder s=new StringBuilder();
        for(Item item:this)
            s.append(item+" ");
        return s.toString();
    }
    */
    
//return an iterator that iterates over the items
    public Iterator<Item> iterator(){
        return new ListIterator<Item>(first);
    }
    
    //an iterator, doesn't implement remove() since it's optional
    //from front to end
    private class ListIterator<Item> implements Iterator<Item>{
        private Node<Item> current;
        
        public ListIterator(Node<Item> first){
            current=first;
            //current=last;
        }
        
        public boolean hasNext(){
            return current!=null;
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            Item item=current.item;
            current=current.back;
            return item;
        }
    }
        
   public static void main(String [] args){
       Deque<String> deq=new Deque<String>();
       while(!StdIn.isEmpty()){
       //for (int i=0;i<10;i++){
           String item=StdIn.readString();
           if(!item.equals("-")) deq.addFirst(item);
           else if (!deq.isEmpty()) StdOut.print(deq.removeFirst()+" ");
       }
       StdOut.println("("+deq.size()+" left on deque");
       for(String item:deq)
           StdOut.println(item);
   }
    
}