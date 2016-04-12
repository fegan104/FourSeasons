package dijordan;

import dijordan.model.PositionCard;
import dijordan.model.Pyramid;
import dijordan.model.SelectionManager;
import dijordan.view.PyramidView;
import ks.common.model.*;

/***********************************************
 * Controlls all actions that involve thePyramid
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PyramidPyramidController extends java.awt.event.MouseAdapter {
  /* a copy of the game */
  protected PyramidGame theGame;

  /* Selection manager. */
  protected SelectionManager selectionManager;

  /* PyramidView we are controlling. */
  protected PyramidView pyramidView;
  /*************
   * Constructor
   */
  public PyramidPyramidController(PyramidGame pGame, PyramidView pv, SelectionManager sm) {
	super();
	theGame = pGame;
	this.pyramidView = pv;
	this.selectionManager = sm;
  }  
/**
 * if there is a mouse doubleclick, we want to try to remove king
 */
public void mouseClicked(java.awt.event.MouseEvent me) {
	// nothing...
}
/*********************************************************************
 * if there is a mousepress, we want to select a card or remove a pair
 */
public void mousePressed(java.awt.event.MouseEvent me) {
	int sel = selectionManager.getSelected();

	/** Get our model element. */ 
	Pyramid pyramid = (Pyramid) pyramidView.getModelElement();

	if (sel == SelectionManager.NONE) {
		/* select the pyramid card. This only returns TRUE if nothing else was selected. */
		selectPyramid (me);
		
		// see if moving pyramid...
		Move m = new RemoveKingPyramidMove (pyramid);
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove (m);
			selectionManager.clearSelected();
		}
		
		theGame.refreshWidgets();
		return;
	}


	if (sel == SelectionManager.PYRAMID) {
		// get selected.
		PositionCard secondCard = pyramidView.returnPositionCard (me);
		Move m = new MatchTwoPyramidMove (pyramid, pyramid.peekSelected(), secondCard);
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove (m);
		}
	} else if (sel == SelectionManager.DISCARDS) {
		Column discards = (Column) theGame.getModelElement("theDiscards");
			
		boolean proceed = true;
		if (!pyramid.isSelected()) {
			/* select the pyramid card. This only returns TRUE if nothing else was selected. */
			proceed = selectPyramid (me);
		}
		if (proceed) {
			/* match the pyramid card with discards */
			Move m = new MatchPyramidAndDiscardsMove(pyramid, discards);
			if (m.doMove (theGame)) {
				// SUCCESS
				theGame.pushMove (m);
			}
		}
	} else if (sel == SelectionManager.JUSTDRAWN) {
		Pile justDrawn = (Pile)theGame.getModelElement("theJustDrawn");
		
		boolean proceed = true;
		if (!pyramid.isSelected()) {
			/* select the pyramid card. This only returns TRUE if nothing else was selected. */
			proceed = selectPyramid (me);
		}
		if (proceed) {
			Move m = new MatchPyramidAndJustDrawnMove(pyramid, justDrawn);
			if (m.doMove (theGame)) {
				// SUCCESS
				theGame.pushMove (m);
			}
		}
	}

	selectionManager.clearSelected();
	theGame.refreshWidgets();
}
/**
 * Selects a card in the pyramid with a mouse event
 * returns true if it works
 */
public boolean selectPyramid(java.awt.event.MouseEvent me) {
	/** Get our model element. */
	Pyramid pyramid = (Pyramid) pyramidView.getModelElement();
	
	if (pyramid.empty()) return false;
	
	if (!pyramid.isSelected()) {
		if (selectionManager.getSelected() != SelectionManager.PYRAMID) {
			PositionCard pc = pyramidView.returnPositionCard(me);
			if (pc != null) {
				if(pyramid.select(pc.getRow(), pc.getPosition())) {
					selectionManager.setSelected(SelectionManager.PYRAMID);
					return true;
				}
			}
		}
	}
	return false;
}
}
