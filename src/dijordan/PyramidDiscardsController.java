package dijordan;

import ks.common.model.*;
import dijordan.view.ExtendedRowView;
import dijordan.model.SelectionManager;
import dijordan.model.Pyramid;

/****************************************************
 * Controlls all actions that involve theDiscards row
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PyramidDiscardsController extends java.awt.event.MouseAdapter {
  /* a copy of the game */
  protected PyramidGame theGame;

  /** Our controlled widget. */
  protected ExtendedRowView  discardsView;

  /* Controls selections. */
  protected SelectionManager selectionManager;

/**
 * Constructor
 */
public PyramidDiscardsController(PyramidGame pGame, ExtendedRowView discardsView, SelectionManager sm) {
	super();
	
	theGame = pGame;
	this.discardsView = discardsView;
	this.selectionManager = sm;
}
/**
 * if there is a mouse doubleclick, we want to try to remove king
 */
public void mouseClicked(java.awt.event.MouseEvent me) {
	if(me.getClickCount() > 1) {
		/** Get our model. */
		Column discards = (Column) discardsView.getModelElement();

		Move m = new RemoveKingDiscardsMove (discards);
		if (m.doMove(theGame)) {
			// SUCCESS
			theGame.pushMove (m);
			theGame.refreshWidgets();
		}
	}
}
/**
 * if there is a mousepress, we want to select a card or remove a pair
 */
public void mousePressed(java.awt.event.MouseEvent me) {
	int sel = selectionManager.getSelected();

	if (sel == SelectionManager.NONE) {
		selectDiscards ();
		theGame.refreshWidgets();
		return;
	}

	/** Get our model. */
	Column discards = (Column) discardsView.getModelElement();
	
	if (sel == SelectionManager.DISCARDS) {
		if (! discards.empty()) {
			discards.deselect();
		}
	} else if (sel == SelectionManager.PYRAMID) {
		Pyramid p = (Pyramid)theGame.getModelElement("thePyramid");
		Move m = new MatchPyramidAndDiscardsMove(p, discards);
		
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove(m);
		} else {
			selectionManager.clearSelected();			
		}
	} else if (sel == SelectionManager.JUSTDRAWN) {
		Pile justDrawn = (Pile)theGame.getModelElement("theJustDrawn");
		
		Move m = new MatchDiscardsAndJustDrawnMove(discards, justDrawn);
		if (m.doMove (theGame)) {
			// SUCCESS
			theGame.pushMove(m);
		} else {
			selectionManager.clearSelected();
		}
	}

	theGame.refreshWidgets();
	selectionManager.clearSelected();		
	return;
}  
/**
 * Selects the top card in discards with a mouse event
 * returns true if it works
 */
public boolean selectDiscards() {

	/** Get our model element. */
	Column discards = (Column) discardsView.getModelElement();
	
	if (discards.empty()) return false;

	discards.select();
	
	selectionManager.setSelected (SelectionManager.DISCARDS);
	return true;
}
}
