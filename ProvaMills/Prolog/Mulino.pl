%
%
%ALGORITHM
%
%

%PLAY - Entry point for alphabeta pruning
%play(+DEPTH,-MOVES,-STATES)
%play(+STATE,+DEPTH,-MOVES,-STATES)
play(DEPTH,MOVE)  :-
  !,
  initial(STATE),
  play(STATE,DEPTH,MOVE).

play(STATE,DEPTH,MOVE) :-
  alphabeta(STATE,DEPTH,MOVE).

%ALPHABETA - Start the ALGORITHM, max DEPTH level
%alphabeta(+STATE,+DEPTH)
alphabeta(STATE,DEPTH,MOVE) :-
  expand(STATE,max,DEPTH,MOVE).

%EXPAND - Start tree expansion, searching the best move for the current state
%expand(+STATE,+TURN,+DEPTH)
%expand(+STATE,+TURN,+DEPTH,+ALPHA,+BETA,-BEST_MOVE,-BEST_VALUE)
expand(STATE,TURN,DEPTH,MOVE) :-
  expand(STATE,TURN,DEPTH,minusinf,plusinf,MOVE,_).

expand(STATE,_,0,_,_,_,VALUE) :-
  !,
  evaluate(STATE,VALUE).

expand(STATE,TURN,DEPTH,ALPHA,BETA,BEST_MOVE,BEST_VALUE) :-
  generate_moves(STATE,TURN,AVAIBLE_MOVES),
  expand_moves(STATE,TURN,DEPTH,AVAIBLE_MOVES,ALPHA,BETA,BEST_MOVE,BEST_VALUE).

%EXPAND_MOVES - Apply and expand moves
%expand_moves(+STATE,+TURN,+DEPTH,+[MOVES],+ALPHA,+BETA,-BEST_MOVE,-BEST_VALUE)
expand_moves(STATE,_,_,[],_,_,_,VALUE) :-
  !,
  evaluate(STATE,VALUE).

expand_moves(STATE,TURN,DEPTH,[MOVE|MOVES],ALPHA,BETA,BEST_MOVE,BEST_VALUE) :-
  apply(STATE,TURN,MOVE,NEW_STATE),
  NEW_DEPTH is DEPTH - 1,
  change_turn(TURN,NEW_TURN),
  expand(NEW_STATE,NEW_TURN,NEW_DEPTH,ALPHA,BETA,_,NEW_VALUE),
  check(STATE,TURN,DEPTH,MOVES,MOVE,ALPHA,BETA,NEW_VALUE,MOVE,NEW_VALUE,BEST_MOVE,BEST_VALUE).


%CHECK - Check possible cut
%check(+STATE,+TURN,+DEPTH,+[MOVES],+MOVE,+ALPHA,+BETA,+VALUE,+TMP_MOVE,+TMP_VALUE,-BEST_MOVE,-BEST_VALUE)
check(_,max,_,_,MOVE,_,BETA,VALUE,_,_,MOVE,VALUE) :-
  mineq(BETA,VALUE),
  !.

check(_,min,_,_,MOVE,ALPHA,_,VALUE,_,_,MOVE,VALUE) :-
  mineq(VALUE,ALPHA),
  !.

check(STATE,max,DEPTH,MOVES,MOVE,_,BETA,VALUE,_,TMP_VALUE,BEST_MOVE,BEST_VALUE) :-
  min(TMP_VALUE,VALUE),
  !,
  expand_moves_tmp(STATE,max,DEPTH,MOVES,VALUE,BETA,MOVE,VALUE,BEST_MOVE,BEST_VALUE).

check(STATE,min,DEPTH,MOVES,MOVE,ALPHA,_,VALUE,_,TMP_VALUE,BEST_MOVE,BEST_VALUE) :-
  min(VALUE,TMP_VALUE),
  !,
  expand_moves_tmp(STATE,min,DEPTH,MOVES,ALPHA,VALUE,MOVE,VALUE,BEST_MOVE,BEST_VALUE).

check(STATE,TURN,DEPTH,MOVES,_,ALPHA,BETA,_,TMP_MOVE,TMP_VALUE,BEST_MOVE,BEST_VALUE) :-
  expand_moves_tmp(STATE,TURN,DEPTH,MOVES,ALPHA,BETA,TMP_MOVE,TMP_VALUE,BEST_MOVE,BEST_VALUE).

%EXPAND_MOVES_TMP - Continue expand with a first VALUE
%expand_moves_tmp(+STATE,+TURN,+DEPTH,+[MOVES],+ALPHA,+BETA,+TMP_MOVE,+TMP_VALUE,-BEST_MOVE,-BEST_VALUE)
expand_moves_tmp(_,_,_,[],_,_,TMP_MOVE,TMP_VALUE,TMP_MOVE,TMP_VALUE) :-
  !.

expand_moves_tmp(STATE,TURN,DEPTH,[MOVE|MOVES],ALPHA,BETA,TMP_MOVE,TMP_VALUE,BEST_MOVE,BEST_VALUE) :-
  apply(STATE,TURN,MOVE,NEW_STATE),
  NEW_DEPTH is DEPTH - 1,
  change_turn(TURN,NEW_TURN),
  expand(NEW_STATE,NEW_TURN,NEW_DEPTH,ALPHA,BETA,_,NEW_VALUE),
  check(STATE,TURN,DEPTH,MOVES,MOVE,ALPHA,BETA,NEW_VALUE,TMP_MOVE,TMP_VALUE,BEST_MOVE,BEST_VALUE).

%GENERATE_MOVES - Generate all avaible moves
%generate_moves(+STATE,+TURN,-AVAIBLE_MOVES)
generate_moves(STATE,_,[]) :- 
  goal(STATE,min),
  !.

generate_moves(STATE,_,[]) :- 
  goal(STATE,max),
  !.

generate_moves(STATE,TURN,AVAIBLE_MOVES) :-
  findall(MOVE,avaible(MOVE,TURN,STATE),AVAIBLE_MOVES).

%MINEQ 
mineq(X,X) :- 
  !.

mineq(X,Y) :- 
  min(X,Y).

%MIN 
min(_,minusinf) :- 
  !, 
  fail.

min(plusinf,_) :- 
  !, 
  fail.

min(_,plusinf) :- 
  !.

min(minusinf,_) :- 
  !.

min(X,Y) :- 
  X < Y.

%CHANGE_TURN 
change_turn(max,min).
change_turn(min,max).

%
%
%GAME
%
%

%Need:
%initial(-STATE)
%goal(+STATE,+TURN)
%avaible(+MOVE,+TURN,+STATE)
%evaluate(+STATE,-VALUE)
%apply(+STATE,+TURN,+MOVE,-NEW_STATE),

%STATE - [N_DISP,N_NO_MILL,MAX_LIST,MIN_LIST,FREE_LIST]
%MOVE - [I_FROM,I_TO,I_DELETE]

%INITIAL 
initial([18,0,[],[],[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]]).

%GOAL 
goal([0,_,_,[_P1,_P2|[]],_],min).
goal([0,_,[_P1,_P2|[]],_,_],max).
goal([_,50,_,_],_).

%AVAIBLE 
avaible(_,_,STATE) :- 
  goal(STATE,_),
  !,
  fail.

%PHASE 1
%Max
avaible([0,I_TO,I_DELETE],max,[N,_,MAX_LIST,MIN_LIST,FREE_LIST]) :-
  N>0,
  member(I_TO,FREE_LIST),
  check_mill(I_TO,MAX_LIST),
  member(I_DELETE,MIN_LIST).

avaible([0,I_TO,0],max,[N,_,MAX_LIST,_,FREE_LIST]) :-
  N>0,
  !,
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,MAX_LIST).

%Min
avaible([0,I_TO,I_DELETE],min,[N,_,MAX_LIST,MIN_LIST,FREE_LIST]) :-
  N>0,
  member(I_TO,FREE_LIST),
  check_mill(I_TO,MIN_LIST),
  member(I_DELETE,MAX_LIST).

avaible([0,I_TO,0],min,[N,_,_,MIN_LIST,FREE_LIST]) :-
  N>0,
  !,
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,MIN_LIST).

%PHASE 3
%Max
avaible([P1,I_TO,I_DELETE],max,[0,_,[P1,P2,P3|[]],MIN_LIST,FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P2,P3]),
  member(I_DELETE,MIN_LIST).

avaible([P1,I_TO,0],max,[0,_,[P1,P2,P3|[]],_,FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P2,P3]).

avaible([P2,I_TO,I_DELETE],max,[0,_,[P1,P2,P3|[]],MIN_LIST,FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P1,P3]),
  member(I_DELETE,MIN_LIST).

avaible([P2,I_TO,0],max,[0,_,[P1,P2,P3|[]],_,FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P1,P3]).

avaible([P3,I_TO,I_DELETE],max,[0,_,[P1,P2,P3|[]],MIN_LIST,FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P1,P2]),
  member(I_DELETE,MIN_LIST).

avaible([P3,I_TO,0],max,[0,_,[P1,P2,P3|[]],_,FREE_LIST]) :-
  !,
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P1,P2]).

%Min
avaible([P1,I_TO,I_DELETE],min,[0,_,MAX_LIST,[P1,P2,P3|[]],FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P2,P3]),
  member(I_DELETE,MAX_LIST).

avaible([P1,I_TO,0],min,[0,_,_,[P1,P2,P3|[]],FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P2,P3]).

avaible([P2,I_TO,I_DELETE],min,[0,_,MAX_LIST,[P1,P2,P3|[]],FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P1,P3]),
  member(I_DELETE,MAX_LIST).

avaible([P2,I_TO,0],min,[0,_,_,[P1,P2,P3|[]],FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P1,P3]).

avaible([P3,I_TO,I_DELETE],min,[0,_,MAX_LIST,[P1,P2,P3|[]],FREE_LIST]) :-
  member(I_TO,FREE_LIST),
  check_mill(I_TO,[P1,P2]),
  member(I_DELETE,MAX_LIST).

avaible([P3,I_TO,0],min,[0,_,_,[P1,P2,P3|[]],FREE_LIST]) :-
  !,
  member(I_TO,FREE_LIST),
  check_not_mill(I_TO,[P1,P2]).

%PHASE 2
%Max
avaible([I_FROM,I_TO,I_DELETE],max,[0,_,MAX_LIST,MIN_LIST,FREE_LIST]) :-
  member(I_FROM,MAX_LIST),
  find_near(I_FROM,FREE_LIST,I_TO),
  check_mill(I_FROM,I_TO,MAX_LIST),
  member(I_DELETE,MIN_LIST).

avaible([I_FROM,I_TO,0],max,[0,_,MAX_LIST,_,FREE_LIST]) :-
  !,
  member(I_FROM,MAX_LIST),
  find_near(I_FROM,FREE_LIST,I_TO),
  check_not_mill(I_FROM,I_TO,MAX_LIST).

%Min
avaible([I_FROM,I_TO,I_DELETE],min,[0,_,MAX_LIST,MIN_LIST,FREE_LIST]) :-
  member(I_FROM,MIN_LIST),
  find_near(I_FROM,FREE_LIST,I_TO),
  check_mill(I_FROM,I_TO,MIN_LIST),
  member(I_DELETE,MAX_LIST).

avaible([I_FROM,I_TO,0],min,[0,_,_,MIN_LIST,FREE_LIST]) :-
  !,
  member(I_FROM,MIN_LIST),
  find_near(I_FROM,FREE_LIST,I_TO),
  check_not_mill(I_FROM,I_TO,MIN_LIST).

%FIND_NEAR - find adjacent index
%find_near(I,LIST,I_ADJ)
find_near(I,LIST,I2) :-
  mill([I,I2,_]),
  member(I2,LIST).

find_near(I,LIST,I1) :-
  mill([I1,I,_]),
  member(I1,LIST).

find_near(I,LIST,I3) :-
  mill([_,I,I3]),
  member(I3,LIST).

find_near(I,LIST,I2) :-
  mill([_,I2,I]),
  member(I2,LIST).

%CHECK_MILL - check mill closed
%check_mill(I_FROM,I_TO,LIST)
%check_mill(I,LIST)
check_mill(I_FROM,I_TO,LIST) :-
  delete_one(I_FROM,LIST,NEW_LIST),
  !,
  check_mill(I_TO,NEW_LIST).

check_mill(I,LIST) :-
  mill([I,I2,I3]),
  member(I2,LIST),
  member(I3,LIST),
  !.

check_mill(I,LIST) :-
  mill([I1,I,I3]),
  member(I1,LIST),
  member(I3,LIST),
  !.

check_mill(I,LIST) :-
  mill([I1,I2,I]),
  member(I1,LIST),
  member(I2,LIST).

%CHECK_NOT_MILL - check mill not closed
%check_not_mill(I_FROM,I_TO,LIST)
%check_not_mill(I,LIST)
check_not_mill(I_FROM,I_TO,LIST) :-
  check_mill(I_FROM,I_TO,LIST),
  !,
  fail.

check_not_mill(_,_,_).

check_not_mill(I,LIST) :-
  check_mill(I,LIST),
  !,
  fail.

check_not_mill(_,_).

%MILL
mill([1,2,3]).
mill([4,5,6]).
mill([7,8,9]).
mill([10,11,12]).
mill([13,14,15]).
mill([16,17,18]).
mill([19,20,21]).
mill([22,23,24]).
mill([1,10,22]).
mill([4,11,19]).
mill([7,12,16]).
mill([2,5,8]).
mill([17,20,23]).
mill([9,13,18]).
mill([6,14,21]).
mill([3,15,24]).

%EVALUATE 
evaluate(STATE,plusinf) :- 
  goal(STATE,min), 
  !.

evaluate(STATE,minusinf) :- 
  goal(STATE,max), 
  !.

evaluate(STATE,0) :- 
  goal(STATE,_), 
  !.

evaluate(STATE,VALUE) :- 
  heuristic(STATE,VALUE).

%HEURISTIC 
heuristic(STATE,VALUE) :-
  n_piece(STATE,N1),
  n_movement(STATE,N2),
  n_mills(STATE,N3),
  VALUE is N1+N2+N3.

%N_PIECE
n_piece([_,_,MAX_LIST,MIN_LIST,_],VALUE) :-
  list_length(MAX_LIST,N1),
  list_length(MIN_LIST,N2),
  VALUE is (N1-N2)*1.

%N_MOVEMENT
n_movement([_,_,MAX_LIST,MIN_LIST,FREE_LIST],VALUE) :-
  findall(I_FREE1,(member(I1,MAX_LIST),find_near(I1,FREE_LIST,I_FREE1)),FREE_LIST1),
  findall(I_FREE2,(member(I2,MIN_LIST),find_near(I2,FREE_LIST,I_FREE2)),FREE_LIST2),
  list_length(FREE_LIST1,N1),
  list_length(FREE_LIST2,N2),
  VALUE is (N1-N2)*3.

%N_MILLS
n_mills([_,_,MAX_LIST,MIN_LIST,_],VALUE) :-
  findall(I_MILL1,(member(I_MILL1,[1,5,7,11,14,18,20,24]),member(I_MILL1,MAX_LIST),check_mill(I_MILL1,MAX_LIST)),MILL_LIST1),
  findall(I_MILL2,(member(I_MILL2,[1,5,7,11,14,18,20,24]),member(I_MILL2,MIN_LIST),check_mill(I_MILL2,MIN_LIST)),MILL_LIST2),
  list_length(MILL_LIST1,N1),
  list_length(MILL_LIST2,N2),
  VALUE is (N1-N2)*5.

%APPLY 
%PHASE 1 NO MILL
%Max
apply([N,N_NO_MILL,MAX_LIST,MIN_LIST,FREE_LIST],max,[0,I_TO,0],[NEW_N,NEW_N_NO_MILL,NEW_MAX_LIST,MIN_LIST,NEW_FREE_LIST]) :-
  N>0,
  !,
  NEW_N is N-1,
  NEW_N_NO_MILL is N_NO_MILL+1,
  delete_one(I_TO,FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,MAX_LIST,NEW_MAX_LIST).

%Min
apply([N,N_NO_MILL,MAX_LIST,MIN_LIST,FREE_LIST],min,[0,I_TO,0],[NEW_N,NEW_N_NO_MILL,MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  N>0,
  !,
  NEW_N is N-1,
  NEW_N_NO_MILL is N_NO_MILL+1,
  delete_one(I_TO,FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,MIN_LIST,NEW_MIN_LIST).

%PHASE 1 MILL
%Max
apply([N,_,MAX_LIST,MIN_LIST,FREE_LIST],max,[0,I_TO,I_DELETE],[NEW_N,0,NEW_MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  N>0,
  !,
  NEW_N is N-1,
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  delete_one(I_DELETE,MIN_LIST,NEW_MIN_LIST),
  append_element(I_TO,MAX_LIST,NEW_MAX_LIST),
  append_element(I_DELETE,TMP_FREE_LIST,NEW_FREE_LIST).

%Min
apply([N,_,MAX_LIST,MIN_LIST,FREE_LIST],min,[0,I_TO,I_DELETE],[NEW_N,0,NEW_MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  N>0,
  !,
  NEW_N is N-1,
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  delete_one(I_DELETE,MAX_LIST,NEW_MAX_LIST),
  append_element(I_TO,MIN_LIST,NEW_MIN_LIST),
  append_element(I_DELETE,TMP_FREE_LIST,NEW_FREE_LIST).

%PHASE 2/3 NO MILL
%Max
apply([0,N_NO_MILL,MAX_LIST,MIN_LIST,FREE_LIST],max,[I_FROM,I_TO,0],[0,NEW_N_NO_MILL,NEW_MAX_LIST,MIN_LIST,NEW_FREE_LIST]) :-
  !,
  NEW_N_NO_MILL is N_NO_MILL+1,
  delete_one(I_FROM,MAX_LIST,TMP_MAX_LIST),
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  append_element(I_FROM,TMP_FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,TMP_MAX_LIST,NEW_MAX_LIST).

%Min
apply([0,N_NO_MILL,MAX_LIST,MIN_LIST,FREE_LIST],min,[I_FROM,I_TO,0],[0,NEW_N_NO_MILL,MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  !,
  NEW_N_NO_MILL is N_NO_MILL+1,
  delete_one(I_FROM,MIN_LIST,TMP_MIN_LIST),
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  append_element(I_FROM,TMP_FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,TMP_MIN_LIST,NEW_MIN_LIST).

%PHASE 2/3 MILL
%Max
apply([0,_,MAX_LIST,MIN_LIST,FREE_LIST],max,[I_FROM,I_TO,I_DELETE],[0,0,NEW_MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  !,
  delete_one(I_FROM,MAX_LIST,TMP_MAX_LIST),
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  delete_one(I_DELETE,MIN_LIST,NEW_MIN_LIST),
  append_element([I_FROM,I_DELETE],TMP_FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,TMP_MAX_LIST,NEW_MAX_LIST).

%Min
apply([0,_,MAX_LIST,MIN_LIST,FREE_LIST],min,[I_FROM,I_TO,I_DELETE],[0,0,NEW_MAX_LIST,NEW_MIN_LIST,NEW_FREE_LIST]) :-
  !,
  delete_one(I_FROM,MIN_LIST,TMP_MIN_LIST),
  delete_one(I_TO,FREE_LIST,TMP_FREE_LIST),
  delete_one(I_DELETE,MAX_LIST,NEW_MAX_LIST),
  append_element([I_FROM,I_DELETE],TMP_FREE_LIST,NEW_FREE_LIST),
  append_element(I_TO,TMP_MIN_LIST,NEW_MIN_LIST).

%
%
%UTILITY
%
%

%MEMBER
member(T,[T|_]).

member(T,[_|L]) :- 
  member(T,L).

%DELETE_ONE
delete_one(_,[],[]).

delete_one(E,[E|T],T).

delete_one(E,[H|T],[H|T1]) :- 
  delete_one(E,T,T1).

%APPEND_ELEMENT
append_element(E,[],[E]).

append_element(E,[H|T],[H|T1]) :- 
  append_element(E,T,T1).

%LIST_LENGTH
list_length(L,N) :- 
  list_length(L,0,N).

list_length([],ACC,ACC).

list_length([_|L],ACC,N) :- 
  NEW_ACC is ACC+1,
  list_length(L,NEW_ACC,N).