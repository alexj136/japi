package utils;

public final class Pair<T> {
    
    private final T first;
    private final T second;

    public Pair<T>(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T first() { return this.first; }

    public T second() { return this.second; }
}
