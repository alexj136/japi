package utils;

public class Triple<T, U, V> extends Pair<T, U> {

    public final V thrd;

    /**
     * Construct a new Triple.
     * @param frst the first element
     * @param scnd the second element
     * @param thrd the third element
     */
    protected Triple(T frst, U scnd, V thrd) {
        super(frst, scnd);
        this.thrd = thrd;
    }

    /**
     * Static constructor for Triples. Removes the need for the lengthy type
     * name in the constructor call.
     * @param frst the first element
     * @param scnd the second element
     * @param thrd the third element
     * @return a triple (frst, scnd, thrd) that is compile-time type checked
     */
    public static <T, U, V> Triple<T, U, V> make(T frst, U scnd, V thrd) {
        return new Triple<T, U, V>(frst, scnd, thrd);
    }

    /**
     * Construct a Triple with the scnd and thrd elements of this Triple and a
     * new frst element.
     * @param frst the new frst element
     * @return a new triple with the same scnd and thrd elements as this one,
     * and a new frst element
     */
    @Override
    public Triple<T, U, V> withFrst(T frst) {
        return new Triple<T, U, V>(frst, this.scnd, this.thrd);
    }

    /**
     * Construct a Triple with the frst and thrd elements of this Triple and a
     * new scnd element.
     * @param scnd the new scnd element
     * @return a new triple with the same frst and thrd elements as this one,
     * and a new scnd element
     */
    @Override
    public Triple<T, U, V> withScnd(U scnd) {
        return new Triple<T, U, V>(this.frst, scnd, this.thrd);
    }

    /**
     * Construct a Triple with the frst and scnd elements of this Triple and a
     * new thrd element.
     * @param thrd the new thrd element
     * @return a new triple with the same frst and scnd elements as this one,
     * and a new thrd element
     */
    public Triple<T, U, V> withThrd(V thrd) {
        return new Triple<T, U, V>(this.frst, this.scnd, thrd);
    }
}
