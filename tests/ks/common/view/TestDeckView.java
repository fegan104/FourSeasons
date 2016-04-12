package ks.common.view;


import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.view.DeckView;
import ks.launcher.Main;

public class TestDeckView extends TestCase {

	/** Deck view and Deck model (accessed within dummy). */
	Deck d;
	DeckView dv;
	
	/** Container. */
	GameWindow  gw;

	class Dummy extends Solitaire {
		@Override
		public String getName() { return "dummy"; }

		@Override
		public boolean hasWon() { return false; }

		@Override
		public void initialize() {
			d = new Deck();
			d.create(Deck.OrderByRank);
			
			dv = new DeckView(d);
			dv.setBounds (10, 10, 200, 100);
			addModelElement(d);
			addViewWidget(dv);
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
		//  not much to do. This covers full deck.
		assertEquals (52, d.count());
	}
	
	// make sure can draw empty deck.
	public void testEmpty() {
		//  not much to do. This covers full deck.
		assertEquals (52, d.count());
		d.removeAll();
		
		assertEquals (0, d.count());
	}
}
