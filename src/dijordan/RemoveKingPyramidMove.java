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
public class RemoveKingPyramidMove extends Move {
	/** Pyramid. */
	protected Pyramid pyramid;

	/** King removed. */
	protected PositionCard pyramidCard;
	/**
	 * Constructor
	 */
	public RemoveKingPyramidMove(Pyramid pyramid) {
		this.pyramid = pyramid;
		pyramidCard = null;
	}    
	/**
	 * doMove not yet implemented
	 */
	public boolean doMove(Solitaire theGame) {
		// VALIDATE
		if (valid (theGame) == false) {
			return false;
		}

		// EXECUTE:
		pyramidCard = pyramid.getSelected();
		pyramidCard.setSelected(false);
		theGame.updateScore(+1);

		return true;
	}        
	/**
	 * to undo this move we must put the card back
	 */
	public boolean undo(Solitaire theGame) {
		// VALIDATE:
		if (pyramidCard == null) return false;

		// EXECUTE:

		pyramid.addCard (pyramidCard);
		pyramidCard = null;

		theGame.updateScore(-1);
		return true;
	}      
	/**
	 * Validate this move.
	 */
	public boolean valid (Solitaire theGame) {
		boolean validation = false;

		// VALIDATE
		Card pc = pyramid.peekSelected();
		if (pc != null) {
			validation = (pc.getRank() == Card.KING);
		}

		// VALIDATE:

		return validation;
	}
}
