package dijordan;

import dijordan.model.Pyramid;
import dijordan.model.SelectionManager;
import ks.common.view.*;
import ks.common.model.*;

/******************************************************
 * Controlls all actions that involve theJustDrawn pile
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PyramidJustDrawnController extends java.awt.event.MouseAdapter {
  /* a copy of the game */
  protected PyramidGame theGame;

  /** The widget we are controlling. */
  protected PileView justDrawnView;
  
  /** The brains controlling all selections. */
  protected SelectionManager selectionManager;

/**
 * PyramidJustDrawnController needs to know about game as well as controlling SelectionManager.
 */
public PyramidJustDrawnController(PyramidGame pGame, PileView justDrawnView, SelectionManager sm) {
	super();
	
	theGame = pGame;
	this.justDrawnView = justDrawnView;
	this.selectionManager = sm;
}
/**
 * If there is a mouse doubleclick, Try to remove king.
 */
public void mouseClicked(java.awt.event.MouseEvent me) {
	if (me.getClickCount() > 1) {

		/** Get our model element. */
		Pile justDrawn = (Pile) justDrawnView.getModelElement();

		/** Get discards pile as well. */
		Column discards = (Column) theGame.getModelElement ("theDiscards");
		
		Move m = new RemoveKingJustDrawnMove (justDrawn, discards);
		if (m.doMove(theGame)) {
			// SUCCESS!
			theGame.pushMove (m);

			// elements have changed. Refresh view....
			theGame.refreshWidgets();
		}
	}
}
/**
 * Process a mouse click on the JustDrawn pile.
 */
public void mousePressed(java.awt.event.MouseEvent me) {
	int sel = selectionManager.getSelected();

	if (sel == SelectionManager.NONE) {
		selectJustDrawn();
		theGame.refreshWidgets();
		return;
	}

	/** Get our model element. */
	Pile justDrawn = (Pile) justDrawnView.getModelElement();

	// De-select if we are back on the drawn pile
	if (sel == SelectionManager.JUSTDRAWN) {
		/** Get model. */
		if (!justDrawn.empty()) {
			justDrawn.deselect();
		}
	} else if (sel == SelectionManager.PYRAMID) {
		// match the pyramid card with just drawn.
		Pyramid pyramid = (Pyramid) theGame.getModelElement ("thePyramid");
		
		Move m = new MatchPyramidAndJustDrawnMove (pyramid, justDrawn);
		if (m.doMove(theGame)) {
			// SUCCESS
			theGame.pushMove (m);
		}
	} else if (sel == SelectionManager.DISCARDS) {
		Column discards = (Column) theGame.getModelElement("theDiscards");
		
		Move m = new MatchDiscardsAndJustDrawnMove(discards, justDrawn);
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove(m);
		}
	}


	selectionManager.clearSelected();
	theGame.refreshWidgets();
}            
/**
 * Selects the card in justDrawn.
 * <p>
 * If fails for any reason, then this de-selects any cards
 * @return true on success
 */
public boolean selectJustDrawn() {
	/** Get our model element. */
	Pile justDrawn = (Pile) justDrawnView.getModelElement();

	if (!justDrawn.empty()) {
		if (justDrawn.getNumSelectedCards() == 0) {
			if (!selectionManager.isSelected()) {
				justDrawn.select();
				selectionManager.setSelected (SelectionManager.JUSTDRAWN);
				return true;
			}
		}
	}
	
	/* if for any reason this fails, make sure nothing is selected */
	selectionManager.clearSelected();
	return false;
  }                    
}
