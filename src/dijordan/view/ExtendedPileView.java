package dijordan.view;

import ks.common.view.*;
import ks.common.model.*;
import java.awt.Graphics;
import java.awt.Image;

/*****************************************************
 * Represents a Pile, adding functionality to PileView
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class ExtendedPileView extends PileView {

  /*******************************
   * Construct a ExtendedPileView
   */
  public ExtendedPileView(Pile p) {
	super(p);
  }  
  /*****************************************************************************************************
   * This version changes the view of a selected card
   * All code but that small section was taken from PileView in version 1.6.12
   * Minor changes were also made to affect the look of code and change "PileView" to "ExtendedPileView"
   */
  public void redraw() {
	Pile thePile = (Pile) getModelElement();

	// create offscreen Image if not already done so.
	if (offscreenImage == null) {
	  if (getContainer() == null) {
		System.err.println ("ExtendedPileView::redraw(). Invalid request to draw a Widget that is not part of a container.");
		return;
	  }
	  // Create image with width x height of CardImages
	  offscreenImage = getContainer().createImage (cards.getWidth(), cards.getHeight());
	}

	// clear to the background color of the viewing peer.
	java.awt.Graphics g = offscreenImage.getGraphics();
	g.setColor (container.getBackground());
	g.fillRect (0, 0, width, height);

	// Peek at the top card from the pile
	Card c = thePile.peek();
	if (c != null) {
	  Image img = null;
	  if (c.isFaceUp()) {
		img = cards.getCardImage(c);
	  } else {
		img = cards.getCardReverse();
	  }
	  g.drawImage (img, 0, 0, container);
	  
	  if (c.isSelected()) {
		g.setColor(java.awt.Color.cyan);
		g.fillRect(0, 0, cards.getWidth(), 3);
		g.fillRect(0, 0, 3, cards.getHeight());
		g.fillRect(cards.getWidth() - 3, 0, 3, cards.getHeight());
		g.fillRect(0, cards.getHeight() - 3, cards.getWidth(), 3);
	  }
	} else {
	  // Create a thin rectangle outline to show outline of the pile.
	  int w = cards.getWidth();
	  int h = cards.getHeight();

	  // Draw full rectangle
	  g.setColor (java.awt.Color.black);
	  g.drawRect (0, 0, w, h);
	}
	
	// no longer needed.
	g.dispose();
	
	// transfer image once done.
	if (getImage() == null) {
	  // first time create image
	  setImage (container.createImage (cards.getWidth(), cards.getHeight()));
	}
	
	Graphics lc = getImage().getGraphics();
	if (lc != null) {
	  lc.drawImage (offscreenImage, 0, 0, container);
	  lc.dispose();
	}

	setImage(offscreenImage);
  }    
}
