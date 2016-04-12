package dijordan.model;

import ks.common.model.*;

/***************************************************************************************
 * Models a Pyramid of cards which has rows with increasing numbers of cards in each row
 * <p>
 * NOTE: Only once card (row,pos) can be selected at any given time.
 * <p>
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class Pyramid extends Element {
  /* array of PositionCards that makes up Pyramid */
  protected PositionCard[][] pyrArray;

  /* number of rows in the pyramid */
  protected int numRows;

  /* number of cards in the Pyramid */
  protected int numCards;

  /* name counter */
  protected int nameCounter = 1;

  /* selected row (or -1 if nothing selected). */
  protected int selectedRow = -1;

  /* selected position (or -1 if nothing selected). */
  protected int selectedPos = -1;

  
  /*********************************************************************
   * Construct a Pyramid with the default of 7 rows and a generated name
   */
  public Pyramid() {
	this(7, "");
	setName(new String("pyramid" + (nameCounter++)));
  }  
  /**********************************************************************
   * Construct a Pyramid given a number of rows and with a generated name
   */
  public Pyramid(int numRows) {
	this(numRows, "");
	setName(new String("pyramid" + (nameCounter++)));
  }  
  /*************************************************************
   * Construct a Pyramid given a number of rows and a name
   * @exception: IllegalArgumentException if rows is less than 1
   */
  public Pyramid(int numRows, String name) {
	super();
	if(numRows < 1) {
	  throw new IllegalArgumentException("There must be at least one row");
	}

	/* Because we want to number from 1, not 0, we must make the array big enough */
	pyrArray = new PositionCard[numRows+1][numRows+1];
	this.numRows = numRows;
	numCards = 0;
	setName(name);
  }  
  /*************************************************************
   * Construct a Pyramid given a name with the default of 7 rows
   */
  public Pyramid(String name) {
	this(7, name);
  }  
  /******************************************************************
   * Adds a PositionCard to the pyramid
   * returns false if there is already a card there
   * @exception: IllegalArgumentException if the positioncard is null
   */
  public boolean addCard(PositionCard pc) {
	if(pc == null) {
	  throw new IllegalArgumentException("Cannot add null card to pyramid");
	}

	int r = pc.getRow();
	int p = pc.getPosition();
	if (pyrArray[r][p] != null) {
		return false;
	}
	
	pyrArray[r][p] = pc;
	hasChanged();
	return true;
}    
  /************************************
   * Returns number of cards in pyramid
   */
  public int countCards() {
	return numCards;
  }  
  /***************************************
   * Returns the number of rows in pyramid
   */
  public int countRows() {
	return numRows;
  }  
  /****************************************************************************
   * Deal a pyramid
   * @exception: IllegalArgumentException if deck is null
   * @exception: IllegalStateException if pyramid is not empty or deck is empty
   */
  public void deal(Deck d) {
	if(d == null) {
	  throw new IllegalArgumentException("Deck to be dealt cannot be null");
	}
	if(d.empty()) {
	  throw new IllegalStateException("Deck is empty, cannot deal");
	}
	if(!empty()) {
	  throw new IllegalStateException("Cannot deal over non-empty pyramid");
	}
	Card c;
	PositionCard pc;

	for(int r = 1; r <= numRows ; r++) {
	  for(int p = 1; p <= r; p++) {
		if(d.empty()) {
		  throw new IllegalStateException("Deck has become empty, cannot complete deal");
		}
		c = d.get();
		pc = new PositionCard(c, r, p);
		pyrArray[r][p] = pc;
		numCards += 1;
	  }
	}

	hasChanged();
  }  
  /*********************************************
   * Makes all cards in the pyramid not selected
   */
  public void deselect() {
	  if (selectedRow == -1) return;  // nothing to do.

	  Card c = pyrArray[selectedRow][selectedPos];
	  if (c != null) {
		  c.setSelected (false);
	  }

	  // no longer selected.
	  selectedRow = selectedPos = -1;
	  hasChanged();
  }
  
  /********************************
   * Determines if pyramid is empty
   */
  public boolean empty() {
	return (numCards == 0);
  }  
  /***********************************************************************
   * Returns a card by row and position and removes it from the pyramid
   * @exception: IllegalArgumentException if row or position is invalid
   * @exception: IllegalStateException if the card is covered or not there
   */
  public PositionCard getCard(int row, int position) {
	if((row > numRows) || (row <= 0)) {
	  throw new IllegalArgumentException("\"" + row + "\" is an invalid row number");
	}
	if((position > numRows) || (position <= 0)) {
	  throw new IllegalArgumentException("\"" + position + "\" is an invalid position number");
	}

	PositionCard pc = pyrArray[row][position];
	if(pc != null) {
	  if(!isCovered(row, position)) {
		pyrArray[row][position] = null;
		hasChanged();
		return pc;
	  } else {
		throw new IllegalStateException("The card at " + row + "," + position + " is covered");
	  }
	} else {
	  throw new IllegalStateException("There is no card at " + row + "," + position);
	}
  }  
/**
 * Returns the selected card and removes it from the pyramid (or null if none selected).
 */
public PositionCard getSelected() {
	if (selectedRow == -1) return null;

	PositionCard c = pyrArray[selectedRow][selectedPos];
	if (c == null) return null;

	pyrArray[selectedRow][selectedPos] = null;
	hasChanged();
	return c;
}    
  /*****************************************************
   * Determines if there is a card at a row and position
   * @exception: IllegalArgumentException if row or position is invalid
   */
  public boolean isCardThere(int row, int position) {
	if((row > numRows) || (row <= 0)) {
	  throw new IllegalArgumentException("\"" + row + "\" is an invalid row number");
	}
	if((position > numRows) || (position <= 0)) {
	  throw new IllegalArgumentException("\"" + position + "\" is an invalid position number");
	}

	if(pyrArray[row][position] == null) {
	  return false;
	} else {
	  return true;
	}
  }  
  /********************************************************************
   * determines if a card at row and position is covered
   * @exception: IllegalArgumentException if row or position is invalid
   * @exception: IllegalStateException if there is no card there
   */
  public boolean isCovered(int row, int position) {
	if((row > numRows) || (row <= 0)) {
	  throw new IllegalArgumentException("\"" + row + "\" is an invalid row number");
	}
	if((position > numRows) || (position <= 0)) {
	  throw new IllegalArgumentException("\"" + position + "\" is an invalid position number");
	}
	if(pyrArray[row][position] == null) {
	  throw new IllegalStateException("There is no card at " + row + "," + position);
	}
	/* do this to check if it is the bottom row */
	/* the bottom row is never covered */
	/* make sure to return if it is, so that the following if statement never gets run */
	if(row == numRows) {
	  return false;
	}
	if((pyrArray[row + 1][position] == null) && (pyrArray[row + 1][position + 1] == null)) {
	  return false;
	} else {
	  return true;
	}
  }  
  /********************************************************************
   * determines if a card at row and position is covered
   * @exception: IllegalArgumentException if row or position is invalid
   * @exception: IllegalStateException if there is no card there
   */
  public boolean isCovered (PositionCard pc) {
	  if (pc == null) {
		  throw new IllegalArgumentException ("Pyramid::isCovered() received null argument.");
	  }
	  
	  return isCovered (pc.getRow(), pc.getPosition());
  }
  /*******************************************************
   * Determines if there is a selected card in the pyramid
   */
  public boolean isSelected() {
	  return (selectedRow != -1);
	  
  }    
  /*********************************************************************
   * Returns a card by row and position, does not remove it
   * If the card is not there, it returns null
   * @exception: IllegalArgumentException if row or position is invalid
   */
  public PositionCard peekCard(int row, int position) {
	if((row > numRows) || (row <= 0)) {
	  throw new IllegalArgumentException("\"" + row + "\" is an invalid row number");
	}
	if((position > numRows) || (position <= 0)) {
	  throw new IllegalArgumentException("\"" + position + "\" is an invalid position number");
	}

	PositionCard pc = pyrArray[row][position];
	return pc;
  }    
/**
 * Returns selected card without removing it (or null if none is selected).
 */
 public PositionCard peekSelected() {
	 if (selectedRow == -1) return null;

	 PositionCard pc = pyrArray[selectedRow][selectedPos];
	 return pc;
}
  /***********************************************************************
   * Selects a card by row and position
   * Returns true if card is selected, false if not
   * @exception: IllegalArgumentException if row or position is invalid
   * @exception: IllegalStateException if another card is already selected
   */
  public boolean select(int row, int position) {
	if((row > numRows) || (row <= 0)) {
	  throw new IllegalArgumentException("\"" + row + "\" is an invalid row number");
	}
	if((position > numRows) || (position <= 0)) {
	  throw new IllegalArgumentException("\"" + position + "\" is an invalid position number");
	}
	if(isSelected()) {
	  throw new IllegalStateException("Cannot select more than one card");
	}

	if(!isCovered(row, position)) {

		PositionCard pc = pyrArray[row][position];
		pc.setSelected (true);
		hasChanged();
		selectedRow = row;
		selectedPos = position;
		return true;
	}
	return false;
  }      
  /**********************
   * print name as string
   */
  public String toString() {
	return getName();
  }  
}
