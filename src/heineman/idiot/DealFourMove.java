package heineman.idiot;


import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
/** 
 * Action for Idiot Game: Deal four cards from deck to the columns.
 * <p>
 * VALIDATION: not d.empty()<br>
 * ACTION: col1.add (d.get()); col2.add (d.get()); col3.add (d.get()); col3.add (d.get());<br>
 *   updateNumberCardsLeft (-4);<br>
 * UNDO: reverse action<br>
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class DealFourMove extends Move {
	/** Columns. */
	protected Column col1;
	protected Column col2;
	protected Column col3;
	protected Column col4;

	/** Deck. */
	protected Deck deck;
/**
 * DealFourMove constructor .
 * @param moveType int
 */
public DealFourMove (Deck d, Column c1, Column c2, Column c3, Column c4) {
	super ();

	deck = d;
	col1 = c1;
	col2 = c2;
	col3 = c3;
	col4 = c4;			
}
/**
 * doMove method comment.
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE
	if (valid (theGame) == false)
		return false;
	
	// EXECUTE. Changes to models will cause views to be marked as dirty)
	col1.add (deck.get());
	col2.add (deck.get());
	col3.add (deck.get());
	col4.add (deck.get());

	// update count in deck.
	theGame.updateNumberCardsLeft (-4);
	return true;
}
/**
 * undo move.
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (col1.empty() || col2.empty() || col3.empty() || col4.empty()) return false;

	// UNDO: reverse order of operations (changes to models will cause views to be marked as dirty)
	deck.add (col4.get());
	deck.add (col3.get());
	deck.add (col2.get());
	deck.add (col1.get());

	// update count in deck.
	theGame.updateNumberCardsLeft (+4);
	return true;
}
/**
 * validate move.
 */
public boolean valid (Solitaire theGame) {
	// VALIDATE
	return !deck.empty();
}
}
