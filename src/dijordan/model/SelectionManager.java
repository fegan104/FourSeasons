package dijordan.model;

import ks.common.model.Pile;
import ks.common.model.Column;
/**
 * Class responsible for knowing which cards are selected in the game.
 * <p>
 * All controllers operating over PyramidGame must be aware of this
 * class to ensure proper operation.
 * Creation date: (1/6/02 9:47:15 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class SelectionManager {
  /* Stores the name of a location where a card has been selected */
  protected int whereSelected;

  /* Values that whereSelected may take on */
  public static final int NONE = 0;
  public static final int PYRAMID = 1;
  public static final int DISCARDS = 2;
  public static final int JUSTDRAWN = 3;

  protected Pyramid pyramid;
  protected Column discards;
  protected Pile justDrawn;
/**
 * SelectionManager constructor comment.
 */
public SelectionManager(Pyramid pyramid, Column discards, Pile justDrawn) {
	super();

	this.pyramid = pyramid;
	this.discards = discards;
	this.justDrawn = justDrawn;
}
/**
 * Clears the selection (if any)
 */
public void clearSelected() {
	whereSelected = NONE;

	pyramid.deselect();
	justDrawn.deselect();
	discards.deselect();
}    
/**
 * Returns which entity is selected.
 */
public int getSelected() {
	return whereSelected;
}
/**
 * Decides if there is a selected card somewhere
 */
public boolean isSelected() {
	return !(whereSelected == NONE);
}    
/**
 * Sets the selected location.
 * <p>
 * Valid arguments are constant PYRAMID, DISCARDS, JUSTDRAWN, or NONE
 * @exception: IllegalArgumentException if toWhere is not valid
 */
public void setSelected (int toWhere) {
	if ((toWhere == PYRAMID) || (toWhere == DISCARDS) || (toWhere == JUSTDRAWN) || (toWhere == NONE)) {
		whereSelected = toWhere;
		return;
	}

	throw new IllegalArgumentException("SelectionManager::setSelected() cannot set location, bad selection Index");
}
}
