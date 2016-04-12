package heineman.idiot;

import java.awt.event.MouseEvent;

import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;
import heineman.Idiot;

/**
 * Controller for all interaction with the ColumnView widget.
 * <p>
 * Creation date: (1/2/02 10:18:49 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class IdiotColumnAdapter extends java.awt.event.MouseAdapter {
	/** The Solitaire game. */
	protected Idiot theGame = null;

	/** My object. */
	protected ColumnView src;
	
	/**
	 * IdiotColumnAdapter constructor.
	 */
	public IdiotColumnAdapter (Idiot theGame, ColumnView columnView) {
		this.theGame = theGame;

		src = columnView;
	}
	
	/**
	 * React to the removeCard action .
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseClicked(MouseEvent me) {
		// Respond to DoubleClick events
		if (me.getClickCount() > 1) {
			Column col = (Column) src.getModelElement();
			if (col == null) {
				throw new IllegalArgumentException ("Idiot::clickCardController() received invalid ColumnView.");
			}

			// Extract columns from game (a bit of a HACK)
			Column col1 = (Column) theGame.getModelElement("col1");
			Column col2 = (Column) theGame.getModelElement("col2");
			Column col3 = (Column) theGame.getModelElement("col3");
			Column col4 = (Column) theGame.getModelElement("col4");			

			Move m = new RemoveCardMove (col, null, col1, col2, col3, col4);
			if (m.doMove (theGame)) {
				// SUCCESS
				// add move to our set of moves. Note that removeCard() updates lastCardRemoved
				theGame.pushMove (m);
				theGame.refreshWidgets(); // to get column that lost card to be redrawn.
			}
		}
	}
	
	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 * <p>
	 * Note: There is no way to differentiate between a press that
	 *       will become part of a double click vs. a click that will
	 *       be held and dragged. Only releaseCardController will be
	 *       able to help us out with that one.
	 * <p>
	 * Note: uses KombatSolitaire v1.6 capability of having container remember drag source widget.
	 * <p>
	 * Creation date: (10/4/01 6:05:55 PM)
	 * @param me java.awt.event.MouseEvent
	 */
	public void mousePressed (java.awt.event.MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		Column col = (Column) src.getModelElement();
		if (col.count() == 0) {
			c.releaseDraggingObject();
			return;
		}

		// Could be something! Verify that the user clicked on the TOP card in the Column.
		// Note that this method will alter the model for columnView if the condition is met.
		CardView cardView = src.getCardViewForTopCard(me);
		if (cardView == null) {
			return;
		}

		// If we get here, then the user has indeed clicked on the top card in the ColumnView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		Widget w = c.getActiveDraggingObject();
		if (w != Container.getNothingBeingDragged()) {
			System.err.println ("Idiot::pressCardController(): Unexpectedly encountered a Dragging Object during a Mouse press.");
			return;
		}

		// Tell container which object is being dragged, and where in that widget the mouse was clicked.
		c.setActiveDraggingObject (cardView, me);

		// Have container remember who initiated the drag
		c.setDragSource (src);

		// we simply redraw our source pile to avoid flicker,
		// rather than refreshing all widgets...
		src.redraw();
	}
	
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * Note: if cv is null, then it is assumed that the mouse was released on a non-ColumnView Widget
	 * and this is a sign that no move has been made by the user. When this happens, the card must
	 * be returned to the originating column from which it was dragged.
	 * <p>
	 * Note: uses KombatSolitaire v1.6 capability of having container remember drag source widget.
	 * <p>
	 * Creation date: (10/4/01 6:56:34 PM)
	 * @param cv edu.wpi.cs.solitaire.common.view.ColumnView
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased (java.awt.event.MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();
		if (w == Container.getNothingBeingDragged()) return;

		/** Must be the CardView widget. */
		CardView cardView = (CardView) w;
		Card theCard = (Card) cardView.getModelElement();
		if (theCard == null) {
			System.err.println ("Idiot::releaseCardController(): somehow CardView model element is null.");
			return;
		}

		/** Recover the From Column */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("Idiot::releaseCardController(): somehow fromWidget is null.");
			return;
		}
		Column fromColumn = (Column) fromWidget.getModelElement();

		Column toColumn = (Column) src.getModelElement();

		// Try to make the move
		Move m = new MoveCardMove (fromColumn, theCard, toColumn);
		if (m.doMove (theGame)) {
			// Successful move!  
			// add move to our set of moves
			theGame.pushMove (m);
		} else {
			// Invalid move. Restore dragging widget to source
			fromWidget.returnWidget (w);
		}

		c.releaseDraggingObject();    // also releases dragSource

		c.repaint();
	}
}
