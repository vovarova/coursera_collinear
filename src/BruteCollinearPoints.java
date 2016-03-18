import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private FastCollinearPoints fastCollinearPoints;

    public BruteCollinearPoints(Point[] points) {
        fastCollinearPoints = new FastCollinearPoints(points);
    }

    public int numberOfSegments() {
        return fastCollinearPoints.numberOfSegments();
    }

    public LineSegment[] segments() {
        return fastCollinearPoints.segments();
    }

    public static void main(String[] args) {
        // read the N points from a file
        In in = new In("D:/collinear/equidistant.txt");
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        //StdDraw.setPenRadius(.06);
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        //StdDraw.setPenRadius(.01);
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        points[0] = null;
        points[3] = null;
        collinear.segments();

    }
}