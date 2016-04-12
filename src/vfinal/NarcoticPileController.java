package vfinal;

import java.awt.event.MouseEvent;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

/**
 * Final Pile controller.
 * 
 * @author George T. Heineman (heineman@cs.wpi.edu)
 */
public class NarcoticPileController extends java.awt.event.MouseAdapter {
	/** The narcotic instance. */
	protected Solitaire theGame = null;

	/** The PileView widget being controlled. */
	PileView pileview;

	/** NarcoticDeckController constructor comment. */
	public NarcoticPileController(Solitaire game, PileView pileview) {
		super();

		theGame = game;
		this.pileview = pileview;
	}

	/**
	 * Respond to mouse click events.
	 */
	public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() > 1) {
            Pile p1 = (Pile) theGame.getModelElement ("pile1");
            Pile p2 = (Pile) theGame.getModelElement ("pile2");
            Pile p3 = (Pile) theGame.getModelElement ("pile3");
            Pile p4 = (Pile) theGame.getModelElement ("pile4");

            // check to see if we can remove all cards.
            Move m = new RemoveAllMove (p1, p2, p3, p4);
            if (m.doMove(theGame)) {
                // SUCCESS
                theGame.pushMove (m);

                // redraw all piles
                theGame.refreshWidgets();
            }
        }
	}
	
	/**
	 * On mouse released, get card being dragged and properly form the
	 * desired move
	 */
	public void mouseReleased(java.awt.event.MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();
		if (w == Container.getNothingBeingDragged()) return;

		/** Must be the CardView widget. */
		CardView cardView = (CardView) w;
		Card theCard = (Card) cardView.getModelElement();

		/** Recover the From Pile */
		PileView fromPileView = (PileView) c.getDragSource();
		Pile fromPile = (Pile) fromPileView.getModelElement();

		// Determine the To Pile
		Pile toPile = (Pile) pileview.getModelElement();

		// Try to make the move
		Move m = new MoveCardMove (fromPile, theCard, toPile);
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove (m);
		} else {
			// invalid move! Return to the pile from whence it came.
			// Rely on the ability of each Widget to support this method.
			fromPileView.returnWidget (cardView);
		}

		// Since we could be released over a widget, or over the container, 
		// we must repaintAll() clipped to the region we are concerned about.
		// first refresh the last known position of the card being dragged.
		// then the widgets.
		theGame.refreshWidgets(); 

		// release the dragging object since the move is now complete (this 
		// will reset container's dragSource).
		c.releaseDraggingObject();
		c.repaint();
	}

	/**
	 * On mouse Press for now, we simply delete top card and move to be
	 * the dragging card of the container.
	 * 
	 * @param me     low-level mouse event
	 */
	public void mousePressed(java.awt.event.MouseEvent me) {
		// Ask PileView to retrieve the top card as a CardView Widget
		CardView cardView = pileview.getCardViewForTopCard(me);

		// no card present!
		if (cardView == null) { return; }
		
		// Have container track this object now. Record where it came from
		Container c = theGame.getContainer();
		c.setActiveDraggingObject (cardView, me);
		c.setDragSource(pileview);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		pileview.redraw();
	}
}
