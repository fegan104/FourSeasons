package dijordan;

import ks.common.games.Solitaire;
import ks.common.model.*;

import dijordan.model.Pyramid;
import dijordan.model.PositionCard;

/******************************************
 * Represents a Move for undo use
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class MatchPyramidAndJustDrawnMove extends Move {
	/** Pyramid. */
	protected Pyramid pyramid;
	
	/** Pyramid Card. */
	protected PositionCard pyramidCard;

	/** Just Drawn. */
	protected Pile justDrawn;
	
	/** Just Drawn Card. */
	protected Card justDrawnCard;
/** 
 * Constructor
 */
public MatchPyramidAndJustDrawnMove(Pyramid pyramid, Pile justDrawn) {
	this.pyramid = pyramid;
	this.justDrawn = justDrawn;

	justDrawnCard = null;
	pyramidCard = null;
}    
/**
 * Do MatchPyramidAndJustDrawnMove
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE
	if (valid (theGame) == false) {
		return false;
	}

	// EXECUTE:
	justDrawnCard = justDrawn.get();
	pyramidCard = pyramid.getSelected ();

	// un-hilight these cards.
	justDrawnCard.setSelected (false);
	pyramidCard.setSelected (false);
	
	theGame.updateScore(+2);
	return true;
}
  
/**
 * Undo MatchPyramidAndJustDrawnMove.
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (pyramidCard == null) return false;

	// UNDO:
	justDrawn.add (justDrawnCard);
	pyramid.addCard (pyramidCard);
	
	justDrawnCard = null;
	pyramidCard = null;
	
	theGame.updateScore(-2);
	return true;
}        
/**
 * Validate MatchPyramidAndJustDrawnMove.
 */
public boolean valid (Solitaire theGame) {
	boolean validation = false;
	
	// VALIDATE:
	PositionCard pc = pyramid.peekSelected();
	if (pc != null) {
		int row = pc.getRow();
		int pos = pc.getPosition();
		
		if (!pyramid.isCovered(row, pos)) {
			Card drawnCard = justDrawn.peek();
			
			if ((drawnCard.getRank() + pc.getRank()) == 13) {
				validation = true;
			}
		}
	}
	
	return validation;
}        
}
