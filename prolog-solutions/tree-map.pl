empty(node(null, null, null, null, 0, 0)).
node(K, V, node(K, V, E, E, 1, 1)) :- empty(E).
node(K, V, L, R, node(K, V, L, R, S, H)) :-
		size(L, LS), size(R, RS), S is LS + RS + 1,
		height(L, LH), height(R, RH), max(LH, RH, MH), H is MH + 1.

max(A, B, A) :- A >= B, !.
max(A, B, B) :- B > A.

key   (node(K, _, _, _, _, _), K).
value (node(_, V, _, _, _, _), V).
left  (node(_, _, L, _, _, _), L).
right (node(_, _, _, R, _, _), R).
size  (node(_, _, _, _, S, _), S).
height(node(_, _, _, _, _, H), H).

bfactor(node(_, _, L, R, _, _), Res) :- height(L, LH), height(R, RH), Res is RH - LH.
balance(N, Res) :- bfactor(N, 2), right(N, R), bfactor(R, RB), RB < 0, !,
								   rotate_right(R, NR), key(N, K), value(N, V), left(N, L), node(K, V, L, NR, Res).
balance(N, Res) :- bfactor(N, 2), right(N, R), bfactor(R, RB), RB >= 0, !, rotate_left(N, Res).

balance(N, Res) :- bfactor(N, -2), left(N, L), bfactor(L, LB), LB > 0, !,
									 rotate_left(L, NL), key(N, K), value(N, V), right(N, R), node(K, V, NL, R, Res).
balance(N, Res) :- bfactor(N, -2), left(N, L), bfactor(L, LB), LB =< 0, !, rotate_right(N, Res).
balance(N, N).

rotate_right(node(K, V, node(LK, LV, LL, LR, _, _), R, _, _), Res) :- node(K, V, LR, R, NR), node(LK, LV, LL, NR, Res).
rotate_left (node(K, V, L, node(RK, RV, RL, RR, _, _), _, _), Res) :- node(K, V, L, RL, NL), node(RK, RV, NL, RR, Res).

map_put(node(null, _, _, _, _, _), K,  V,  Res) :- !, node(K, V, Res).
map_put(node(K, _, L, R, _, _)   , K,  V,  Res) :- !, node(K, V, L, R, Res).
map_put(node(K, V, L, R, _, _), NK, NV, Res) :- empty(L), K > NK, !, node(NK, NV, NL), node(K, V, NL, R, Res).
map_put(node(K, V, L, R, _, _), NK, NV, Res) :- empty(R), K < NK, !, node(NK, NV, NR), node(K, V, L, NR, Res).
map_put(node(K, V, L, R,    _, _), NK, NV, Res) :- K > NK, !,
		map_put(L, NK, NV, NL), node(K, V, NL, R, Res1), balance(Res1, Res).
map_put(node(K, V, L, R,    _, _), NK, NV, Res) :- K < NK, !,
		map_put(R, NK, NV, NR), node(K, V, L, NR, Res1), balance(Res1, Res).

map_get(node(null, _, _, _, _, _), _, _) :- !, fail.
map_get(node(K, V, _, _, _, _), K,  V)  :- !.
map_get(node(K, _, L, _, _, _), SK, SV) :- SK < K, !, map_get(L, SK, SV).
map_get(node(K, _, _, R, _, _), SK, SV) :- SK > K, !, map_get(R, SK, SV).

get_min(node(K, V, L, _, _, _), K, V) :- empty(L), !.
get_min(node(_, _, L, _, _, _), K, V) :- get_min(L, K, V).

del_min(E) :- empty(E), !.
del_min(node(_, _, L, R, _, _), R) :- empty(L), !.
del_min(node(K, V, L, R, _, _), Res) :- del_min(L, NL), node(K, V, NL, R, Res1), balance(Res1, Res).

map_remove(E, K, E) :- empty(E), !.
map_remove(node(K, _, L, R, _, _), K, L) :-empty(R), !.
map_remove(node(K, _, L, R, _, _), K, Res)  :- !, get_min(R, KM, VM), del_min(R, NR),
		node(KM, VM, L, NR, Res1), balance(Res1, Res).
map_remove(node(K, V, L, R, _, _), DK, Res) :- empty(L), K > DK, !, node(K, V, L, R, Res).
map_remove(node(K, V, L, R, _, _), DK, Res) :- empty(R), K < DK, !, node(K, V, L, R, Res).
map_remove(node(K, V, L, R, _, _), DK, Res) :- K > DK, !,
		map_remove(L, DK, NL), node(K, V, NL, R, Res1), balance(Res1, Res).
map_remove(node(K, V, L, R, _, _), DK, Res) :- K < DK, !,
		map_remove(R, DK, NR), node(K, V, L, NR, Res1), balance(Res1, Res).

map_build([], E) :- empty(E).
map_build([(K, V) | T], R) :- map_build(T, R1), map_put(R1, K, V, R).

take_from(E, _, 0) :- empty(E), !.
take_from(node(K, _, L, R, _, _), From, S) :- K >= From, !, size(R, SR), take_from(L, From, SL), S is SL + SR + 1.
take_from(node(K, _, _, R, _, _), From, S) :- K < From,  !, take_from(R, From, S).

take_to(E, _, 0) :- empty(E), !.
take_to(node(K, _, L, R, _, _), To, S) :- K < To,  !, size(L, SL), take_to(R, To, SR), S is SL + SR + 1.
take_to(node(K, _, L, _, _, _), To, S) :- K >= To, !, take_to(L, To, S).

map_subMapSize(N, From, To, 0) :- empty(N), !.
map_subMapSize(node(K, _, L, _, _, _), From, To, S) :- K >= To,  !, map_subMapSize(L, From, To, S).
map_subMapSize(node(K, _, _, R, _, _), From, To, S) :- K < From, !, map_subMapSize(R, From, To, S).
map_subMapSize(node(_, _, L, R, _, _), From, To, S) :- take_to(R, To, SR), take_from(L, From, SL), S is SL + SR + 1.