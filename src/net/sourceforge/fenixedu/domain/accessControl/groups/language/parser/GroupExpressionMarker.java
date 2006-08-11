package net.sourceforge.fenixedu.domain.accessControl.groups.language.parser;

import java.io.Serializable;

/**
 * This class represents an expression marked delimiting a specific region of
 * the group expression. It can represent a single point of the expression to
 * mark a character, for example, or a wide region of the expression enclosing
 * multiple elements.
 * 
 * @author cfgi
 */
public class GroupExpressionMarker implements Serializable {

    private static final long serialVersionUID = 1L;

    private Point start;
    private Point end;

    /**
     * Creates a new marker with two invalid points.
     * 
     */
    public GroupExpressionMarker() {
        super();

        this.start = new Point();
        this.end = new Point();
    }

    public GroupExpressionMarker(Point start) {
        super();

        this.start = start;
        this.end = new Point();
    }

    public GroupExpressionMarker(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public GroupExpressionMarker(int startLine, int startColumn) {
        this(new Point(startLine, startColumn));
    }

    public GroupExpressionMarker(int startLine, int startColumn, int endLine, int endColumn) {
        this(new Point(startLine, startColumn), new Point(endLine, endColumn));
    }

    public Point getStart() {
        return this.start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return this.end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * This stucture represents a point in the expression. A point is just a
     * line number and a column number.
     * 
     * @author cfgi
     */
    public static class Point implements Comparable<Point> {

        /**
         * Represents an invalid value of a line or column.
         */
        public static final int NONE = -1;

        private int line;
        private int column;

        /**
         * Creates an invalid point.
         * 
         * @see #isValid()
         */
        public Point() {
            super();

            this.line = NONE;
            this.column = NONE;
        }

        public Point(int line, int column) {
            super();

            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return this.line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int getColumn() {
            return this.column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        /**
         * Checks if this point is valid, that is, if both the line and column
         * contain valid values.
         * 
         * @return <code>true</code> if the line and column contain an usable
         *         value
         */
        public boolean isValid() {
            return getLine() != NONE && getColumn() != NONE;
        }

        /**
         * Compares to another point. An invalid point is smaller than a valid
         * point.
         * 
         * @return -1, 0, or 1 respective the contract of the {@link Comparable}
         */
        public int compareTo(Point other) {
            if (getLine() < other.getLine()) {
                return -1;
            }

            if (getLine() == other.getLine() && getColumn() < other.getColumn()) {
                return -1;
            }
            
            if (getLine() == other.getLine() && getColumn() == other.getColumn()) {
                return 0;
            }

            return 1;
        }

        /**
         * Compares all points and chooses the smaller point that is valid.
         * 
         * @param points
         *            the points to compare
         * 
         * @return the smaller valid point if any or an invalid point
         */
        public static Point smaller(Point... points) {
            return select(1, points);
        }

        /**
         * Compares all points and chooses the greater point that is valid.
         * 
         * @param points
         *            the points to compare
         *            
         * @return the greater valid point if any or an invalid point
         */
        public static Point greater(Point... points) {
            return select(-1, points);
        }

        private static Point select(int value, Point... points) {
            Point result = new Point();

            for (int i = 0; i < points.length; i++) {
                Point point = points[i];

                if (result.isValid()) {
                    if (point.isValid() && result.compareTo(point) == value) {
                        result = point;
                    }
                } else {
                    result = point;
                }
            }

            return result;
        }

    }

}
