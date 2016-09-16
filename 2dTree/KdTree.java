import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private Node root; // root of the KdTree
    private int N = 0; // size
    private static class Node {
        private Point2D p; // the point
        private Node lb;  // the left/bottom subtree
        private Node rt; // the right/top subtree
        public Node(Point2D p) {
            this.p = p;
        }
    }
    
    public boolean isEmpty() {
        return (root == null);    
    }
    public int size() {
        return N;
        
    }
    public void insert(Point2D p) {
        root = insert(root, p, 1);       
    }
    private Node insert(Node x, Point2D p, int depth) {
        if (x == null) {
            Node dummy = new Node(p);
            N++;
            return dummy;            
        }
        if (x.p.equals(p)) {
            x.p = p;
            return x;
        }
        if (depth % 2 == 0) {
            //the upper level is horizontal, so this level should be vertical, check y
            if (p.y() < x.p.y()) {
                // bottom
                x.lb = insert(x.lb, p, depth + 1);
            }
            else {
                // top
                x.rt = insert(x.rt, p, depth + 1);
            }  
        }
        else {
            // the upper level is vertical, so this level should be horizontal, check x
            if (p.x() < x.p.x()) {
                // left             
                x.lb = insert(x.lb, p, depth + 1);
            }
            else {
                //right
                x.rt = insert(x.rt, p, depth + 1);
            }
        }
        return x;
    }
    public boolean contains(Point2D p) {
        return contains(root, p, 1);
    }
    private boolean contains(Node x, Point2D p, int depth) {
        if (x == null) {
            return false;
        }
        if (x.p.equals(p)) {
            return true;
        }
        if (depth % 2 == 0) {
            //the upper level is horizontal, so this level should be vertical, check y
            if (p.y() < x.p.y()) {
                // bottom
                return contains(x.lb, p, depth + 1);
            }
            else {
                // top
                return contains(x.rt, p, depth + 1);
            }
        }
        else {
            // the upper level is vertical, so this level should be horizontal, check x
            if (p.x() < x.p.x()) {
                // left
                return contains(x.lb, p, depth + 1);
            }
            else {
                //right
                return contains(x.rt, p, depth + 1);
            }
        }
        
    }
    public void draw() {
        draw(root, 1, new RectHV(0.0, 0.0, 1.0, 1.0));
    }
    private void draw(Node x, int depth, RectHV rect) {
        if (x != null) {
            if (depth % 2 == 0) {
                // horizontal line
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(.005);
                
                StdDraw.line(rect.xmin(), x.p.y(), rect.xmax(), x.p.y());
                StdDraw.setPenColor(StdDraw.BLACK);
                x.p.draw();
                draw(x.lb, depth + 1, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y())); 
                draw(x.rt, depth + 1, new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax()));
            }
            else {
                // vertical line
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(.005);
                StdDraw.line(x.p.x(), rect.ymin(), x.p.x(), rect.ymax());
                StdDraw.setPenColor(StdDraw.BLACK);
                x.p.draw();
                draw(x.lb, depth + 1, new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax()));
                draw(x.rt, depth + 1, new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax()));                
            }
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pArray = new Queue<Point2D>();
        return range(root, rect, 1, pArray);
    }
    private Queue<Point2D> range(Node x, RectHV rect, int depth, Queue<Point2D> q) {
        if (x != null) {
            if (rect.contains(x.p)) {
                q.enqueue(x.p);
                q = range(x.lb, rect, depth + 1, q);
                q = range(x.rt, rect, depth + 1, q);                
            }
            else {
                if (depth % 2 == 0) {
                    if (rect.ymin() > x.p.y()) {
                        //top
                        q = range(x.rt, rect, depth + 1, q);
                    }                   
                    else if (rect.ymax() < x.p.y()) {
                        //left
                        q = range(x.lb, rect, depth + 1, q);
                    }
                    else {
                        q = range(x.lb, rect, depth + 1, q);
                        q = range(x.rt, rect, depth + 1, q);                    
                    }
                }
                else {
                    if (rect.xmin() > x.p.x()) {
                        //right
                        q = range(x.rt, rect, depth + 1, q);
                    }
                    else if (rect.xmax() < x.p.x()) {
                        //left
                        q = range(x.lb, rect, depth + 1, q);
                    }
                    else {
                        //both
                        q = range(x.lb, rect, depth + 1, q);
                        q = range(x.rt, rect, depth + 1, q);                    
                    }                
                }
            }
        } 
        return q;
    }    
    
    public Point2D nearest(Point2D p) {
        Point2D pMin = null;
        pMin = nearest(root, p, pMin, 1);
        return pMin;
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D pMin, int depth) {
        if (x != null) {
            if (pMin == null) {
                pMin = x.p;
            }            
            else if (x.p.distanceSquaredTo(p) < pMin.distanceSquaredTo(p)) {
                pMin = x.p;
            }
            if (depth % 2 == 0) {
                if (p.y() > x.p.y()) {
                    // query point is on the top
                    if (x.rt != null) {
                        pMin = nearest(x.rt, p, pMin, depth + 1);
                    }
                    if ((x.lb != null) && (pMin.distanceSquaredTo(p) > (p.y()-x.p.y())*(p.y()-x.p.y()))) {
                        pMin = nearest(x.lb, p, pMin, depth + 1);
                    }
                }
                else {
                    if (x.lb != null) {
                        pMin = nearest(x.lb, p, pMin, depth + 1);
                    } 
                    if ((x.rt != null) && (pMin.distanceSquaredTo(p) > (p.y()-x.p.y())*(p.y()-x.p.y()))) {
                        pMin = nearest(x.rt, p, pMin, depth + 1);
                    }
                }
            }
            else {
                if (p.x() > x.p.x()) {
                    // query point is on the right side
                    if (x.rt != null) {
                        pMin = nearest(x.rt, p, pMin, depth + 1);
                    }
                    if ((x.lb != null) && (pMin.distanceSquaredTo(p) > (p.x()-x.p.x())*(p.x()-x.p.x()))) 
                    {
                        pMin = nearest(x.lb, p, pMin, depth + 1);                  
                    }
                
                }
                else {
                    if (x.lb != null) {
                        pMin = nearest(x.lb, p, pMin, depth + 1);
                    }               
                    if ((x.rt != null) && (pMin.distanceSquaredTo(p) > (p.x()-x.p.x())*(p.x()-x.p.x()))) 
                    {  
                        pMin = nearest(x.rt, p, pMin, depth + 1);
                    }
                }
            }
            
        }
        return pMin;
    }

    public static void main(String[] args) {
        //RectHV rect = new RectHV(0, 0, 0.5, 0.6);
        KdTree kdtree = new KdTree();
        StdOut.println("isEmpty? "+kdtree.isEmpty());
        StdOut.println("size: "+kdtree.size());
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            kdtree.insert(new Point2D(x, y));
            
        }
        StdOut.println("isEmpty? "+ kdtree.isEmpty());
        StdOut.println("size: "+ kdtree.size());
        StdOut.println("contains "+ kdtree.contains(new Point2D(0.5, 0.3)));
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(.005);
        kdtree.draw();
        //Iterable<Point2D> pArray = kdtree.range(rect);
        //StdDraw.setPenColor(StdDraw.RED);
        //StdDraw.setPenRadius(.005);
        //rect.draw();
        /**
        int count = 0;
        for (Point2D p : pArray) {
            count++;
            p.draw();
        }
        */
        //StdOut.println("count: "+count);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.01);
        Point2D pp = kdtree.nearest(new Point2D(0.9, 0.1));
        if (pp != null) 
        {
            pp.draw();
            StdOut.println("nearest point: ("+pp.x()+ " , "+pp.y()+")");
        }
    }
}
