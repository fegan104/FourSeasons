package vfinal;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Controller for the DeckView widgets used in Narcotic.
 * 
 * By extending 'SolitaireReleasedAdapter' we take advantage of the 
 * default behavior 
 *
 * @author George T. Heineman (heineman@cs.wpi.edu)
 */
public class NarcoticDeckController extends SolitaireReleasedAdapter {
    /** NarcoticDeckController constructor comment. */
    public NarcoticDeckController(Solitaire game) {
        super(game);
    }
    
    /**
     * A MousePressed event on the DeckView is a signal to deal the next
     * set of cards (if the deck is nonempty).
     *
     * @param me     low-level mouse event
     */
    public void mousePressed(java.awt.event.MouseEvent me) {

        // Find the deck from our model
        Deck d = (Deck) theGame.getModelElement("deck");
        Pile p1 = (Pile) theGame.getModelElement("pile1");
        Pile p2 = (Pile) theGame.getModelElement("pile2");
        Pile p3 = (Pile) theGame.getModelElement("pile3");
        Pile p4 = (Pile) theGame.getModelElement("pile4");

        if (!d.empty()) {
            // Deal four cards
            Move m = new DealFourMove(d, p1, p2, p3, p4);
            if (m.doMove(theGame)) {
                // SUCCESS: have solitaire game store this move
            	theGame.pushMove(m);

                // have solitaire game refresh widgets that were affected 
            	theGame.refreshWidgets();
            }
        } else {
        	 // Must be a request to reset the deck.
            Move m = new ResetDeckMove (d, p1, p2, p3, p4);
            if (m.doMove(theGame)) {
                // SUCCESS
            	theGame.pushMove (m);

                // refresh all widgets that were affected by this move.
            	theGame.refreshWidgets();
            }
        }
    }
}
