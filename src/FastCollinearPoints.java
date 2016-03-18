import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;


public class FastCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] segments = new LineSegment[2];
    private Point[] checkPoints;
    private Point[] originalPoints;

    public FastCollinearPoints(Point[] points) {

        originalPoints = points;
        eval();
    }    // finds all line segments containing 4 or more points


    private void eval() {
        if (originalPoints == null) {
            throw new NullPointerException();
        }
        if (originalPoints.length == 0) return;

        Function<Integer, Point[]> integerFunction = (s) -> new Point[s];
        Point[] points = arrayCopy(originalPoints, integerFunction);
        this.checkPoints = points;
        Point[] copyPoints = arrayCopy(originalPoints,integerFunction);
        int z = 0;
        Point point = originalPoints[z];
        if(point==null){
            while (z!=originalPoints.length-1){
                point = originalPoints[z];
                if(point!=null) break;
            }
        }
        Comparator<Point> pointComparator = newComparator(point.slopeOrder());
        Comparator<Point> modifiedComparator = (p1, p2) -> {
            if (p1.compareTo(p2) == 0) {
                throw new IllegalArgumentException();
            }
            return pointComparator.compare(p1, p2);
        };
        findSegmentsForPoint(point, copyPoints, modifiedComparator);
        for (int i = 1; i < originalPoints.length; i++) {
            point = originalPoints[i];
            if(point==null)continue;
            findSegmentsForPoint(point, copyPoints, newComparator(point.slopeOrder()));
        }
        trimSegments();
    }


    private <T> T[] arrayCopy(T[] initial, Function<Integer,T[]> arrayCreator) {
        T[] objects = arrayCreator.apply(initial.length);
        int counter = 0;
        for (T o : initial) {
            if(o==null) continue;
            objects[counter++] = o;
        }
        if(counter<initial.length-1){
            objects = Arrays.copyOf(objects, counter + 1);
        }
        return objects;
    }


    private Comparator<Point> newComparator(Comparator<Point> comparator) {
        return (p1, p2) -> {
            int compare = comparator.compare(p1, p2);
            if (compare == 0) {
                compare = p1.compareTo(p2);
            }
            return compare;
        };
    }

    private void findSegmentsForPoint(Point basePoint, Point[] points, Comparator<Point> comparator) {
        Arrays.sort(points, comparator);
        double initialSlop = Double.NEGATIVE_INFINITY;
        double slop = initialSlop;
        Point first = null;
        byte counter = 0;
        for (int i = 1; i < points.length; i++) {
            Point point = points[i];
            double nSlop = basePoint.slopeTo(point);
            boolean compare = Double.compare(slop, nSlop) == 0 || Double.compare(slop, initialSlop) == 0;
            if (compare) {
                counter++;
            }
            boolean lastEl = i == points.length - 1;
            if (!compare || lastEl) {
                if (counter >= 3) {
                    if (basePoint.compareTo(first) < 0) {
                        first = basePoint;
                        Point second = null;
                        if(lastEl && compare){
                            second = point;
                        }else {
                            second =  points[i - 1];
                        }
                        addSegments(new LineSegment(first, second));
                    }
                }
                counter = 1;
            }
            if (counter == 1) {
                first = point;
            }
            slop = nSlop;
        }
    }

    private void addSegments(LineSegment segment) {
        if (numberOfSegments == segments.length) resizeLineSegments(2 * segments.length);
        segments[numberOfSegments++] = segment;
    }

    private void trimSegments() {
        if (segments == null) return;
        if (segments.length > numberOfSegments) {
            resizeLineSegments(numberOfSegments);
        }
    }

    private void resizeLineSegments(int capacity) {
        assert capacity >= numberOfSegments;
        LineSegment[] temp = new LineSegment[capacity];
        System.arraycopy(segments, 0, temp, 0, numberOfSegments);
        segments = temp;
    }


    public int numberOfSegments() {
        if(!Arrays.equals(checkPoints,originalPoints)){
            eval();
        }
        return numberOfSegments;
    }       // the number of line segments

    public LineSegment[] segments() {
        if(!Arrays.equals(checkPoints,originalPoints)){
            eval();
        }
        return arrayCopy(segments,(s) ->new LineSegment[s]);
    }             // the line segments
}