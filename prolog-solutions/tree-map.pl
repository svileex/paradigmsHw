ll_rev([(XK, XV), [(YK, YV), A, B, _], C, _], Res) :-
	update_tree_height([(XK, XV), B, C, -1], NXH),
	update_tree_height([(YK, YV), A, NXH, -1], Res).

lr_rev([(XK, XV), [(YK, YV), A, [(ZK, ZV), B, C, _], _], D, _], Res) :-
	update_tree_height([(YK, YV), A, B, -1], NYH),
	update_tree_height([(XK, XV), C, D, -1], NXH),
	update_tree_height([(ZK, ZV), NYH, NXH, -1], Res).

rr_rev([(XK, XV), A, [(YK, YV), B, C, _], _], Res) :-
	update_tree_height([(XK, XV), A, B, -1], NXH),
	update_tree_height([(YK, YV), NXH, C, -1], Res).

rl_rev([(XK, XV), A, [(YK, YV), [(ZK, ZV), B, C, _], D, _], _], Res) :-
	update_tree_height([(XK, XV), A, B, 1], NXH),
	update_tree_height([(YK, YV), C, D, 1], NYH),
	update_tree_height([(ZK, ZV), NXH, NYH, 1], Res).

get_height([(_, _), _, _, H], H):- !.
get_height([], 0).

balance([(XK, XV), A, B, XH], Res) :-
	get_height(A, AH), get_height(B, BH),
	TEMP is AH - BH, balance(TEMP, [(XK, XV), A, B, XH], Res), !.

balance(-2, [(XK, XV), A, [(YK, YV), B, C, YH], XH], Res) :-
	 get_height(C, CH), get_height(A, AH),
	 TEMP is CH - AH, TEMP = 1,
	 rr_rev([(XK, XV), A, [(YK, YV), B, C, YH], XH], Res), !.

balance(-2, [(XK, XV), A, [(YK, YV), B, C, YH], XH], Res) :- rl_rev([(XK, XV), A, [(YK, YV), B, C, YH], XH], Res), !.

balance(2, [(XK, XV), [(YK, YV), A, B, YH], C, XH], Res) :-
	get_height(A, AH), get_height(C, CH),
	TEMP is CH - AH, TEMP = -1,
	ll_rev([(XK, XV), [(YK, YV), A, B, YH], C, XH], Res), !.

balance(2, [(XK, XV), [(YK, YV), A, B, YH], C, XH], Res) :- ll_rev([(XK, XV), [(YK, YV), A, B, YH], C, XH], Res), !.

balance(A, R) :- update_tree_height(A, R).
max(X, Y, Res) :- (X < Y ->  Res = Y; Res = X).

update_tree_height([], []).
update_tree_height([(XK, XV), [], [], _], [(XK, XV), [], [], 0]) :- !.
update_tree_height([(XK, XV), A, B, _], R) :-
	get_height(A, AH), get_height(B, BH),
	max(AH, BH, MH), TEMP is MH + 1,
	R = [(XK, XV), A, B, TEMP].

map_put([], K, V, R) :- R = [(K, V), [], [], 0], !.
map_put([(XK, _), LT, RT, XH], XK, V, [(XK, V), LT, RT, XH]) :- !.

map_put([(XK, XV), LT, RT, _], K, V, R) :-
	XK < K,
	map_put(RT, K, V, NR),
	balance([(XK, XV), LT, NR, _], R), !.

map_put([(XK, XV), TL, TR, XH], K, V, R) :-
	map_put(TL, K, V, TEMP),
	balance([(XK, XV), TEMP, TR, XH], R).

map_build(L, R) :- map_build(L, [], R).
map_build([], CurrT, CurrT).
map_build([(XK, XV) | T], CurrT, Tree) :- map_put(CurrT, XK, XV, R), map_build(T, R, Tree).

map_get([(XK, XV), _, _, _], XK, XV) :- !.
map_get([(XK, _), _, XR, _], K, V) :- K > XK, map_get(XR, K, V), !.
map_get([(XK, _), XL, _, _], K, V) :- K < XK, map_get(XL, K, V), !.

map_remove([], XK, []).
map_remove([(XK, XV), L, R, XH], XK, Result) :- 
	remove([(XK, XV), L, R, XH], Temp), 
	balance(Temp, Result), !.
	
map_remove([(XK, XV), L, R, XH], K, Result) :- 
	XK < K, 
	map_remove(R, K, Temp),
	balance([(XK, XV), L, Temp, XH], Result), !.

map_remove([(XK, XV), L, R, XH], K, Result) :- 
	map_remove(L, K, Temp),
	balance([(XK, XV), Temp, R, XH], Result), !.

remove([(_, _), [], R, _], Res) :- balance(R, Res), !.
remove([(_, _), L, [], _], Res) :- balance(L, Res), !.
remove([(XK, XV), L, R, _], Res) :-
	remove_left(R, (NXK, NXV), Temp),
	balance([(NXK, NXV), L, Temp, _], Res), !.

remove_left([(XK, XV), [], R, _], (XK, XV), R) :- !.

remove_left([(XK, XV), L, R, _], NV, NTree) :-
	remove_left(L, NV, Temp),
	balance([(XK, XV), Temp, R, -1], NTree), !.

map_getLast([(XK, XV), L, [], XH], (XK, XV)) :- !.
map_getLast([(XK, XV), L, R, XH], (K, V)) :- map_getLast(R, (K, V)), !.

map_removeLast([], []) :- !.
map_removeLast([(XK, XV), L, [], XH], L) :- !.

map_removeLast([(XK, XV), L, R, XH], Result) :-
	map_removeLast(R, TResult),
	balance([(XK, XV), L, TResult, -1], Result), !.