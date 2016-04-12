Model Narcotic4
===============

As we move on to each successive variation, these README files will only 
contain the changes that you need to apply to the prior version.

  * T5: Construct the Controllers by which the user interacts with the view.
    
We will cover each in turn.

Task T5: 
  
As we have seen, the framework infrastructure handles the dragging of widgets,
now we need to make the full connection. 

We must show how the mouse PRESS hands off the card being dragged to the 
container, and then on mouse RELEASE the requested move is made. The PRESS 
part of the controller is unchanged from v3.NarcoticPileController.  Here is the
RELEASE part:

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
		theGame.repaintAll (cardView.getBounds());   
		theGame.refreshWidgets(); 

		// release the dragging object since the move is now complete (this 
		// will reset container's dragSource).
		c.releaseDraggingObject();
	}  
	
The release adapter first finds if there is any element being dragged by the container. 
If there isn't, then there is no move to be made; otherwise, we get the widget being
dragged. In this example, we "know" that there is only the chance that a CardView widget
is being dragged; in your solitaire variations, this may not be the case, so you may have
to use 'instanceof' to determine the type of widget being dragged. Alternatively, you may
get the 'dragSource' and use that to determine the specific move type.

In this case, we need to determine (a) the fromPile; (b) the toPile; and (c) the card being
dragged. It is imperative to note that when the card is being dragged, the fromPile has already
been modified to remove that card from its pile. See the above for how to compute these
values. Once done, we construct a move object as we did with DealFourMove. If the move
can be made then we push that move onto the solitaire variations stack of moves made
and continue; otherwise we take advantage of the powerful feature that every widget
provides. Specifically, if the widget-being-dragged was properly extracted from a
solitaire widget during the mousePress handler, then returnWidget(w) simply undoes
the earlier operation and restores the card (or cards) being dragged to its source
model element (which in this case is the fromPileView, computed from the container).

Finally, we redraw everything and we have to be sure to tell the container that there
is no longer a need to remember that there is an object being dragged.

Now when you execute the variation and advance two deals, you can drag the 9C onto 
the 9H as required by the rules of Narcotic. Note, however, what happens when you 
drag the 9C and let the mouse release over the DeckViewWidget, or over one of the 
IntegerView widgets. The card remains suspended in space! Perhaps you have guessed
rightly that the DeckView does not properly respond to mouseReleased events. To resolve
the issue, we modified NarcoticDeckController (and placed it within the v4 package) to
respond to Released events properly. Here is the code, which is quite generic:

	public void mouseReleased(java.awt.event.MouseEvent me) {
		Container c = narcoticGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) return;

		/** Recover from wherever it came. */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			c.releaseDraggingObject();		
			return;
		}

		// Simply return the widget to where it came from.
		fromWidget.returnWidget (draggingWidget);

		// Since we could be released over a widget, or over the container, 
		// we must repaintAll() clipped to the region we are concerned about.
		// first refresh the last known position of the card being dragged.
		// then the widgets.
		narcoticGame.repaintAll (draggingWidget.getBounds());   
		narcoticGame.refreshWidgets(); 

		// release the dragging object since the move is now complete (this 
		// will reset container's dragSource).
		c.releaseDraggingObject();
	}
 
  