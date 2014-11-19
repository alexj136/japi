package utils;

/**
 * Standard two-tuple class using generics.
 */
public class Pair<T, U> {
    
    public final T frst;
    public final U scnd;

    /**
     * Construct a new Pair.
     * @param frst the first element
     * @param scnd the second element
     */
    protected Pair(T frst, U scnd) {
        this.frst = frst;
        this.scnd = scnd;
    }

    /**
     * Static constructor for Pairs. Removes the need for the lengthy type name
     * in the constructor call.
     * @param frst the first element
     * @param scnd the second element
     * @return a pair (frst, scnd) that is compile-time type checked
     */
    public static <T, U> Pair<T, U> make(T frst, U scnd) {
        return new Pair<T, U>(frst, scnd);
    }

    /**
     * Construct a Pair with the scnd element of this Pair and a new frst
     * element.
     * @param frst the new frst element
     * @return a new pair with the same scnd element as this one, and a new frst
     * element
     */
    public Pair<T, U> withFrst(T frst) {
        return new Pair<T, U>(frst, this.scnd);
    }

    /**
     * Construct a Pair with the frst element of this Pair and a new scnd
     * element.
     * @param scnd the new scnd element
     * @return a new pair with the same frst element as this one, and a new scnd
     * element
     */
    public Pair<T, U> withScnd(U scnd) {
        return new Pair<T, U>(this.frst, scnd);
    }
}
