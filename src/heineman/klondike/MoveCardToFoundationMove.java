package heineman.klondike;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Pile;


/**
 * Represents the move of a card to the Foundation from a Column
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class MoveCardToFoundationMove extends ks.common.model.Move {
	
	/** The BuildablePile. */
	protected BuildablePile buildablePile;

	/** The card being dragged (if at all). */
	protected Card draggingCard;
	
	/** The destination Foundation Pile. */
	protected Pile foundation;
/**
 * MoveCardToFoundationmove constructor comment.
 */
public MoveCardToFoundationMove(BuildablePile bp, Card card, Pile foundation) {
	super();

	buildablePile = bp;
	this.draggingCard = card;
	this.foundation = foundation;
}
/**
 * Each move should knows how to execute itself.
 * <p>
 * @param theGame ks.game.Solitaire the game being played.
 * @return boolean
 */
public boolean doMove (Solitaire theGame) {
	// VALIDATE (should we also check for validitation by rank? suit?
	if (valid (theGame) == false)
		return false;

	// EXECUTE:
	// Deal with both situations
	if (draggingCard == null)
		foundation.add (buildablePile.get());
	else
		foundation.add (draggingCard);

	// advance score
	theGame.updateScore (1);
	return true;
}
/**
 * undo method.
 */
public boolean undo(Solitaire game) {
	// VALIDATE:
	if (foundation.empty()) return false;

	// EXECUTE UNDO:	
	buildablePile.add (foundation.get());

	// reverse score advance
	game.updateScore (-1);
	return true;
}
/**
 * Action for Klondike: BuildablePile card draggged to Foundation Pile
 * @param game ks.common.games.Solitaire
 */
public boolean valid(ks.common.games.Solitaire game) {
	// VALIDATION:
	boolean validation = false;

	// If draggingCard is null, then no action has yet taken place.
	if (draggingCard == null) {
		// moveWasteToFoundation(buildablePile,pile) : not foundation.empty() and not buildablePile.empty() and 
		if (!foundation.empty() && !buildablePile.empty() && (buildablePile.rank() == foundation.rank() + 1) && (buildablePile.suit() == foundation.suit()))
			validation = true;

		// moveWasteToFoundation(buildablePile,pile) : foundation.empty() and not buildablePile.empty() and waste.rank() == ACE
		if (foundation.empty() && !buildablePile.empty() && buildablePile.rank() == Card.ACE)
			validation = true;  
	} else {
		// the action must have taken place, so act on the card.

		// moveWasteToFoundation(waste,pile) : not foundation.empty() and not waste.empty() and 
		if (!foundation.empty() && (draggingCard.getRank() == foundation.rank() + 1) && (draggingCard.getSuit() == foundation.suit()))
			validation = true;

			// moveWasteToFoundation(waste,pile) : foundation.empty() and card.rank() == ACE
		if (foundation.empty() && (draggingCard.getRank() == Card.ACE))
			validation = true;
	}

	return validation;
}
}
