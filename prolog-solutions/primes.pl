composite(N) :- min_div(N, _).
% :NOTE: fail
put_comp(N, D) :- \+ composite(N), !, assert(min_div(N, D)).
put_comp(_, _).
make_comp(N, D, L) :- N =< L, put_comp(N, D), N1 is N + D, make_comp(N1, D, L).

build_table(N, L) :- \+ composite(N), N1 is N * N, make_comp(N1, N, L).
build_table(N, L) :- N1 is N + 1, N1 * N1 =< L, build_table(N1, L).

init(N) :- build_table(2, N).
init(_).

prime(N) :- \+ composite(N).

mul_seq(1, []) :- !.
mul_seq(N, [N]) :- !, prime(N).
mul_seq(N, [F, S | T]) :- F =< S, prime(F), mul_seq(N1, [S | T]), N is F * N1.

prime_divisors(1, []) :- !.
prime_divisors(N, T) :- var(N), !, mul_seq(N, T).
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [M | T]) :- min_div(N, M), N1 is N / M, prime_divisors(N1, T).

merge(T, [], T) :- !.
merge([], T, T).
merge([H1 | T1], [H2 | T2], [H2 | R]) :- H1 > H2, !, merge([H1 | T1], T2, R).
merge([H1 | T1], [H2 | T2], [H1 | R]) :- H1 < H2, !, merge(T1, [H2 | T2], R).
merge([H | T1], [H | T2], [H | R]) :- merge(T1, T2, R).

lcm(A, B, LCM) :- prime_divisors(A, L1), prime_divisors(B, L2), merge(L1, L2, R), mul_seq(LCM, R).
