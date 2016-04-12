package ks.common.view;


import java.awt.event.MouseEvent;
import java.util.Scanner;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.PileView;
import ks.common.view.Widget;
import ks.launcher.Main;

public class TestPileView extends TestCase {

	/** Pile view and Pile model (accessed within dummy). */
	Pile p1, p2;
	PileView pv1, pv2;
	
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
			p1 = new Pile();
			p2 = new Pile();
			
			pv1 = new PileView(p1);
			pv2 = new PileView(p2);
			pv1.setBounds (10, 10, 100, 200);
			pv2.setBounds (200, 10, 100, 200);
			addModelElement(p1);
			addModelElement(p2);
			addViewWidget(pv1);
			addViewWidget(pv2);
			
			// place five cards in Pile1 each
			for (int i = 0; i < 5; i++) {
				p1.add (new Card (i+2, Card.HEARTS));
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
		assertEquals (5, p1.count());
		assertEquals (0, p2.count());
	}
	
	// Try to grab cards from the Pile1 
	public void testGrabCards() {
		// create a mouse event over the exposed card
		// 0,0 is still in the widget.
		
		int x = 15; // in a bit
		int y = 1;
		
		// pull off the "top" exposed card
		MouseEvent press = createPressed (sol, pv1, x, y);
		CardView cv = pv1.getCardViewForTopCard(press);
		
		Card card = (Card) cv.getModelElement();
		assertEquals (new Card (6, Card.HEARTS), card);
		
		// only four cards left
		assertEquals (4, p1.count());
		
		// return this one-card widget
		pv1.returnWidget(cv);
		
		// back to 4 cards
		assertEquals (5, p1.count());
	}
	
	// Try to grab cards from the Pile1 
	public void testSelect() {
		assertTrue (p1.select());
		pv1.redraw();
		
		Container c = sol.getContainer();
		c.repaint();
		
		int x = 15; // in a bit
		int y = 1;
		
		// pull off the "top" exposed card
		MouseEvent pr = createPressed (sol, pv1, x, y);
		CardView cv2 = pv1.getCardViewForTopCard(pr);
		Card card = (Card) cv2.getModelElement();
		assertTrue (card.isSelected());
		assertTrue (card.isFaceUp());
		
		assertTrue (p1.getSelected().empty());
		
		c.repaint();

	}
}
