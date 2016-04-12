package dijordan.model;

import ks.common.model.*;

/*****************************************************
 * PositionCard is a Card with a notion of where it is
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 */
public class PositionCard extends Card {
	/* the row of the PositionCard */
	protected final int row;

	/* the position of the PositionCard */
	protected final int position;

	/*********************************************************************************
	 * Construct a PositionCard given a rank, suit, row, and position
	 * @exception: IllegalArgumentException if suit, rank, row, or position is invalid
	 */
	public PositionCard(int rank, int suit, int row, int position) {
		super(rank, suit);
		/* row may only be between 1 and 7 inclusive */
		if((row > 7) || (row < 1)) {
			throw new IllegalArgumentException("\"" + row + "\" is an invalid row");
		}
		/* position must be no greater than row */
		if(position > row) {
			throw new IllegalArgumentException("\"" + position + "\" is an invalid position in row \"" + row);
		}
		this.row = row;
		this.position = position;
	}  
	/***************************************************************************
	 * Construct a PositionCard given a PositionCard
	 * @exception: IllegalArgumentException if Card, row, or position is invalid
	 */
	public PositionCard(PositionCard pc) {
		this (pc.getRank(), pc.getSuit(), pc.getRow(), pc.getPosition());
	}        

	/***************************************************************************
	 * Construct a PositionCard given a Card, row, and position
	 * @exception: IllegalArgumentException if Card, row, or position is invalid
	 */
	public PositionCard(Card c, int row, int position) {
		super(c);
		/* row may only be between 1 and 7 inclusive */
		if((row > 7) || (row < 1)) {
			throw new IllegalArgumentException("\"" + row + "\" is an invalid row");
		}
		/* position must be no greater than row */
		if(position > row) {
			throw new IllegalArgumentException("\"" + position + "\" is an invalid position in row \"" + row);
		}
		this.row = row;
		this.position = position;
	}  

	/*****************************************
	 * Return the position of the PositionCard
	 */
	public int getPosition() {
		return position;
	}  

	/************************************
	 * Return the row of the PositionCard
	 */
	public int getRow() {
		return row;
	}  
}
