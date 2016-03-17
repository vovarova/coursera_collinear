import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] segments = new LineSegment[2];

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        if (points.length == 0) return;
        Point point = points[0];
        Comparator<Point> pointComparator = point.slopeOrder();
        Comparator<Point> modifiedComparator = (p1, p2) -> {
            if (p1.compareTo(p2)==0){
                throw new IllegalStateException();
            }
            return pointComparator.compare(p1, p2);
        };
        findSegmentsForPoint(point, points, modifiedComparator);
        for (int i = 1; i < points.length; i++) {
            point = points[i];
            findSegmentsForPoint(point, points, point.slopeOrder());
        }
        trimSegments();
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }       // the number of line segments

    public LineSegment[] segments() {
        return segments;
    }             // the line segments


    private void findSegmentsForPoint(Point basePoint, Point[] initArray, Comparator<Point> comparator) {
        Point[] points = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(points, comparator);
        double initialSlop = Double.NEGATIVE_INFINITY;
        double slop = initialSlop;
        Point first = null;
        byte counter = 0;
        for (int i = 1; i < points.length; i++) {
            Point point = points[i];
            double nSlop = basePoint.slopeTo(point);


            if (Double.compare(slop, nSlop) == 0 || Double.compare(slop, initialSlop) == 0) {
                counter++;
            } else {
                if (counter >= 2) {
                    Point second = points[i - 1];
                   if (basePoint.compareTo(first) < 0) {
                        first = basePoint;
                    }
                    if (basePoint.compareTo(second) > 0) {
                        second = basePoint;
                    }
                    addSegments(new LineSegment(first, second));
                }
                counter = 1;
            }
            if (counter == 1) {
                first = point;
                i = i + 1;
            }
            if (i == points.length-1) {
                if (counter >= 2) {
                    Point second = points[i - 1];
                   /*if (basePoint.compareTo(first) < 0) {
                        first = basePoint;
                    }
                    if (basePoint.compareTo(second) > 0) {
                        second = basePoint;
                    }*/
                    addSegments(new LineSegment(first, second));
                }
            }
            slop = nSlop;
        }
    }

    private void addSegments(LineSegment segment) {
        if (numberOfSegments == segments.length) resize(2 * segments.length);
        segments[numberOfSegments++] = segment;
    }

    private void trimSegments() {
        if(segments == null) return;
        if (segments.length > numberOfSegments) {
            resize(numberOfSegments);
        }
    }


    private void resize(int capacity) {
        assert capacity >= numberOfSegments;
        LineSegment[] temp = new LineSegment[capacity];
        System.arraycopy(segments, 0, temp, 0, numberOfSegments);
        segments = temp;
    }


    public static void main(String[] args) {
        // read the N points from a file
        In in = new In("D:/collinear/input6.txt");
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setPenRadius(.06);
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        StdDraw.setPenRadius(.01);
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
               segment.draw();
        }
    }
}