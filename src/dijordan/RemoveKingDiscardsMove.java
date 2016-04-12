package dijordan;

import ks.common.games.Solitaire;
import ks.common.model.*;

/******************************************
 * Represents a Move for undo use
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class RemoveKingDiscardsMove extends Move {
	/** Discards. */
	protected Column discards;

	/** Card to be removed. */
	protected Card discardsCard;
/*************
 * Constructor
 */
public RemoveKingDiscardsMove(Column discards) {
	this.discards = discards;
	discardsCard = null;
}
/**
 * doMove not yet implemented
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE
	if (valid (theGame) == false)
		return false;

	// EXECUTE:
	discardsCard = discards.get();
	discardsCard.setSelected (false); // just to be sure.

	theGame.updateScore(+1);
	return true;
}
  
/**
 * to undo this move we must put the card back
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (discardsCard == null) return false;

	// EXECUTE
	discards.add (discardsCard);
	theGame.updateScore(-1);

	return true;
}    
/**
 * Validate this move.
 */
public boolean valid(Solitaire theGame) {
	boolean validation = false;
	// VALIDATE

	// can eliminate kings from discard stack.
	if ((discards.count() > 0) && (discards.rank() == Card.KING)) {
		validation = true;
	}
	
	return validation;
}    
}
