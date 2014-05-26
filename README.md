# This is my README
TO DO:
1) cachare i valori dell'euristica nell'albero.
2) implementare il grafo, con nodi o stati da vedere.
3) Espansione incrementale?!
4) Ordinare la generazione dei figli.
5) Migliorare l'euristica ---> !!!!!
6) Provare quanto va veloce con o senza albero e quanti livelli riesce a fare.
7) Ripassare la Brini.

-------------------------------------------------------------------------------------
http://www.smartlittlegames.com/ninemensmorris --> questo ci batte da 5 secondi in su

--------------------------------------------------------------------------------------
Errori improbabili:

Exception in thread "main" java.lang.NullPointerException
	at java.util.ArrayList.addAll(Unknown Source)
	at prova.MillsBoard.generatePhaseTwo(MillsBoard.java:193)
	at prova.MillsBoard.phaseTwo(MillsBoard.java:201)
	at prova.MillsBoard.getAvailableMoves(MillsBoard.java:103)
	at prova.MillsBoard.isTerminal(MillsBoard.java:325)
	at prova.MillsState.isTerminal(MillsState.java:64)
	at main.Main.main(Main.java:84)
