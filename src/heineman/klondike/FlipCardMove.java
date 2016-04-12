package heineman.klondike;

import ks.common.model.BuildablePile;

/**
 * Represents the flip of a BuildablePile card.
 * <p>
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class FlipCardMove extends ks.common.model.Move {
	/** The affected pile. */
	protected BuildablePile pile;
/**
 * Represents the flip of a card on a BuildablePile.
 * <p>
 * Creation date: (11/10/01 11:02:58 PM)
 * @param bp ks.games.klondike.BuildablePile
 */
public FlipCardMove(BuildablePile bp) {
	super ();
	
	pile = bp;
}
/**
 * Flip card in this BuildablePile
 * <p>
 * @param theGame ks.games.Solitaire 
 */
public boolean doMove (ks.common.games.Solitaire game) {
	// VALIDATE
	if (valid (game) == false)
		return false;

	// EXECUTE
	pile.flipCard();
	return true;
}
/**
 * undo Flip by calling flipCard()
 */
public boolean undo(ks.common.games.Solitaire game) {
	// VALIDATE
	if (pile.empty()) return false;

	// UNDO
	pile.flipCard();
	
	return true;
}
/**
 * Action for Klondike: Flip BuildablePile
 * @param d ks.common.games.Solitaire
 */
public boolean valid(ks.common.games.Solitaire game) {
	// VALIDATION:
	boolean validation = false;

	//    flipCard(pile) : not pile.empty() and not pile.faceUp()
	if (!pile.empty() && !pile.faceUp())
		validation = true;

	return validation;
}
}
