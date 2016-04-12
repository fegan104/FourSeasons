package heineman.klondike;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

import heineman.Klondike;

/**
 * Controls all mouse actions with the deck.
 * Creation date: (11/11/01 10:57:40 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class KlondikeDeckController extends SolitaireReleasedAdapter {
	/** The game. */
	protected Klondike theGame;

	/** The WastePile of interest. */
	protected Pile wastePile;

	/** The Deck of interest. */
	protected Deck deck;

	/**
	 * KlondikeDeckController constructor comment.
	 */
	public KlondikeDeckController(Klondike theGame, Deck d, Pile wastePile) {
		super(theGame);

		this.theGame = theGame;
		this.wastePile = wastePile;
		this.deck = d;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event. In this case,
	 * no drag is ever achieved, and we simply deal upon the press.
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {

		// Attempting a DealFourCardMove
		Move m = new DealCardMove (deck, wastePile);
		if (m.doMove(theGame)) {
			theGame.pushMove (m);     // Successful DealFour Move
			theGame.refreshWidgets(); // refresh updated widgets.
		}
	}

}
