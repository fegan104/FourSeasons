package vfinal;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;


/**
 * Represents the removal of four cards, one from each pile.
 * <p>
 * This move is an interesting one to write. Specifically, when the
 * user requests the move, the move object is created to store the cards
 * that were removed from the board. In this way, the undo() will restore
 * the cards because they were kept insider the Move object.
 * <p>
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class RemoveAllMove extends Move {
	/** All piles. */
	protected Pile[] piles;

	/** Removed cards. */
	protected Card[] removedCards;
	/**
	 * Class Representing the action of removing four cards, one from each pile. Use this
	 * constructor when the move has not yet been made (and the cards are therefore in the
	 * Pile objects).
	 * <p>
	 * @param p1 Pile  first pile
	 * @param p2 Pile  second pile
	 * @param p3 Pile  third pile
	 * @param p4 Pile  fourth pile
	 */
	public RemoveAllMove (Pile p1, Pile p2, Pile p3, Pile p4) {
		super ();

		/** Store all parameters with the Move Object. */
		piles = new Pile[] {p1, p2, p3, p4};

		// These can only be filled once the move has occurred.
		this.removedCards = null;
	}
	
	/**
	 * Each move should knows how to execute itself.
	 * <p>
	 * @return boolean
	 * @since V1.6.2
	 */
	public boolean doMove (Solitaire theGame) {
		// VALIDATE:
		if (valid (theGame) == false) {
			return false;
		}

		// store the cards being removed inside the "card-n" parameters for this move object
		// so we can undo if needed later.
		removedCards = new Card[piles.length];
		for (int i = 0; i < piles.length; i++) {
			removedCards[i] = piles[i].get();
		}
		// don't forget to update score!
		theGame.updateScore (4);
		return true;
	}
	/**
	 * undo method comment.
	 * <p>
	 * Version 1.1 fixes undo which didn't update score
	 */
	public boolean undo(Solitaire theGame) {

		// VALIDATE:
		if (removedCards ==null) { return false; }

		// UNDO:
		for (int i = 0; i < piles.length; i++) {
			piles[i].add(removedCards[i]);
		}

		removedCards = null;

		// don't forget to update score!
		theGame.updateScore (-4);
		return true;
	}
	/**
	 * Validate action for Narcotic: RemoveAll cards
	 *
	 * @return boolean
	 */
	public boolean valid (Solitaire game) {
		if (piles[0].empty()) {
			return false;
		}
		
		int rank = piles[0].rank();
		for (int i = 1; i < piles.length; i++) {
			if (piles[i].empty() || piles[i].rank() != rank) { return false; }
		}
		
		return true;
	}
}