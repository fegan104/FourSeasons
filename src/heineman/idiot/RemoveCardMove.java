package heineman.idiot;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
/** 
 * Action for Idiot Game: Remove card
 * <p>
 *
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class RemoveCardMove extends ks.common.model.Move {
	/** Column and Card being removed from column. */
	protected Column from;
	protected Card removingCard = null;

	/** All columns in the game. */
	protected Column cols[];
	
	/**
	 * RemoveCardMove constructor .
	 * @param moveType int
	 */
	public RemoveCardMove (Column from, Card removed, Column col1, Column col2, Column col3, Column col4) {
		super ();

		this.from = from;
		this.removingCard = removed;
		Column newCols[] = { col1, col2, col3, col4 };
		cols = newCols;
	}
	
	/**
	 * doMove method comment.
	 */
	public boolean doMove(Solitaire theGame) {
		// VALIDATE:
		if (valid (theGame) == false)
			return false;

		// EXECUTE: save card (for future undo) increment score by one
		removingCard = from.get();

		// update count in score
		theGame.updateScore (+1);
		return true;

	}
	
	/**
	 * undo move.
	 */
	public boolean undo(Solitaire theGame) {
		// VALIDATE:
		if (removingCard == null) return false;

		// UNDO:
		// put the card back and reduce score by one
		from.add (removingCard);

		// update count in score
		theGame.updateScore (-1);

		return true;
	}
	
	/**
	 * validate move.
	 */
	public boolean valid (Solitaire theGame) {
		// VALIDATION:
		boolean validation = false;

		// removeCard (from) : not from.empty() and 
		//	   EXISTS Column c1 suchthat (from != c1) and (c1.suit() == from.suit()) and (c1.rank() > from.rank())
		// NOTE that ACES receive special treatment; must better incorporate aces into model.
		Column cols[] = { (Column) theGame.getModelElement ("col1"),
				(Column) theGame.getModelElement ("col2"),
				(Column) theGame.getModelElement ("col3"),
				(Column) theGame.getModelElement ("col4") };

		// Detected 3/28/2011
		// empty columns are not eligible.
		if (from.empty()) { return false; }
		
		// Detected (9:54 PM Oct/21/2001).
		if (from.rank() != Card.ACE) {
			for (int i = 0; i < cols.length; i++) {
				// skip 'from' column and empty ones
				if (cols[i] == from || cols[i].empty()) continue;

				// must be same suit
				if (cols[i].suit() != from.suit()) continue;

				// if the current column (has same suit) and has larger rank than the from column, we can remove.
				// Note ACES handles specially.
				if (cols[i].rank() > from.rank() || cols[i].rank() == Card.ACE) {
					validation = true;
				}
			}
		}

		return validation;
	}
}
