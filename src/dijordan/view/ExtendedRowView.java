package dijordan.view;

import ks.common.view.*;
import ks.common.model.*;
import java.awt.Graphics;
import java.awt.Image;

/***************************************************
 * Represents a Row, adding functionality to RowView
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class ExtendedRowView extends RowView {

  /******************************
   * Construct a ExtendedRowView
   */
  public ExtendedRowView(Column c) {
	super(c);
  }  
  /*********************************
   * Return the width for a RowView.
   * allows for 23 cards
   */
  public int getDefaultWidth(CardImages ci) {
	return  (ci.getWidth() + (23 * ci.getOverlap()));
  }  
  /***************************************************************************************************
   * This version changes the view of a selected card
   * All code but that small section was taken from RowView in version 1.6.12
   * Minor changes were also made to affect the look of code and change "RowView" to "ExtendedRowView"
   */
  public void redraw() {
	// Type the model element.
	Column col = (Column) getModelElement();
	if (col == null) {
	  throw new IllegalArgumentException ("ExtendedRowView::redraw() encountered null Column Model Element.");
	}

	// Determine default values to use
	int overlap = cards.getOverlap();

	int imageWidth = getDefaultWidth(cards);

	// create offscreen Image if not already done so.
	if (offscreenImage == null) {
	  if (container == null) {
		System.err.println ("ColumnView::redraw(). Invalid request to draw a Widget that is not part of a container.");
		return;
	  }
	  offscreenImage = container.createImage (imageWidth, height);
	}

	// clear to the background color of the viewing peer.
	java.awt.Graphics g = offscreenImage.getGraphics();
	g.setColor (container.getBackground());
	g.fillRect (0, 0, width, height);

	// For each card in the Column, draw at increasing offsets, overlapping as we go down.
	// We are always drawing in order of the column: that is, the last card drawn will be
	// the top card in the deck. The only variability, then, is where to draw the first card

	//int start[] = {col.count() - 1, 0};
	// the start[] array determines the starting point in the iteration for LEFT or RIGHT.
	// going LEFT, we want to start at the location determined by the number of covered cards.
	// going RIGHT, we can safely start flush at the edge.
	int start[] = {numCoveredCards(), 0};
	int totalNum = col.count() - firstDrawn();

	int offset[] = {-1, 1};
	for (int i = start[direction], ct = 0; ct < totalNum; i += offset[direction], ct++) {
	  Card c = col.peek (firstDrawn() + ct);
	  Image img;
	  if (c.isFaceUp()) {
		img = cards.getCardImage(c);
	  } else {
		img = cards.getCardReverse();
	  }

	  g.drawImage (img, i*overlap, 0, container);

	  if (c.isSelected()) {
		g.setColor(java.awt.Color.cyan);
		g.fillRect(i*overlap, 0, cards.getWidth(), 3);
		g.fillRect(i*overlap, 0, 3, cards.getHeight());
		g.fillRect(i*overlap + cards.getWidth() - 3, 0, 3, cards.getHeight());
		g.fillRect(i*overlap, cards.getHeight() - 3, cards.getWidth(), 3);
	  }
	}
	
	// no longer needed
	g.dispose();
	
	// transfer image once done.
	if (getImage() == null) {
	  // first time create image
	  setImage (container.createImage (imageWidth, height));
	}
	
	Graphics lc = getImage().getGraphics();
	if (lc != null) {
	  lc.drawImage (offscreenImage, 0, 0, container);
	  lc.dispose();
	}

	setImage(offscreenImage);
  }        
  
}
