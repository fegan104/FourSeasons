package ks.common.view;


import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.view.CardImages;
import ks.common.view.CardView;
import ks.common.view.RowView;
import ks.common.view.Widget;
import ks.launcher.Main;

public class TestRowView extends TestCase {

	/** RowView and Row model (accessed within dummy). */
	Column c1, c2;
	RowView rv1, rv2;
	
	/** Container. */
	GameWindow  gw;

	/** (dx,dy) are offsets into the widget space. Feel Free to Use as Is. */
	public MouseEvent createPressed (Solitaire game, Widget view, int dx, int dy) {
		MouseEvent me = new MouseEvent(game.getContainer(), MouseEvent.MOUSE_PRESSED, 
				System.currentTimeMillis(), 0, 
				view.getX()+dx, view.getY()+dy, 0, false);
		return me;
	}
	
	class Dummy extends Solitaire {
		@Override
		public String getName() { return "dummy"; }

		@Override
		public boolean hasWon() { return false; }

		@Override
		public void initialize() {
			c1 = new Column();
			c2 = new Column();
			
			rv1 = new RowView(c1);
			rv1.setDirection(RowView.LEFT);
			rv2 = new RowView(c2);
			rv2.setDirection(RowView.RIGHT);
			rv2.setJustification(RowView.LEFT);
			
			rv1.setBounds (400, 10, 300, 100);
			rv2.setBounds (10, 10, 300, 100);
			addModelElement(c1);
			addModelElement(c2);
			addViewWidget(rv1);
			addViewWidget(rv2);
			
			// place five cards in Column1 and Column2 each
			for (int i = 0; i < 5; i++) {
				c1.add (new Card (i+2, Card.HEARTS));
				c2.add (new Card (i+2, Card.SPADES));
			}
		}
	}
	
	/** Solitaire variation. */
	Solitaire sol;
	
	protected void setUp() {
		// Seed is to ensure we get the same initial cards every time.
		gw = Main.generateWindow(sol = new Dummy(), 117);
	}
	
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	public void testBase() {
		//  not much to do. 
		assertEquals (5, c1.count());
		assertEquals (5, c2.count());
	}
	
	// Try to grab cards from the ColumnView1 
	public void testGrabCards() {
		// create a mouse event over the exposed card
		// 0,0 is still in the widget.
		CardImages ci = sol.getCardImages();
		
		int x = 15; // in a bit
		int y = ci.getOverlap() * 4;
		
		// pull off the "top" exposed card
		MouseEvent press = createPressed (sol, rv1, x, y);
		CardView cv = rv1.getCardViewForTopCard(press);
		
		Card card = (Card) cv.getModelElement();
		assertEquals (new Card (6, Card.HEARTS), card);
		
		// only four cards left
		assertEquals (4, c1.count());
		
		// return this one-card widget
		rv1.returnWidget(cv);
		
		// back to 4 cards
		assertEquals (5, c1.count());
		
		
		// do the same with rv2, but this time to the right
		press = createPressed (sol, rv2, 4*ci.getOverlap(), 1);
		cv = rv2.getCardViewForTopCard(press);
		
		card = (Card) cv.getModelElement();
		assertEquals (new Card (6, Card.SPADES), card);
		
		// only four cards left
		assertEquals (4, c2.count());
		
		// return this one-card widget
		rv2.returnWidget(cv);
		
		// back to 4 cards
		assertEquals (5, c2.count());
		
		// now grab cards two-four as a column
		x = 15;
		y = ci.getOverlap();
		press = createPressed (sol, rv2, 2*ci.getOverlap(), 1);
		RowView drv = rv2.getRowView(press);
		
		// just one left
		assertEquals (1, c2.count());
		
		// four cards in column extracted from columnView
		Column cc = (Column) drv.getModelElement();
		assertEquals (4, cc.count());
		
		// return this widget back to the column
		rv2.returnWidget(drv);
		
		// four cards back there.
		assertEquals (5, c2.count());
		
	}
}
