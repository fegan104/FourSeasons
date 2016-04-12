package vfinal;

import java.awt.event.MouseEvent;

import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

public class TestCases extends KSTestCase {

	
	public void testInitialDeal() {
		// Seed is to ensure we get the same initial cards every time.
		NarcoticFinal nf = new NarcoticFinal();
		GameWindow gw = Main.generateWindow(nf, 117);
		
		assertFalse (nf.pile1.empty());
		assertFalse (nf.pile2.empty());
		assertFalse (nf.pile3.empty());
		assertFalse (nf.pile4.empty());
		gw.dispose();
	}

	// This shows how to manipulate even the deck...
	public void testSpecialDeal() {
		// Seed is to ensure we get the same initial cards every time.
		NarcoticFinal nf = new NarcoticFinal();
		GameWindow gw = Main.generateWindow(nf, Deck.OrderByRank);
		
		// we actually reach on in and Blow away deck with our own
		// Note that this deck is Woefully inadequate (only 4 cards!)
		// but you can do whatever you want. Hey, this is only a test 
		// case, not a full game!
		ModelFactory.init(nf.deck, "2C 2D 2H 2S");
		ModelFactory.init(nf.pile1, "AC");
		ModelFactory.init(nf.pile2, "AD");
		ModelFactory.init(nf.pile3, "AH");
		ModelFactory.init(nf.pile4, "AS");
		
		// be even more precise.
		assertEquals ("AC", nf.pile1.peek().toString());
		assertEquals ("AD", nf.pile2.peek().toString());
		assertEquals ("AH", nf.pile3.peek().toString());
		assertEquals ("AS", nf.pile4.peek().toString());
		
		DealFourMove dfm = new DealFourMove(nf.deck, nf.pile1, nf.pile2, nf.pile3, nf.pile4);
		assertTrue (dfm.valid(nf));
		
		// know where we are
		assertEquals (1, nf.pile1.count());
		assertEquals (1, nf.pile2.count());
		assertEquals (1, nf.pile3.count());
		assertEquals (1, nf.pile4.count());
	
		// make move.
		dfm.doMove(nf);
		
		// assert results.
		assertEquals (2, nf.pile1.count());
		assertEquals (2, nf.pile2.count());
		assertEquals (2, nf.pile3.count());
		assertEquals (2, nf.pile4.count());
		
		// be even more precise.
		assertEquals ("2S", nf.pile1.peek().toString());
		assertEquals ("2H", nf.pile2.peek().toString());
		assertEquals ("2D", nf.pile3.peek().toString());
		assertEquals ("2C", nf.pile4.peek().toString());
		gw.dispose();
	}
	
	public void testDoubleClick() {
		NarcoticFinal nf = new NarcoticFinal();
		GameWindow gw = Main.generateWindow(nf, Deck.OrderByRank);
		
		assertEquals ("KS", nf.pile1.peek().toString());
		assertEquals ("KH", nf.pile2.peek().toString());
		assertEquals ("KD", nf.pile3.peek().toString());
		assertEquals ("KC", nf.pile4.peek().toString());
		
		// Note that (0,0) is still "within" the widget so we are OK.
		MouseEvent dbl = createDoubleClicked (nf, nf.pileView2, 0, 0);
		nf.pileView2.getMouseManager().handleMouseEvent(dbl);
		
		// Note to PROPERLY simulate the double click, we should have
		// created SEVEN events, but such robust testing is likely
		// only necessary for truly complex games.
		
		// MouseEvent press = createPressed (nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(press);
		// MouseEvent release = createReleased (nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(release);
		// MouseEvent clicked = createClicked(nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(clicked);
		// MouseEvent press2 = createPressed (nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(press2);
		// MouseEvent release2 = createReleased (nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(release2);
		// MouseEvent clicked2 = createClicked(nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(clicked2);
		// MouseEvent clicked3 = createDoubleClicked(nf, nf.pileView2, 0, 0);
		// nf.pileView2.getMouseManager().handleMouseEvent(clicked3);
		
		
		//assert cards are all moved.
		assertTrue (nf.pile1.empty());
		gw.dispose();
	}
	
	public void testPileController() {
		NarcoticFinal nf = new NarcoticFinal();
		GameWindow gw = Main.generateWindow(nf, 115);
		
		ModelFactory.init(nf.pile1, "3C 4S 5H");
		ModelFactory.init(nf.pile2, "7S 5D");
		
		assertEquals ("5H", nf.pile1.peek().toString());
		assertEquals ("5D", nf.pile2.peek().toString());
		
		// press a bit offset into the widget.
		MouseEvent press = createPressed (nf, nf.pileView2, 10, 10);
		nf.pileView2.getMouseManager().handleMouseEvent(press);
		
		assertEquals ("5H", nf.pile1.peek().toString());
		assertEquals ("7S", nf.pile2.peek().toString());
		
		// release onto pile1
		MouseEvent release = createReleased (nf, nf.pileView1, 10, 10);
		nf.pileView1.getMouseManager().handleMouseEvent(release);

		// move is made.
		assertEquals ("5D", nf.pile1.peek().toString());
		assertEquals ("7S", nf.pile2.peek().toString());
		gw.dispose();
	}
	
	public void testValidDealFourMove() {
		// Seed is to ensure we get the same initial cards every time.
		// -1 is the seed which returns deck in order of RANK. That is
		// FOUR aces are on the bottom and FOUR kings are on the top.
		// order of cards is Clubs, Diamonds, Hearts, Spades.
		NarcoticFinal nf = new NarcoticFinal();
		GameWindow gw = Main.generateWindow(nf, Deck.OrderByRank);
		
		DealFourMove dfm = new DealFourMove(nf.deck, nf.pile1, nf.pile2, nf.pile3, nf.pile4);
		assertTrue (dfm.valid(nf));
		
		// know where we are
		assertEquals (1, nf.pile1.count());
		assertEquals (1, nf.pile2.count());
		assertEquals (1, nf.pile3.count());
		assertEquals (1, nf.pile4.count());
		
		// make move.
		assertTrue (dfm.doMove(nf));
		
		// assert results.
		assertEquals (2, nf.pile1.count());
		assertEquals (2, nf.pile2.count());
		assertEquals (2, nf.pile3.count());
		assertEquals (2, nf.pile4.count());
		
		// be even more precise.
		assertEquals ("QS", nf.pile1.peek().toString());
		assertEquals ("QH", nf.pile2.peek().toString());
		assertEquals ("QD", nf.pile3.peek().toString());
		assertEquals ("QC", nf.pile4.peek().toString());
		
		// undo move
		assertTrue (dfm.undo(nf));
		
		// be even more precise.
		assertEquals ("KS", nf.pile1.peek().toString());
		assertEquals ("KH", nf.pile2.peek().toString());
		assertEquals ("KD", nf.pile3.peek().toString());
		assertEquals ("KC", nf.pile4.peek().toString());
		gw.dispose();
	}
}
