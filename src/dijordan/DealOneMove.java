package dijordan;

import ks.common.games.Solitaire;
import ks.common.model.*;

/******************************************
 * Represents a Move for undo use
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class DealOneMove extends Move {
	/** move information. */
	protected Deck deck;
	protected Pile justDrawn;
	protected Column discards;
	/** Needed during undo to remember if justDrawn pile was empty or not. */
	protected boolean wasOnePileEmpty;
  /*************
   * Constructor
   */
  public DealOneMove(Deck d, Pile p, Column c) {

	this.deck = d;
	this.justDrawn = p;
	this.discards = c;
	this.wasOnePileEmpty = justDrawn.empty();
  }        
/**
 * DealOneMove executed.
 * @param theGame   ks.common.games.Solitaire
 */
public boolean doMove(Solitaire theGame) {
	if (valid (theGame) == false) {
		return false;
	}

	// EXECUTE:
	// Action: Deal one card from the deck to the justDrawn pile
	// if there was a card in justDrawn, move it to discards. Be sure to deselect
	// the justdrawn card if it had previously been selected.
	if (!justDrawn.empty()) {
		Card c = justDrawn.get();
		c.setSelected (false);
		discards.add (c);
	}
	
	justDrawn.add (deck.get());
	theGame.updateNumberCardsLeft(-1);
	return true;
}          
/**
 * To undo this move, we must move the card from justdrawn to deck
 * and if pileWasEmpty is false, we must move a discarsd to justdrawn.
 * @param theGame ks.common.games.Solitaire 
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (justDrawn.empty()) return false;
	if ((!wasOnePileEmpty) && (discards.empty())) return false;
		
	// UNDO
	deck.add (justDrawn.get());
	
	if (!wasOnePileEmpty) {
	  justDrawn.add (discards.get());
	}

	theGame.updateNumberCardsLeft(+1);
	return true;
  }                
/**
 * Validation for DealOneMove.
 * @param theGame ks.common.games.Solitaire
 */ 
public boolean valid (Solitaire theGame) {
	boolean validation = false;

	// VALIDATE:
	if (! deck.empty())
		validation = true;

	return validation;
}
}
