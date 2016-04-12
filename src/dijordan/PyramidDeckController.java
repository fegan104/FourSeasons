package dijordan;

import dijordan.model.SelectionManager;
import ks.common.view.DeckView;
import ks.common.model.*;

/******************************************
 * Controller for the Deck widget
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PyramidDeckController extends java.awt.event.MouseAdapter {
	
  /* The game we are partially controlling. */
  protected PyramidGame theGame;
	/* The deck we are managing. */
  protected DeckView deckView;
	/* Manages all selections in the game. */
  protected SelectionManager selectionManager;

/*************
 * Constructor
 */
public PyramidDeckController(PyramidGame pGame, DeckView deckView, SelectionManager sm) {
	super();

	theGame = pGame;
	this.deckView = deckView;
	this.selectionManager = sm;
}  
/**
 * If there is a mouse pressed event, we want to try to deal
 */
public void mousePressed(java.awt.event.MouseEvent me) {

	Pile justDrawn = (Pile)theGame.getModelElement("theJustDrawn");
	Column discards = (Column)theGame.getModelElement("theDiscards");

	/** Get our model element. */
	Deck deck = (Deck) deckView.getModelElement();
	
	Move m = new DealOneMove (deck, justDrawn, discards);
	if (m.doMove (theGame)) {
		// SUCCESS
		theGame.pushMove(m);
	}

	// In all cases, we want to deselect when a card is dealte.	
	selectionManager.clearSelected();
	theGame.refreshWidgets();
}
}
