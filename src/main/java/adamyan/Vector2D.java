package adamyan;

/**
 * This represents a vector (like a position) in 2D space.
 * <p>
 * Note: adamyan.Vector2D objects are immutable (record classes) => when it is modified, a new object is created
 * @param x the x coordinate of the vector
 * @param y the y coordinate of the vector
 */
public record Vector2D(double x, double y) {

    /**
     * @param scalar
     * @return a new Vector
     */
    public Vector2D scale(double scalar) {
        return new Vector2D(
                x * scalar,
                y * scalar
        );
    }

    /**
     * @return the magnitude (=length) of the vector
     */
    public double magnitude() {
        return Math.hypot(x, y);
    }

    /**
     * @return the unit vector (= new vector with the same direction but a length of 1)
     */
    public Vector2D normalized() {
        return scale(1 / magnitude());
    }
}
