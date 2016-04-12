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
public class MatchTwoPyramidMove extends Move {
	/** Pyramid. */
	protected Pyramid pyramid;

	/** Cards removed from pyramid. */
	protected PositionCard firstCard;
	protected PositionCard secondCard;
/**
 * Constructor
 */
public MatchTwoPyramidMove (Pyramid pyramid, PositionCard card1, PositionCard card2) {
	this.pyramid = pyramid;
	firstCard = card1;
	secondCard = card2;
}    
/********************************************************************
 * doMove not implemented yet
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE
	if (valid (theGame) == false)
		return false;

	// EXECUTE:
	PositionCard c1 = pyramid.getCard (firstCard.getRow(), firstCard.getPosition());
	PositionCard c2 = pyramid.getCard (secondCard.getRow(), secondCard.getPosition());

	firstCard = c1;
	firstCard.setSelected (false);
	secondCard = c2;
	secondCard.setSelected (false);
	
	theGame.updateScore(+2);
	return true;
}
  
/**
 * to undo this move we must put the cards back
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (firstCard == null) return false;

	
	// EXECUTE
	pyramid.addCard (firstCard);
	pyramid.addCard (secondCard);

	firstCard = null;
	secondCard = null;
		
	theGame.updateScore(-2);
	return true;
} 
/**
 * Validate move.
 */
public boolean valid(Solitaire theGame) {
	boolean validation = false;
	
	// VALIDATE
	PositionCard c1 = pyramid.peekCard (firstCard.getRow(), firstCard.getPosition());
	PositionCard c2 = pyramid.peekCard (secondCard.getRow(), secondCard.getPosition());

	// verify that these two cards are in the pyramid, are uncovered, and total to 13.
	if ((c1 != null) && (c2 != null)) {
		if ((!pyramid.isCovered (c1)) && (!pyramid.isCovered (c2))) {
			if (c1.getRank() + c2.getRank() == 13) {
				validation = true;
			}
		}
	}
	
	return validation;
}
 
}
