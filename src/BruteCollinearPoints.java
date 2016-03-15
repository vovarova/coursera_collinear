import java.util.Arrays;

public class BruteCollinearPoints {

    public BruteCollinearPoints(Point[] points){
        if(points==null){
            throw new NullPointerException();
        }
        Arrays.sort(points, (p1, p2) -> {
            int res = p1.compareTo(p2);
            if (res ==0){
                throw new IllegalArgumentException ();
            }
            return res;
        });




    }    // finds all line segments containing 4 points
    public           int numberOfSegments() {return  0;}       // the number of line segments
    public LineSegment[] segments()   {return  null;}             // the line segments



}