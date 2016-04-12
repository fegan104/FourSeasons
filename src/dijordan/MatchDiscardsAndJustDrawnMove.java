package dijordan;

import ks.common.games.Solitaire;
import ks.common.model.*;


/******************************************
 * Represents a Move for undo use
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class MatchDiscardsAndJustDrawnMove extends Move {
	/** justDrawn. */
	protected Pile justDrawn;

	/** discards. */
	protected Column discards;

	/** Two cards removed. */
	protected Card drawnCard;
	/** Was discards empty when the remove occured? */
	protected boolean wasDiscardsEmpty;
	protected Card discardsCard;
  /*************
   * Constructor
   */
public MatchDiscardsAndJustDrawnMove(Column c, Pile p) {
	this.discards = c;
	this.justDrawn = p;

	this.wasDiscardsEmpty = discards.empty();
}      
/**
 * Do the MatchDiscardsAndJustDrawnMove.
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE:
	if (valid (theGame) == false) {
		return false;
	}

	// EXECUTE:
	discardsCard = discards.get();
	drawnCard = justDrawn.get();

	// these cards are no longer selected.
	discardsCard.setSelected(false);
	drawnCard.setSelected(false);

	wasDiscardsEmpty = discards.empty();
	if (!wasDiscardsEmpty) {
		justDrawn.add (discards.get());
	}
	
	theGame.updateScore(+2);
	return true;
} 
  /**********************************************
   * to undo this move we must put the cards back IN THE ORDER they were extracted.
   */
  public boolean undo(Solitaire theGame) {
	// VALIDATE:
	if ((discardsCard == null) || (drawnCard == null))
		return false;
	
	// EXECUTE:
	if (!wasDiscardsEmpty) {
		discards.add (justDrawn.get());
	}
	discards.add (discardsCard);
	justDrawn.add (drawnCard);

	theGame.updateScore(-2);
	return true;
  }            
/**
 * Validate MatchDiscardsAndJustDrawnMove
 */
public boolean valid (Solitaire theGame) {
	boolean validation = false;

	// VALIDATE:
	Card c1 = null, c2 = null;
	if (!discards.empty()) c1 = discards.peek();
	if (!justDrawn.empty()) c2 = justDrawn.peek();
	
	if ((c1 != null) && (c2 != null)) {
	  if ((c1.getRank() + c2.getRank()) == 13) {
		  validation = true;
	  }
	}
	
	return validation;
}
}
