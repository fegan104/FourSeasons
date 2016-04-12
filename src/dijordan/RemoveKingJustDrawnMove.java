package dijordan;

import ks.common.games.Solitaire;
import ks.common.model.*;

/******************************************
 * Represents a Move for undo use
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class RemoveKingJustDrawnMove extends Move {
	/** JustDrawn pile. */
	protected Pile justDrawn;
	/** Discards Column. */
	protected Column discards;
	/** Did we move cards from discard pile? */
	protected boolean wasDiscardsEmpty;

	/** King once removed. */
	protected Card removedKing;
/**
 * Constructor for RemoveKingJustDrawnMove.
 */
public RemoveKingJustDrawnMove(Pile justDrawn, Column discards) {
	
	this.justDrawn = justDrawn;
	this.discards = discards;
	removedKing = null;
	wasDiscardsEmpty = discards.empty();
}
/**
 * Do RemoveKingJustDrawn move.
 * @param theGame  ks.common.games.Solitaire 
 */
public boolean doMove(Solitaire theGame) {
	// VALIDATE:
	if (valid (theGame) == false)
	 	return false;

	// EXECUTE:
	removedKing = justDrawn.get();

	if (!discards.empty()) {
		justDrawn.add (discards.get());
		wasDiscardsEmpty = false;
	}
	
	theGame.updateScore(+1);
	return true;
}
/**
 * Undo RemoveKingJustDrawn.
 * @param theGame  ks.common.games.Solitaire 
 */
public boolean undo(Solitaire theGame) {
	// VALIDATE:
	if (removedKing == null) return false;
	
	// UNDO:
	if (!wasDiscardsEmpty) {
		discards.add (justDrawn.get());
	}
	
	justDrawn.add (removedKing);
	removedKing = null;
	
	theGame.updateScore(-1);
	return true;
}
/**
 * Validate RemoveKingJustDrawn.
 * @param theGame  ks.common.games.Solitaire
 */
public boolean valid (Solitaire theGame) {
	boolean validation = false;
	
	// VALIDATE:
	if ((!justDrawn.empty()) && (justDrawn.rank() == Card.KING))
		validation = true;

	return validation;
}
}
