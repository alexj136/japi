package runsyntax;

public class Replicate extends Term {

    private Term toReplicate;

    public Replicate(Term toReplicate) {
        this.toReplicate = toReplicate;
    }

    public void rename(int from, int to) {
        this.toReplicate.rename(from, to);
    }
}
