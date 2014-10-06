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

    public void rename(int from, int to) {
        if(this.receiveOn == from) { this.sendOn = to; }
         // Do not rename bindTo, in accordance with the semantics of the pi
         // calculus. Only rename in the subprocess if bindTo was not equal to
         // from.
        if(this.bindTo != from) {
            this.subprocess.rename(from, to);
        }
    }
}
