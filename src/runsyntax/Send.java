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
}
