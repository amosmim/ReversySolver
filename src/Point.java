/**
 *  represent simple point in 2D.
 *  note: I use this class in ex1 without changes...
 */
public class Point {
    private int x,y;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter to x
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * getter to y
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * hashcode override
     * @return
     */
    @Override
    public int hashCode() {
        return (x+1)*100000 + (1+y);
    }

    /**
     * equals override
     * @param other
     * @return
     */
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Point))return false;
        Point otherMyClass = (Point)other;
        return ((this.x == otherMyClass.getX()) && (this.y == otherMyClass.getY()));
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return ("x:" + this.getX() + "; y:" + this.getY() + ";");
    }
}
