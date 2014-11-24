# Japi

Japi is a Java implementation of the polyadic pi calculus. The calculus includes
typicali pi calculus combinators, such as parallel composition, nondeterministic
choice, replication and, crucially, restriction.

This implementation embeds the lambda calculus into Send prefixes. This allows
agents to pass arbitrary computations to one-another.

## Usage

To build, `cd` into the root directory of the repository and run:

    ./build

To clean the build, run:

    ./build clean

To invoke the interpreter, first build, and then run:

    ./japi my_src_file

The syntax of source files is as shown:

    P, Q ::= X < E_1, E_2, ... E_n > . P    # Send prefix, n <= 0
           | X ( Y_1, Y_2, ... Y_n ) . P    # Receive prefix, n <= 0
           | [ P_1 | P_2 | ... P_n ]        # N-ary parallel composition, n <= 0
           | new X in P                     # Name restriction
           | ! P                            # Replication
           | { P_1 + P_2 + ... P_n }        # N-ary nondeterministic choice, n <= 2
           | 0                              # Completed process

    X, Y ::= [a-z]+

    E    ::= \X.E
           | E_1 E_2
           | X
           | (E)

## Operational Semantics

    COM: ——————————————————————————————————————————————————
          X < E > . P | X ( Y ) . Q -> P | Q { eval(E) / Y }


## Structural Congruences

    !(P|Q) = (!P)|(!Q)
     !(!P) = !P
        !0 = 0

## Operational Semantics For Lambda Expressions

    BETA: ——————————————————
          (\X.M) N -> M{N/X}

           M -> M'
    APP: ———————————
         M N -> M' N
