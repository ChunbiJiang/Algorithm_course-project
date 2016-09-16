// OrderedArrayMaxPQ
public class OrderedArrayMaxPQ<Key extends Comparable<Key>> {
    private Key[] pq;
    private int N;
    public OrderedArrayMaxPQ(int capacity) {
        pq = (Key[]) new Comparable [capacity];
        N = 0;
    }
    public boolean isEmpty() { return N ==0; }
    public int size() { return N; }
    public void insert (Key x) {       
        pq[N++] = x;
        int max = N-1;
        for (int i = N-1; i >= 0; i--) {
            if (less(max, i)) {
                exch(max, i);               
            }
        }
    }
    
    public Key delMax() {
        return pq[--N];
    }
    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }
    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }
    public static void main(String[] args) {
        OrderedArrayMaxPQ<String> pq = new OrderedArrayMaxPQ<String> (10);
        pq.insert("a");
        pq.insert("b");
        pq.insert("c");
        pq.insert("d");
        while (!pq.isEmpty()) {
            StdOut.println(pq.delMax());
        }
    }
}
