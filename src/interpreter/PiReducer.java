package interpreter;

import syntax.*;

/**
 * Contains static functions for reducing PiTerms.
 */
public final class PiReducer {

    /**
     * Replace occurrences of the name 'replacing' with the LambdaTerm 'with'
     * within the PiTerm 'in'.
     * @param replacing replace names of this value
     * @param with replaced names are replaced with this LambdaTerm
     * @param in replace inside this PiTerm
     */
    public static void msgPass(Integer replacing, LambdaTerm with, PiTerm in) {

        if(in instanceof Send) {
            Send send = (Send) in;

            if(replacing.equals(send.chnl()) && !(with instanceof Variable)) {
                throw new IllegalArgumentException("Tried to replace a " +
                        "channel name with an expression");
            }
            if(replacing.equals(send.chnl()) && with instanceof Variable) {
                Variable var = (Variable) with;
                send.setChnl(var.name());
            }
            for(int i = 0; i < send.arity(); i++) {
                if(with instanceof Variable) {
                    send.exp(i).renameFree(replacing,
                            ((Variable) with).name());
                }
                else {
                    throw new UnsupportedOperationException("Not yet " +
                            "implemented");
                }
            }
            PiReducer.msgPass(replacing, with, send.subterm());
        }

        else if(in instanceof Receive) {
            Receive rece = (Receive) in;

            if(replacing.equals(rece.chnl()) && !(with instanceof Variable)) {
                throw new IllegalArgumentException("Tried to replace a " +
                        "channel name with an expression");
            }
            else if(replacing.equals(rece.chnl()) && with instanceof Variable) {
                rece.setChnl(((Variable) with).name());
            }
            // Do not rename the message content, in accordance with the
            // semantics of the pi calculus. Only rename in the subprocess if
            // the message did not contain 'from'.
            if(!rece.binds(replacing)) {
                PiReducer.msgPass(replacing, with, rece.subterm());
            }
        }

        else if(in instanceof Replicate) {
            PiReducer.msgPass(replacing, with, ((Replicate) in).subterm());
        }

        else if(in instanceof Restrict) {
            Restrict rest = (Restrict) in;

            if(!rest.boundName().equals(replacing)) {
                PiReducer.msgPass(replacing, with, rest.subterm());
            }
        }

        else if(in instanceof PiTermManySub) {
            PiTermManySub ptms = (PiTermManySub) in;

            for(int i = 0; i < ptms.arity(); i++) {
                PiReducer.msgPass(replacing, with, ptms.subterm(i));
            }
        }

        else {
            throw new IllegalArgumentException("Unrecognised PiTerm type in " +
                    "PiReducer.msgPass");
        }
    }

    public static void msgPass(Integer replacing, LambdaTerm with,
            LambdaTerm in) {

        if(in instanceof Variable) {
            Variable var = (Variable) in;

            if(replacing.equals(var.name())) {
                //var.
            }
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
