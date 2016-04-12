package vfinal;

import ks.common.games.Solitaire;
import ks.common.model.*;

/**
 * Represents the moving of a card from one pile to another.  
 * 
 * @author George Heineman
 */
public class MoveCardMove extends ks.common.model.Move {
	/** From & To piles. */
	protected Pile from;
	protected Pile to;

	/** The card being moved (if any). */
	protected Card movingCard;

	/**
	 * MoveCardMove constructor.
	 * <p>
	 * @param from         The source Pile from which the move originates
	 * @param movingCard   If the move is in progress, this is the card
	 *                     being dragged from Pile <code>from</code>.
	 * @param to           The target Pile for the move.
	 */
	public MoveCardMove (Pile from, Card movingCard, Pile to) {
		super();

		this.from = from;
		this.movingCard = movingCard;
		this.to = to;
	}

	/**
	 * Each move should knows how to execute itself.
	 * <p>
	 * @param ks.common.game.Solitaire   the game being played.
	 * @return boolean
	 */
	public boolean doMove (Solitaire theGame) {
		// VALIDATE:
		if (valid (theGame) == false) {
			return false;
		}

		// EXECUTE:
		to.add (movingCard);

		return true;
	}

	/**
	 * To undo this move, we move the cards from the toPile back to the fromPile
	 * @param theGame ks.common.games.Solitaire
	 */
	public boolean undo(Solitaire game) {
		// VALIDATE:
		if (to.empty()) return false;

		// UNDO: move back
		from.add (to.get());
		return true;
	}

	/**
	 * Validate MoveCardMove.
	 * <p>
	 * @param game 
	 */
	public boolean valid (Solitaire game) {
		// VALIDATION:
		boolean validation = false;

		// Make sure not empty!
		if (to.count() == 0) { return false; }
		
		// Make sure same rank and to left of...
		if (to.rank() == movingCard.getRank()) {
			if (toLeftOf (from, to)) {
				validation = true;
			}
		}
		return validation;
	}


	/**
	 * Determines whether pile1 is to the left of pile2
	 */
	protected boolean toLeftOf(Pile pile1, Pile pile2) {
		String pile1Name = pile1.getName();
		String pile2Name = pile2.getName();

		int rc = pile1Name.compareTo (pile2Name);

		// We must be to the left
		if (rc > 0) return true;

		// all other cases return false.
		return false;
	}
}