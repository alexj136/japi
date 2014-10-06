package runsyntax;

public class Receive extends Term {

    private int receiveOn;
    private int bindTo;
    private Term subprocess;

    public Receive(int receiveOn, int bindTo, Term subprocess) {
        this.receiveOn = receiveOn;
        this.bindTo = bindTo;
        this.subprocess = subprocess;
    }
}
