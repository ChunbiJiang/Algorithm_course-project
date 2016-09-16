//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    //private Color[][] color;
    private int[][]rgb;
    private int width;
    private int height;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        //this.picture = picture;
        this.height = picture.height();
        this.width = picture.width();
        rgb = new int [width][height];
        //color = new Color[width][height];
        
        //rgb = new int [width][height][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb[i][j] = picture.get(i, j).getRGB();
            }
        }        
    }
    // current picture
    public Picture picture() {
        Picture picture1 = new Picture(width, height);
        Color color;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color = new Color(rgb[i][j]);
                picture1.set(i, j, color);
            }
        }
        return picture1;       
    }
    // width of current picture
    public     int width() {
        return width;
    }  
    
    // height of current picture
    public     int height() {
        return height;
    } 
    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        if (x < 0 || x >= width())  throw new IndexOutOfBoundsException("col must be between 0 and " + (width()-1) + ": " + x);
        if (y < 0 || y >= height()) throw new IndexOutOfBoundsException("row must be between 0 and " + (height()-1) + ": " + y);
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;
        Color color1, color2, color3, color4;
        color1 = new Color(rgb[x + 1][y]);
        color2 = new Color(rgb[x - 1][y]);
        color3 = new Color(rgb[x][y + 1]);
        color4 = new Color(rgb[x][y - 1]);
        double Rx = color1.getRed()-color2.getRed();
        double Gx = color1.getGreen()-color2.getGreen();
        double Bx = color1.getBlue()-color2.getBlue();
        
        double Ry = color3.getRed()-color4.getRed();
        double Gy = color3.getGreen()-color4.getGreen();
        double By = color3.getBlue()-color4.getBlue();
        
        return Math.sqrt(Rx * Rx + Gx * Gx + Bx * Bx + Ry * Ry + Gy * Gy + By * By);
        
    } 
    
    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {
        double[][] venergyTo = new double[width][height];
        int[][] vpixelTo = new int[width][height];
        double temp;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    venergyTo[j][i] = 1000;
                    vpixelTo[j][i] = j;
                }
                else
                    venergyTo[j][i] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                
                if (j == 0) {
                    if (width == 1) {
                        temp = venergyTo[j][i] + energy(j, i + 1);
                        if (temp < venergyTo[j][i + 1]) {
                            venergyTo[j][i + 1] = temp;
                            vpixelTo[j][i + 1] = j;
                        }
                    }
                    else {
                        temp = venergyTo[j][i] + energy(j, i + 1);
                        if (temp < venergyTo[j][i + 1]) {
                            venergyTo[j][i + 1] = temp;
                            vpixelTo[j][i + 1] = j;
                        }
                        temp = venergyTo[j][i] + energy(j + 1, i + 1);
                        if (temp < venergyTo[j + 1][i + 1]) {
                            venergyTo[j + 1][i + 1] = temp;
                            vpixelTo[j + 1][i + 1] = j;
                        }
                    }
                
                }
                else if (j == width - 1) {
                    temp = venergyTo[j][i] + energy(j, i + 1);
                    if (temp < venergyTo[j][i + 1]) {
                        venergyTo[j][i + 1] = temp;
                        vpixelTo[j][i + 1] = j;
                    }
                    temp = venergyTo[j][i] + energy(j - 1, i + 1);
                    if (temp < venergyTo[j - 1][i + 1]) {
                        venergyTo[j - 1][i + 1] = temp;
                        vpixelTo[j - 1][i + 1] = j;
                    }
                }
                else {
                    temp = venergyTo[j][i] + energy(j, i + 1);
                    if (temp < venergyTo[j][i + 1]) {
                        venergyTo[j][i + 1] = temp;
                        vpixelTo[j][i + 1] = j;
                    }
                    temp = venergyTo[j][i] + energy(j - 1, i + 1);
                    if (temp < venergyTo[j - 1][i + 1]) {
                        venergyTo[j - 1][i + 1] = temp;
                        vpixelTo[j - 1][i + 1] = j;
                    }
                    
                    temp = venergyTo[j][i] + energy(j + 1, i + 1);
                    if (temp < venergyTo[j + 1][i + 1]) {
                        venergyTo[j + 1][i + 1] = temp;
                        vpixelTo[j + 1][i + 1] = j;
                    }
                }
            }
        }
        
        double minenergy = venergyTo[0][height - 1];
        int j_min = 0;
        for (int j = 1; j < width; j++) {
            if (minenergy > venergyTo[j][height - 1]) {
                j_min = j;
                minenergy = venergyTo[j][height - 1];
            }
        }
                    
        int[] result = new int[height];
        result[height - 1] = j_min;
        for (int i = height - 2; i >= 0; i--) {
            result[i] = vpixelTo[j_min][i + 1];
            j_min = result[i];
        }
        return result;    
    } 
    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam) {
       if (width <= 1) {
            //StdOut.println("width <=1");
            throw new java.lang.IllegalArgumentException();
        }
        if (seam.length == 0)  throw new java.lang.NullPointerException();
        if (seam.length != height) throw new java.lang.IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= width) throw new java.lang.IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width)
                throw new java.lang.IllegalArgumentException();
                
            if (seam[i] - seam[i - 1] >= 2 || seam[i - 1] - seam[i] >= 2)
                throw new java.lang.IllegalArgumentException();
        }
            
            
        for (int i = 0; i < height; i++) {
            for (int j = seam[i]; j < width - 1; j++) {
                rgb[j][i] = rgb[j + 1][i];
            }
        }
        width--;
    } 
    
    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        double[][] henergyTo = new double[width][height];
        int[][] hpixelTo = new int[width][height];
        double temp;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0) {
                    henergyTo[i][j] = 1000;
                    hpixelTo[i][j] = i;
                }
                else
                    henergyTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height; j++) {
                
                if (j == 0) {
                    if (height == 1) {
                        temp = henergyTo[i][j] + energy(i + 1, j);
                        if (temp < henergyTo[i + 1][j]) {
                            henergyTo[i + 1][j] = temp;
                            hpixelTo[i + 1][j] = j;
                        }
                    }
                    else {
                        temp = henergyTo[i][j] + energy(i + 1, j);
                        if (temp < henergyTo[i + 1][j]) {
                            henergyTo[i + 1][j] = temp;
                            hpixelTo[i + 1][j] = j;
                        }
                        temp = henergyTo[i][j] + energy(i + 1, j + 1);
                        if (temp < henergyTo[i + 1][j + 1]) {
                            henergyTo[i + 1][j + 1] = temp;
                            hpixelTo[i + 1][j + 1] = j;
                        }
                    }                
                }
                else if (j == height - 1) {
                    temp = henergyTo[i][j] + energy(i + 1, j);
                    if (temp < henergyTo[i + 1][j]) {
                        henergyTo[i + 1][j] = temp;
                        hpixelTo[i + 1][j] = j;
                    }
                    temp = henergyTo[i][j] + energy(i + 1, j -1);
                    if (temp < henergyTo[i + 1][j -1]) {
                        henergyTo[i + 1][j - 1] = temp;
                        hpixelTo[i + 1][j - 1] = j;
                    }
                }
                else {
                    temp = henergyTo[i][j] + energy(i + 1, j);
                    if (temp < henergyTo[i + 1][j]) {
                        henergyTo[i + 1][j] = temp;
                        hpixelTo[i + 1][j] = j;
                    }
                    temp = henergyTo[i][j] + energy(i + 1, j + 1);
                    if (temp < henergyTo[i + 1][j + 1]) {
                        henergyTo[i + 1][j + 1] = temp;
                        hpixelTo[i + 1][j + 1] = j;
                    }
                    temp = henergyTo[i][j] + energy(i + 1, j -1);
                    if (temp < henergyTo[i + 1][j - 1]) {
                        henergyTo[i + 1][j - 1] = temp;
                        hpixelTo[i + 1][j - 1] = j;
                    }
                }
            }
        }
        
        double minenergy = henergyTo[width - 1][0];
        int j_min = 0;
        for (int j = 1; j < height; j++) {
            if (minenergy > henergyTo[width - 1][j]) {
                j_min = j;
                minenergy = henergyTo[width - 1][j];
            }
        }
                    
        int[] result = new int[width];
        result[width - 1] = j_min;
        for (int i = width - 2; i >= 0; i--) {
            result[i] = hpixelTo[i + 1][j_min];
            j_min = result[i];
            //StdOut.println(result[i]);
        }
        return result;    
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam) {
        if (seam.length == 0)  throw new java.lang.NullPointerException();
        if (height <= 1) {
            throw new java.lang.IllegalArgumentException();
        }
        if (seam.length != width) throw new java.lang.IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= height) throw new java.lang.IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height)
                throw new java.lang.IllegalArgumentException();
            if (seam[i] - seam[i - 1] >= 2 || seam[i - 1] - seam[i] >= 2)
                throw new java.lang.IllegalArgumentException();
        }
            
        for (int i = 0; i < width; i++) {
            for (int j = seam[i]; j < height - 1; j++) {
                rgb[i][j] = rgb[i][j + 1];
            }
        }
        height--;
    } 
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        //picture.show();
        SeamCarver seamcarver = new SeamCarver(picture);
        //StdOut.println(seamcarver.width());
        //StdOut.println(seamcarver.height());
        
        
        int[] result = seamcarver.findVerticalSeam();
        for (int i = 0; i < result.length; i++)
            StdOut.print(" "+ result[i]);
        StdOut.println();
        
        seamcarver.removeVerticalSeam(result);
    }
       
}