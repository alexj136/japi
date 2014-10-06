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

    P, Q ::= receive Y over X then P
           | send Y over X then P
           | concurrently P and Q
           | restricting X in P
           | repeatedly P
           | end

    X, Y ::= [a-z]+
