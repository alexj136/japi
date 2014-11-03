# Japi

Japi is a Java implementation of the pi calculus. More specifically, the
synchronous, monadic pi calculus.

## Usage

To build, `cd` into the root directory of the repository and run:

    ./build

To clean the build, run:

    ./build clean

To invoke the interpreter, first build, and then run:

    ./japi my_src_file

The syntax of source files is as shown:

    P, Q ::= X < Y_1, Y_2, ... Y_n > . P    # Send prefix, n <= 0
           | X ( Y_1, Y_2, ... Y_n ) . P    # Receive prefix, n <= 0
           | [ P_1 | P_2 | ... P_n ]        # N-ary parallel composition, n <= 0
           | new X in P                     # Name restriction
           | ! P                            # Replication
           | { P_1 + P_2 + ... P_n }        # N-ary nondeterministic choice, n <= 2
           | 0                              # Completed process

    X, Y ::= [a-z]+

## Structural Congruences

    !(P|Q) = (!P)|(!Q)
     !(!P) = !P
        !0 = 0
