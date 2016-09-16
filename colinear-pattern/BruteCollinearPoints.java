import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private LineSegment [] line; // line to store all the segments
    private int nLine = 0; // number of segments
    
    public BruteCollinearPoints(Point[] points) {
        //finds all line segments containing 4 points
        if (points == null) {
            throw new NullPointerException("argument is null");
        }                    
        int N = points.length;
        /**
        if (N < 4) {
            throw new IllegalArgumentException("at least four points");
        }
        */
        line = new LineSegment [N*N];
        //coline = new LineSegment [N*N];        
        for (int i = 0; i < N; i++) {
            Point p = points[i];
            // if point p is null
            if (p == null) {
                throw new NullPointerException("elelment in array is null");
            }
            for (int j = i+1; j < N; j++) {
                Point q = points[j];               
                //if point q is null
                if (q == null) {
                    throw new NullPointerException("elelment in array is null");
                }                
                //if point q is the same as point p
                if (p.compareTo(q) == 0) {
                    throw new IllegalArgumentException("repeated points");
                }
                double slope1 = p.slopeTo(q);
                for (int k = j + 1; k < N; k++) {
                    Point r = points[k];
                    // if r is null
                    if (r == null) {
                        throw new NullPointerException("elelment in array is null");
                    }                                   
                    // if r is exactly the same as p
                    if (p.compareTo(r) == 0 || q.compareTo(r) == 0) {
                        throw new IllegalArgumentException("repeated points");
                    }                    
                    double slope2 = p.slopeTo(r);                        
                    // point p, q, r are on the same line, then we do not need to check point s
                    for (int m = k + 1; m < N; m++) {
                        Point s = points[m];
                        // s is null
                        if (s == null) {
                            throw new NullPointerException("elelment in array is null");
                        }                           
                            // if p is exactly the same as r
                        if (p.compareTo(s) == 0 || q.compareTo(s) == 0 || r.compareTo(s) == 0) {
                            throw new IllegalArgumentException("repeated points");
                        }
                        double slope3 = p.slopeTo(s);                                                
                        if (slope1 == slope2 && slope1 == slope3) {
                            Point p_start = p;
                            Point p_end = p;
                            if (p_start.compareTo(q) > 0) {
                                p_start = q;
                            }
                            if (p_end.compareTo(q) < 0) {
                                p_end = q;
                            }                                
                            if (p_start.compareTo(r) > 0) {
                                p_start = r;
                            }
                            if (p_end.compareTo(r) < 0) {
                                p_end = r;
                            }
                            if (p_start.compareTo(s) > 0) {
                                p_start = s;
                            }
                            if (p_end.compareTo(s) < 0) {
                                p_end = s;
                            }                                
                                //add one new segment
                            line[nLine] = new LineSegment(p_start, p_end);
                            nLine += 1;    //segment number+1                                                              
                        }
                    }
                }
            }
        }
        /**
        colline =  new LineSegment[nLine]; // line to store all the segments
        for (int kk = 0; kk < nLine; kk++) {
            colline[kk] = line[kk];
        } 
        */
    }    
    public int numberOfSegments() {
        //the number of line segments
        return nLine;
    }
    public LineSegment[] segments() {
        //the line segments
        LineSegment[] colline =  new LineSegment[nLine]; // line to store all the segments
        for (int kk = 0; kk < nLine; kk++) {
            colline[kk] = line[kk];
        } 
        return colline;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}