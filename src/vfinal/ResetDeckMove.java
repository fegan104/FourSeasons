package vfinal;

import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Pile;


/**
 * Represents the reset of the deck.
 * <p>
 * Note that this move cannot be undone.
 * <p> * 
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class ResetDeckMove extends ks.common.model.Move {
	/** The Deck. */
	protected Deck deck;

	/** The Piles. */
	protected Pile pile1;
	protected Pile pile2;
	protected Pile pile3;
	protected Pile pile4;
	/**
	 * ResetDeckMove constructor comment.
	 */
	public ResetDeckMove(Deck d, Pile p1, Pile p2, Pile p3, Pile p4) {
		super();

		this.deck = d;
		this.pile1 = p1;
		this.pile2 = p2;
		this.pile3 = p3;
		this.pile4 = p4;
	}
	/**
	 * Each move should knows how to execute itself.
	 * <p>
	 * Creation date: (10/21/01 3:33:39 PM)
	 * @param theGame edu.wpi.cs.solitaire.game.Solitaire the game being played.
	 * @return boolean
	 * @since V1.6.2
	 */
	public boolean doMove (Solitaire theGame) {

		// VALIDATE:
		if (!valid(theGame))
			return false;

		// EXECUTE:
		int numAdded = 0;
		while (!pile4.empty()) {
			deck.add(pile4.get());
			numAdded++;
		}
		while (!pile3.empty()) {
			deck.add(pile3.get());
			numAdded++;
		}
		while (!pile2.empty()) {
			deck.add(pile2.get());
			numAdded++;
		}
		while (!pile1.empty()) {
			deck.add(pile1.get());
			numAdded++;
		}

		// finally update the total number.
		theGame.updateNumberCardsLeft(numAdded);
		return true;
	}
	/**
	 * This move cannot be undone. To implement the undo, one would only have to keep
	 * track of the number of cards removed from each pile, and then restore them to
	 * their proper place.
	 * <p>
	 * For the sake of skillful playing, however, we have decided to make the act of
	 * reforming the deck a "point of no return".
	 */
	public boolean undo(ks.common.games.Solitaire game) {
		// announce no ability to undo.
		return false;
	}
	/**
	 * Validate ResetDeck Move.
	 * @param game edu.wpi.cs.soltaire.games.Solitaire
	 */
	public boolean valid (ks.common.games.Solitaire game) {
		// VALIDATION:
		boolean validation = false;

		// dealFour(d) : d.empty()
		if (deck.empty())
			validation = true;

		return validation;
	}
}