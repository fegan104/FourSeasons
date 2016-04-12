package heineman.klondike;

import ks.common.model.Deck;
import ks.common.model.Pile;

/**
 * Represents the moving of a card from the deck to the waste pile.
 * <p>
 * There are two parameters for this move:
 * <p>
 * deck = Deck which deals the cards<br>
 * wastePile = Pile to which the card is dealt.<br>
 * <p>
 * Creation date: (10/28/01 8:50:54 AM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class DealCardMove extends ks.common.model.Move {
	/** The deck. */
	protected Deck deck;

	/** The wastePile. */
	protected Pile wastePile;
/**
 * DealCardMove constructor.
 * @param Deck deck
 * @param Pile wastePile
 */
public DealCardMove (Deck deck, Pile wastePile) {
	super();
	
	this.deck = deck;
	this.wastePile = wastePile;
}
/**
 * Do Move
 * @param theGame ks.games.Solitaire 
 */
public boolean doMove (ks.common.games.Solitaire game) {
	// VALIDATE
	if (valid(game) == false)
		return false;
		
	// EXECUTE:
	// Get card from deck
	wastePile.add (deck.get());
	
	game.updateNumberCardsLeft (-1);
	return true;	
}
/**
 * To undo this move, we move the cards from the wastePile back to the Deck.
 * @param theGame ks.games.Solitaire 
 */
public boolean undo(ks.common.games.Solitaire game) {

	// VALIDATE:
	if (wastePile.empty()) return false;
		
	// UNDO:
	deck.add (wastePile.get());

	// update the number of cards to go.
	game.updateNumberCardsLeft (+1);
	return true;
}
/**
 * Action for Klondike: Deal card from deck to wastePile.
 * @param d ks.common.games.Solitaire
 */
public boolean valid(ks.common.games.Solitaire game) {
	// VALIDATION:
	boolean validation = false;

	//    dealCard(deck,wastePile) : not deck.empty()
	if (!deck.empty()) 
		validation = true;

	return validation;
}
}
