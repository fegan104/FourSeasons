package edu.wpi.fegan.four_seasons;

import heineman.klondike.DealCardMove;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;
import ks.tests.KSTestCase;

import java.awt.event.MouseEvent;

/**
 * Created by frankegan on 4/11/16.
 */
public class TestFourSeason extends KSTestCase {
    // this is the game under test.
    FourSeasons game;

    // window for game.
    GameWindow gw;

    protected void setUp() {
        game = new FourSeasons();

        // Because solitaire variations are expected to run within a container, we need to
        // do this, even though the Container will never be made visible. Note that here
        // we select the "random seed" by which the deck will be shuffled. We use the
        // special constant Deck.OrderBySuit (-2) which orders the deck from Ace of clubs
        // right to King of spades.
        gw = Main.generateWindow(game, Deck.OrderBySuit);

    }

    protected void tearDown(){
        gw.setVisible(false);
        gw.dispose();
    }

    public void testFoo(){
        assertEquals(true, true);
    }

    public void testDealOne() {
        DealCardMove move = new DealCardMove(game.stock, game.wastePile);

        assertTrue (game.wastePile.empty());
        assertTrue (move.valid(game));
        assertTrue (move.doMove(game));
        assertEquals (new Card(Card.SEVEN, Card.SPADES), game.wastePile.peek());
        assertTrue (move.undo(game));

        // fix things so they stay broke
        game.stock.removeAll();

        assertFalse (move.valid(game));
        assertFalse (move.doMove(game));

    }

    public void testDeckController() {

        // first create a mouse event
        MouseEvent pr = createPressed(game, game.stockView, 0, 0);
        game.stockView.getMouseManager().handleMouseEvent(pr);

        assertEquals(new Card(Card.SEVEN, Card.SPADES), game.wastePile.peek());
        assertTrue(game.undoMove());
        assertTrue(game.wastePile.empty());
    }
}