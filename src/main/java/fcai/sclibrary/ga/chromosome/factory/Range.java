package fcai.sclibrary.ga.chromosome.factory;

public class Range<T extends Object> {
    private final T lower;
    private final T upper;

    public Range(T lower, T upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public T getLower() { return lower; }
    public T getUpper() { return upper; }
}