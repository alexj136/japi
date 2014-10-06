package runsyntax;

public class Send extends Term {

    private int sendOn;
    private int toSend;
    private Term subprocess;

    public Send(int sendOn, int toSend, Term subprocess) {
        this.sendOn = sendOn;
        this.toSend = toSend;
        this.subprocess = subprocess;
    }

    public void rename(int from, int to) {
        if(this.sendOn == from) { this.sendOn = to; }
        if(this.toSend == from) { this.toSend = to; }
        this.subprocess.rename(from, to);
    }
}
