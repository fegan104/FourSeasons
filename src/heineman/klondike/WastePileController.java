package heineman.klondike;

import java.awt.event.MouseEvent;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;
import heineman.Klondike;

/**
 * Controls all actions regarding the WastePile.
 * <p>
 * In an earlier version of this controller, the CardView object returned by the PileView
 * object was converted into a ColumnView to make it easier to process Release requests
 * throughout the system. Unfortunately, this negated the usefulness of <code>returnWidget</code>
 * because this method for a PileView only accepted <code>CardView</code> objects. This
 * leads the the following controller design principle: Never change the type of the object
 * extracted from a Widget because then NO OTHER CONTROLLER will be able to use returnWidget.
 * <p>
 * Thus, the alternative we must choose is to have all other controllers check the type of
 * the dragging object, and if it is a CardView, then it is understood to be coming from
 * the wastePile; these controllers will be slightly more complicated, but the logic can
 * easily be localized, and <code>returnWidget</code> is not affected.
 * <p>
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class WastePileController extends SolitaireReleasedAdapter {
	
	/** The Klondike Game. */
	protected Klondike theGame;

	/** The specific WastePileView being controlled. */
	protected PileView src;

	/**
	 * WastePileController constructor comment.
	 */
	public WastePileController(Klondike theGame, PileView waste) {
		super(theGame);
	
		this.theGame = theGame;
		this.src = waste;
	}
		
	/**
	 * Try to play the faceup card directly to the foundation.
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseClicked(MouseEvent me) {
	
		if(me.getClickCount() > 1) {
	
			// Point to our underlying model element.
			Pile pile = (Pile) src.getModelElement();
			
			// See if we can move this one card.
			boolean moveMade = false;
			for (int f = 1; f <=4; f++) {
				Pile fp = (Pile) theGame.getModelElement ("foundation" + f);
				Move m = new MoveWasteToFoundationMove (pile, null, fp);
				if (m.doMove(theGame)) {
	
					// Success! Add this move to our history.
					theGame.pushMove (m);
					
					moveMade = true;
					theGame.refreshWidgets();
					break;
				}
			}
			
			if (!moveMade) {
				java.awt.Toolkit.getDefaultToolkit().beep();
				return; // announce our displeasure			
			}
		}
	}
	
	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 * <p>
	 * @param me java.awt.event.MouseEvent
	 */
	public void mousePressed(MouseEvent me) {
	 
		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();
		
		/** Return if there is no card to be chosen. */
		Pile wastePile = (Pile) src.getModelElement();
		if (wastePile.count() == 0) {
			c.releaseDraggingObject();
			return;
		}
	
		// Get a card to move from PileView. Note: this returns a CardView.
		// Note that this method will alter the model for PileView if the condition is met.
		CardView cardView = src.getCardViewForTopCard (me);
		
		// an invalid selection of some sort.
		if (cardView == null) {
			c.releaseDraggingObject();
			return;
		}
		
		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("WastePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}
	
		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (cardView, me);
		
		// Tell container which source widget initiated the drag
		c.setDragSource (src);
	
		// The only widget that could have changed is ourselves. If we called refresh, there
		// would be a flicker, because the dragged widget would not be redrawn. We simply
		// force the WastePile's image to be updated, but nothing is refreshed on the screen.
		// This is patently OK because the card has not yet been dragged away to reveal the
		// card beneath it.  A bit tricky and I like it!
		src.redraw();
	}

}
