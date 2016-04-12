package dijordan.view;

import ks.common.view.*;
import ks.common.view.CardImages;
import java.awt.Image;
import java.awt.Graphics;
import dijordan.model.Pyramid;
import dijordan.model.PositionCard;

/******************************************
 * Represents a Pyramid
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PyramidView extends Widget {
	
  /* amount to overlap left and right, initialized in constructor */
  protected int halfCardWidth = 0;
  
  /* amount to overlap down, initialized in constructor */
  protected int cardOverlap = 0;
  
  /* width of card, initialized in constructor */
  protected int cardWidth = 0;
  
	/* the cardXXX values are not ready until this is true. */
  protected boolean calculated = false;
  
  /* height of card, initialized in constructor */
  protected int cardHeight = 0;
  
  /* space between cards at half width, initialized in constructor */
  protected int space = 0;
  
  /* number of rows, initialized in constructor */
  protected int numRows = 0;

  /*************************
   * Construct a PyramidView
   */
  public PyramidView(Pyramid p) {
	super(p);
  }          
/**
 * Calculates constants that need the card images.
 */
public void calculateConstants(CardImages cards) {
	// can only calculate with card images on hand.
	if (cards == null) return;

	this.cards = cards;
	  
	cardWidth = cards.getWidth();
	cardHeight = cards.getHeight();
	cardOverlap = cards.getOverlap();

	space = (cardWidth % 2);
	halfCardWidth = cardWidth / 2;
		
	numRows = ((Pyramid)getModelElement()).countRows();
	calculated = true;
}
/***************************
 * returns height of pyramid
 */
public int getHeight() {
	calculateConstants(cards);
	if (!calculated) {
		System.err.println ("PyramidView::getHeight() can't be called yet. No card images available.");
	}
	  
	return (cardHeight + ((numRows - 1) * cardOverlap));
}
/**************************
 * returns width of pyramid
 */
public int getWidth() {
	calculateConstants(cards);
	if (!calculated) {
		System.err.println ("PyramidView::getWidth() can't be called yet. No card images available.");
	}
	
	return ((cardWidth * numRows) + (space * numRows) + 5) ;
}    
/*******************
 * redraws a pyramid
 */
public void redraw() {
	// if not yet calculated, do so now. We can't calculate these in the constructor
	// because of the order in which initialization proceeds.
	calculateConstants(cards);
	if (!calculated) {
		System.err.println ("PyramidView::redraw() can't be called yet. No card images available.");
	}
	  
	Pyramid p = (Pyramid)getModelElement();
	int imageHeight = getHeight();
	int imageWidth = getWidth();
	int startRow = ((int)(imageWidth / 2)) - halfCardWidth;
	int xcoord, ycoord;
	PositionCard pc;
	Image img;

	/* create the image if it's new */
	if(offscreenImage == null) {
	  if(container == null) {
		System.err.println("PyramidView::redraw(). Invalid request to draw a Widget that is not part of a container.");
		return;
	  }
	  offscreenImage = getContainer().createImage(imageWidth, imageHeight);
	}

	Graphics g = offscreenImage.getGraphics();
	g.setColor(getContainer().getBackground());
	g.fillRect(0, 0, width, height);

	/* draw rows, top to bottom */
	for(int i = 1; i <= numRows; i++) {
	  for(int j = 1; j <= i; j++) {
		pc = p.peekCard(i, j);
		if(pc != null) {
		  if(pc.isFaceUp()) {
			img = cards.getCardImage(pc);
		  } else {
			img = cards.getCardReverse();
		  }
		  xcoord = startRow + ((j - 1) * cardWidth) + ((j - 1) + space);
		  ycoord = (i - 1) * cardOverlap;
		  g.drawImage(img, xcoord, ycoord, container);
		  if(pc.isSelected()) {
			g.setColor(java.awt.Color.cyan);
			g.fillRect(xcoord, ycoord, cardWidth, 3);
			g.fillRect(xcoord, ycoord, 3, cardHeight);
			g.fillRect(xcoord + cardWidth - 3, ycoord, 3, cardHeight);
			g.fillRect(xcoord, ycoord + cardHeight - 3, cardWidth, 3);
		  }
		}
	  }
	  startRow = (startRow - halfCardWidth) - space;
	}

	// no longer needed
	g.dispose();
	
	if(getImage() == null) {
	  /* first time: create image */
	  setImage(container.createImage(width, imageHeight));
	}

	Graphics lc = getImage().getGraphics();
	if (lc != null) {
	  lc.drawImage (offscreenImage, 0, 0, container);
	  lc.dispose(); // no longer needed
	}

	setImage (offscreenImage);
  }            
  /*************************************
   * Returns the PositionCard clicked on
   * Calls peek method, not get
   * Will return null if no card
   */
  public PositionCard returnPositionCard(java.awt.event.MouseEvent me) {
	calculateConstants(cards);
	if (!calculated) {
		System.err.println ("PyramidView::returnPositionCard() can't be called yet. No card images available.");
	}
	  
	Pyramid pyr = (Pyramid)getModelElement();
	if(pyr.empty()) {
	  return null;		// nothing there if it's empty
	}

	/* convert the mouse event point to a point within the widget */
	java.awt.Point p = new java.awt.Point(me.getX() - x, me.getY() - y);

	int imageWidth = getWidth();
	int startRow = ((int)(imageWidth / 2)) - halfCardWidth;
	PositionCard retCard = null;

	for(int i = 1; i <= numRows; i++) {
	  for(int j = 1; j <= i; j++) {
		/* no use continuing if there is no card there */
		if(pyr.isCardThere(i, j)) {
		  int myX = startRow + (cards.getWidth() * (j - 1));
		  int myY = cardOverlap * (i - 1);
		  java.awt.Rectangle r = new java.awt.Rectangle(myX, myY, cardWidth, cardHeight);
		  if(r.contains(p)) {
			retCard = pyr.peekCard(i, j);
		  }
		}
	  }
	  startRow = (startRow - halfCardWidth) - space;
	}
	return retCard;
  }        

}
