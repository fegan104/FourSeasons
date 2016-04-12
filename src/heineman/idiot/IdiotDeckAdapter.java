package heineman.idiot;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.view.DeckView;
import heineman.Idiot;

/**
 * Controller for all interaction with the DeckView widget.
 * <p>
 * Creation date: (1/2/02 10:18:49 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class IdiotDeckAdapter extends SolitaireReleasedAdapter {

	/** My object. */
	protected DeckView src;

	/**
	 * IdiotDeckAdapter constructor.
	 */
	public IdiotDeckAdapter (Idiot theGame, DeckView deckView) {
		super(theGame);

		src = deckView;
	}

	public void mousePressed (java.awt.event.MouseEvent me) {
		// Respond to the mousePressed events only on the deck
		Deck deck = (Deck) src.getModelElement();

		// Extract from game object (a bit of a HACK)
		Column col1 = (Column) theGame.getModelElement ("col1");
		Column col2 = (Column) theGame.getModelElement ("col2");
		Column col3 = (Column) theGame.getModelElement ("col3");
		Column col4 = (Column) theGame.getModelElement ("col4");		

		Move m = new DealFourMove (deck, col1, col2, col3, col4);
		if (m.doMove (theGame)) {

			// Successful DealFour Move
			theGame.pushMove (m);

			// refresh all widgets
			theGame.refreshWidgets();
		}
	}
	
}
