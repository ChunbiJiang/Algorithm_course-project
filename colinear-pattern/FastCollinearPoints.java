//import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
//import java.lang.*;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment [] line; // line to store all the segments
    //private LineSegment segment;
    private int nLine = 0; // number of segments
    
    public FastCollinearPoints(Point [] points) {
    //finds all line segments containing 4 points
        if (points == null) {
            throw new NullPointerException("argument is null");
        }
        //sort(points);
                    
        int N = points.length;
        /**
        if (N < 4) {
            throw new IllegalArgumentException("at least four points");
        }
        */
        line = new LineSegment [N*N]; 
        if (N > 1) {
            Point [] p_copy;
            for (int i = 0; i < N; i++) {
                int k = 0;
                p_copy = new Point[N-1];
                for (int j = 0; j < N; j++) {
                    if (j != i) {
                        p_copy[k++] = points[j];  
                        if (points[i].compareTo(points[j]) == 0) 
                            throw new IllegalArgumentException("repeated points");
                    }
                /**
                p_copy[k++]=points[j];
                if (points[i].compareTo(points[j]) == 0 && i != j) 
                    throw new IllegalArgumentException("repeated points");
                    */
                }          
            //sort by the slopeorder to point[i]
            //sort(p_copy, points[i].slopeOrder());
                Arrays.sort(p_copy, points[i].slopeOrder());
            // add colinear points to the line segment
                colinearSearch(p_copy, points[i]);       
            }        
        }
    }
   
    private void colinearSearch(Point[] points, Point p) {
        //if if there are three or more points have the same slope to point p
        int N = points.length;
        int counter_start = 0;
        int counter_end = 0;
        Point p_start;
        Point p_end; 
        p_start = points[0];
        p_end = points[0];
        double slope1 = p.slopeTo(points[0]);
        double slope2;
        int i = 1;
        while (i < N) {
            slope2 = p.slopeTo(points[i]);
            boolean flag = (Double.isInfinite(slope1) && Double.isInfinite(slope2));
            if ((slope2 == slope1) || flag) {
                if (p_start.compareTo(points[i]) > 0)
                    p_start = points[i];
                if (p_end.compareTo(points[i]) < 0)
                    p_end = points[i];
                
                counter_end += 1;
                i++;    
            }
            else {
                if ((counter_end - counter_start + 1) >= 3) {
                    //more than two points have the same slope to p
                    if (p.compareTo(p_start) < 0) {
                        p_start = p;
                        line[nLine] = new LineSegment(p_start, p_end);
                        nLine += 1;    //segment number+1 
                    }
                }
                counter_start = i;
                counter_end = i;
                p_start = points[i];
                p_end = points[i];
                slope1 = slope2;
                i++;
            }   
        }
        //for i= N
        if (i == N && (counter_end - counter_start + 1) >= 3) {
            //more than two points have the same slope to p                   
            if (p.compareTo(p_start) < 0) {
                p_start = p;
                line[nLine] = new LineSegment(p_start, p_end);
                nLine += 1;    //segment number+1 
            }
        }
    }   
    /**
     * sorting
     */
    /**
    private static void sort(Object[] a, Comparator comparator) {
        int N= a.length;
        for (int i = 0; i < N; i++) {
            for (int j = i; j > 0 && less(comparator, a[j], a[j-1]); j--) {
                exch(a, j, j-1);           
            }
        }   
    }
    
    private static boolean less(Comparator c, Object v, Object w) {
        return c.compare(v, w) < 0;
    }
    
    private static void exch(Object [] a, int i, int j) {
        Object swap = a[i];
        a[i]=a[j];
        a[j]=swap;
    }
    */
    public int numberOfSegments() {
        //the number of line segments
        return nLine;
    }
    public LineSegment[] segments() {
        //the line segments
        LineSegment [] collinear =  new LineSegment[nLine]; // line to store all the segments
        for (int kk = 0; kk < nLine; kk++) {
            collinear[kk] = line[kk];
        }
        return collinear;
    }
    
        
    
    public static void main(String[] args) {
// read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

    // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(.005);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        

    // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}