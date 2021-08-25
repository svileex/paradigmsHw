sieve(X, N) :-
    TEMP is X * X,
    TEMP =< N,
    (prime(X),
     START is TEMP - X,
     for_sieve(X, N, START)
     ; true),
    CURRP is X + 1,
    sieve(CURRP, N), !.

for_sieve(X, N, CURR) :-
    TEMP is CURR + X,
    TEMP =< N,
    assert(d_table(TEMP, X)),
    assert(composite(TEMP)),
    for_sieve(X, N, TEMP).

prime(X) :- \+ composite(X).

init(X) :- sieve(2, X).

prime_divisors(1,[]) :- !.

prime_divisors(X, R) :-
    var(R),
    prime(X),
    R = [X], !.

prime_divisors(X, R) :-
    var(R),
    d_table(X, T),
    TEMP is X / T,
    prime_divisors(TEMP, A),
    append([T], A, R), !.

prime_divisors(X, R) :-
    nonvar(R),
    for_prime(R, 1, X).

for_prime([], CURR, CURR). 

for_prime([H], CURR, ANSW) :-
    prime(H),
    ANSW is CURR * H, !.

for_prime([H, T | TAIL], CURR, ANSW) :-
    prime(H),
    H =< T,
    TEMP is CURR * H,
    for_prime([T | TAIL], TEMP, ANSW).

gcd(A, B, GCD) :-
    prime_divisors(A, ADIV),
    prime_divisors(B, BDIV),
    intersection(ADIV, BDIV, Divisors),
    prime_divisors(GCD, Divisors).

intersection(H, [], []) :- !.
intersection([], H, []) :- !.

intersection([HEAD1 | TAIL1], [HEAD1 | TAIL2], [HEAD1 | CurrArray]) :-
    intersection(TAIL1, TAIL2, CurrArray), !.

intersection([HEAD1 | TAIL1], [HEAD2 | TAIL2], CurrArray) :-
    HEAD1 < HEAD2,
    intersection(TAIL1, [HEAD2 | TAIL2], CurrArray), !.

intersection([HEAD1 | TAIL1], [HEAD2 | TAIL2], RES) :-
    intersection([HEAD1 | TAIL1], TAIL2, RES).