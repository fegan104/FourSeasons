package ks.common.view;


import java.awt.event.MouseEvent;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.view.CardImages;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Widget;
import ks.launcher.Main;

public class TestColumnView extends TestCase {

	/** Column view and Column model (accessed within dummy). */
	Column c1, c2;
	ColumnView cv1, cv2;
	
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
			
			cv1 = new ColumnView(c1);
			cv2 = new ColumnView(c2);
			cv1.setBounds (10, 10, 100, 200);
			cv2.setBounds (200, 10, 100, 200);
			addModelElement(c1);
			addModelElement(c2);
			addViewWidget(cv1);
			addViewWidget(cv2);
			
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
		sol = null;
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
		MouseEvent press = createPressed (sol, cv1, x, y);
		CardView cv = cv1.getCardViewForTopCard(press);
		
		Card card = (Card) cv.getModelElement();
		assertEquals (new Card (6, Card.HEARTS), card);
		
		// only four cards left
		assertEquals (4, c1.count());
		
		// now grab "bottom" card [i.e., the most deeply covered one]
		press = createPressed (sol, cv1, 15, 1);
		cv = cv1.getCardViewForBottomCard(press);
		
		card = (Card) cv.getModelElement();
		assertEquals (new Card (2, Card.HEARTS), card);
		
		// only three cards left
		assertEquals (3, c1.count());
		
		// return this one-card widget
		cv1.returnWidget(cv);
		
		// back to 4 cards
		assertEquals (4, c1.count());
		
		// now grab cards two-four as a column
		x = 15;
		y = ci.getOverlap();
		press = createPressed (sol, cv1, x, y);
		ColumnView dcv = cv1.getColumnView(press);
		
		// just one left
		assertEquals (1, c1.count());
		
		// three cards in column extracted from columnView
		Column cc = (Column) dcv.getModelElement();
		assertEquals (3, cc.count());
		
		// return this widget back to the column
		cv1.returnWidget(dcv);
		
		// four cards back there.
		assertEquals (4, c1.count());
	}

	
}
