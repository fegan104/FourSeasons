package heineman;

import java.awt.event.MouseEvent;

import heineman.Idiot;
import heineman.idiot.DealFourMove;
import heineman.idiot.MoveCardMove;
import heineman.idiot.RemoveCardMove;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;

public class TestIdiot extends KSTestCase {
	// this is the game under test.
	Idiot game;
	
	// window for game.
	GameWindow gw;
	
	protected void setUp() {
		game = new Idiot();
		
		// Because solitaire variations are expected to run within a container, we need to 
		// do this, even though the Container will never be made visible. Note that here
		// we select the "random seed" by which the deck will be shuffled. We use the 
		// special constant Deck.OrderBySuit (-2) which orders the deck from Ace of clubs
		// right to King of spades.
		gw = Main.generateWindow(game, Deck.OrderBySuit); 
		
		// First four cards are the K/Q/J/10 of spades.
		assertEquals (new Card(Card.KING, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.TEN, Card.SPADES), game.col4.peek());
	}
	
	// clean up properly
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	public void testDealFour() {
		DealFourMove dfm = new DealFourMove(game.deck,
				game.col1,game.col2,game.col3,game.col4);
		
		assertTrue (dfm.valid(game));
		
		assertTrue (dfm.doMove(game));
		
		// First four cards added are the 9/8/7/6 of spades.
		assertEquals (new Card(Card.NINE, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.EIGHT, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.SEVEN, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.SIX, Card.SPADES), game.col4.peek());
		
		assertTrue (dfm.undo(game));
		
		// Back to original
		assertEquals (new Card(Card.KING, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.TEN, Card.SPADES), game.col4.peek());
		
		// fix things so they stay broke
		game.deck.removeAll();
		
		assertFalse (dfm.valid(game));
		assertFalse (dfm.doMove(game));
		
	}

	public void testRemoveCard() {
		
		RemoveCardMove rcm = new RemoveCardMove(game.col4, new Card(Card.TEN, Card.SPADES),
				game.col1,game.col2,game.col3,game.col4);
		
		assertTrue (rcm.valid(game));
		
		assertTrue (rcm.doMove(game));
		
		// First three cards are the K/Q/J of spades and final column is empty
		assertEquals (new Card(Card.KING, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertTrue (game.col4.empty());
		
		assertTrue (rcm.undo(game));
		
		// Back to original
		assertEquals (new Card(Card.KING, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.TEN, Card.SPADES), game.col4.peek());
		
		// invalid on empty
		game.col4.get();
		
		assertFalse (rcm.valid(game));
		assertFalse (rcm.doMove(game));
		
	}
	
	public void testMoveCard() {
		// first clear a space
		RemoveCardMove rcm = new RemoveCardMove(game.col4, new Card(Card.TEN, Card.SPADES),
				game.col1,game.col2,game.col3,game.col4);
		
		assertTrue (rcm.valid(game));
		assertTrue (rcm.doMove(game));
		
		// Now move King to the empty space. This move expects the card to already have
		// been retrieved from the column.
		Card c = game.col1.get();
		MoveCardMove mcm = new MoveCardMove(game.col1, c, game.col4);
		assertTrue (mcm.valid(game));
		assertTrue (mcm.doMove(game));
		
		// Back to original
		assertTrue (game.col1.empty());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.KING, Card.SPADES), game.col4.peek());
		
		assertTrue (mcm.undo(game));
		
		// Back to original
		assertEquals (new Card(Card.KING, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertTrue (game.col4.empty());
		
		// invalid on empty
		game.col1.get();
	}
	
	public void testDeckGUI() {
		// first create a mouse event
		MouseEvent pr = createPressed (game, game.deckView, 0, 0);
		game.deckView.getMouseManager().handleMouseEvent(pr);
		
		// First four cards added are the 9/8/7/6 of spades.
		assertEquals (new Card(Card.NINE, Card.SPADES), game.col1.peek());
		assertEquals (new Card(Card.EIGHT, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.SEVEN, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.SIX, Card.SPADES), game.col4.peek());
		
	}
	
	public void testColumnGUI() {
		// first clear column four
		MouseEvent dbl = createDoubleClicked (game, game.colView4, 0, 0);
		game.colView4.getMouseManager().handleMouseEvent(dbl);
		
		// Now drag card from col1 to col4
		MouseEvent pr = createPressed(game, game.colView1, 0, 0);
		game.colView1.getMouseManager().handleMouseEvent(pr);
		
		MouseEvent rel = createReleased(game, game.colView4, 0, 0);
		game.colView4.getMouseManager().handleMouseEvent(rel);
		
		// First four cards added are the 9/8/7/6 of spades.
		assertTrue (game.col1.empty());
		assertEquals (new Card(Card.QUEEN, Card.SPADES), game.col2.peek());
		assertEquals (new Card(Card.JACK, Card.SPADES), game.col3.peek());
		assertEquals (new Card(Card.KING, Card.SPADES), game.col4.peek());
		
	}
	
}
