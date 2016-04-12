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
public class MatchPyramidAndDiscardsMove extends Move {
	/** Pyramid. */
	protected Pyramid pyramid;

	/** Discards. */
	protected Column discards;

	/** Card from pyramid. */
	protected PositionCard pyramidCard;

	/** Card from discards. */
	protected Card discardsCard;
/**
 * Constructor
 */
public MatchPyramidAndDiscardsMove(Pyramid pyramid, Column discards) {
	this.pyramid = pyramid;
	this.discards = discards;
	this.discardsCard = null;
	this.pyramidCard = null;
}
  
/**
 * Execute this move.
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE
	if (valid (theGame) == false)
		return false;

	// EXECUTE:
	pyramidCard = pyramid.getSelected ();
	pyramidCard.setSelected(false);
	discardsCard = discards.get();
	discardsCard.setSelected(false);
	  
	theGame.updateScore(+2);
	return true;
}

  
/**
 * to undo this move we must put the cards back
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE
	if (pyramidCard == null) return false;

	// UNDO:
	discards.add(discardsCard);
	pyramid.addCard (pyramidCard);

	discardsCard = null;
	pyramidCard = null;

	theGame.updateScore(-2);
	return true;
}
/**
 * Validate this move.
 */
public boolean valid(Solitaire theGame) {
	boolean validation = false;

	// VALIDATE:
	PositionCard pc = pyramid.peekSelected();
	Card c = discards.peek ();
	
	if ((c != null) && (pc != null)) {
		int row = pc.getRow();
		int pos = pc.getPosition();
		if (!pyramid.isCovered(row, pos)) {
			if ((pc.getRank() + c.getRank()) == 13) {
				validation = true;
			}
		}
	}

	return validation;
}
}
