package ks.common.view;


import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.view.CardImages;
import ks.common.view.CardView;
import ks.launcher.Main;

public class TestCardView extends TestCase {

	/** Card view and Card model (accessed within dummy). */
	Card c;
	CardView cv;
	
	/** Container. */
	GameWindow  gw;

	class Dummy extends Solitaire {
		@Override
		public String getName() { return "dummy"; }

		@Override
		public boolean hasWon() { return false; }

		@Override
		public void initialize() {
			
			CardImages ci = this.getCardImages();
			int w = ci.getWidth();
			int h = ci.getHeight();
			
			// model
			c = new Card(2, Card.HEARTS);
			Card c2 =new Card(9, Card.SPADES);
			c2.setFaceUp(false);
			addModelElement(c);
			addModelElement(c2);
			
			// view
			cv = new CardView(c); 
			cv.setBounds (10, 10, w, h);
			
			CardView cv2 = new CardView(c2);
			cv2.setBounds (20+w, 10, w, h);
			
			addViewWidget(cv);
			addViewWidget(cv2);
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
		assertEquals (2, c.getRank());
	}
	
}
