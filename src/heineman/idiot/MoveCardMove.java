package heineman.idiot;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;

/** 
 * Action for Idiot Game: Move card from one column to another.
 * <p>
 * SIGNATURE: MoveCard (Column from, Card moving, Column to)<br>
 * VALIDATION: to.empty()<br>
 * ACTION: to.add (moving);<br>
 * UNDO: from.add (to.get());<br>
 * <p>
 * Note that the <code>from</code> column has already been modified when the
 * drag is present.
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class MoveCardMove extends Move {
	/** From and To columns. */
	protected Column from;
	protected Column to;

	/** Moving card. */
	protected Card movingCard;
/**
 * MoveCardMove constructor .
 * @param moveType int
 */
public MoveCardMove (Column from, Card moving, Column to) {
	super ();

	this.from = from;
	this.movingCard = moving;
	this.to = to;
}
/**
 * doMove method comment.
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE:
	if (valid (theGame) == false)
		return false;
		
	// EXECUTE
	to.add (movingCard);
	
	return true;
}
/**
 * undo move.
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (to.empty()) return false;
	
	// UNDO
	from.add (to.get());
	return true;
}
/**
 * validate move.
 */
public boolean valid (Solitaire theGame) {
	// VALIDATION:
	boolean validation = false;
	
	if (to.empty() != false) {
		validation = true;
	}
	
	return validation;
}
}
