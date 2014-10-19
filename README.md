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

    P, Q ::= X<Y>.P         # Send prefix
           | X(Y).p         # Receive prefix
           | [P|P|...P]     # N-ary parallel composition
           | new X in P     # Name restriction
           | !P             # Replication
           | 0              # Completed process

    X, Y ::= [a-z]+

## Structural Congruences

    !(P|Q) = (!P)|(!Q)
     !(!P) = !P
        !0 = 0
