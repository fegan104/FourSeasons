package heineman;

import heineman.klondike.DealCardMove;
import heineman.klondike.FlipCardMove;
import heineman.klondike.MoveColumnMove;
import heineman.klondike.MoveWasteToPileMove;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.ColumnView;
import ks.launcher.Main;
import ks.tests.KSTestCase;

import java.awt.event.MouseEvent;

public class TestKlondike extends KSTestCase {
    // this is the game under test.
    Klondike game;

    // window for game.
    GameWindow gw;

    protected void setUp() {
        game = new Klondike();

        // Because solitaire variations are expected to run within a container, we need to
        // do this, even though the Container will never be made visible. Note that here
        // we select the "random seed" by which the deck will be shuffled. We use the
        // special constant Deck.OrderBySuit (-2) which orders the deck from Ace of clubs
        // right to King of spades.
        gw = Main.generateWindow(game, Deck.OrderBySuit);

    }

    // clean up properly
    protected void tearDown() {
        gw.setVisible(false);
        gw.dispose();
    }

    public void testDealOne() {
        DealCardMove dfm = new DealCardMove(game.deck, game.wastePile);

        assertTrue(game.wastePile.empty());
        assertTrue(dfm.valid(game));
        assertTrue(dfm.doMove(game));
        assertEquals(new Card(Card.JACK, Card.DIAMONDS), game.wastePile.peek());
        assertTrue(dfm.undo(game));

        // fix things so they stay broke
        game.deck.removeAll();

        assertFalse(dfm.valid(game));
        assertFalse(dfm.doMove(game));
    }

    public void testDeckController() {

        // first create a mouse event
        MouseEvent pr = createPressed(game, game.deckView, 0, 0);
        game.deckView.getMouseManager().handleMouseEvent(pr);

        assertEquals(new Card(Card.JACK, Card.DIAMONDS), game.wastePile.peek());
        assertTrue(game.undoMove());
        assertTrue(game.wastePile.empty());
    }

    public void testPileController() {

        // first create a mouse event
        MouseEvent pr = createPressed(game, game.pileViews[7], 0, 6 * game.pileViews[7].getSmallOverlap());
        game.pileViews[7].getMouseManager().handleMouseEvent(pr);

        // drop on the first column
        MouseEvent rel = createReleased(game, game.pileViews[1], 0, 0);
        game.pileViews[1].getMouseManager().handleMouseEvent(rel);

        assertEquals(2, game.piles[1].count());

        assertFalse(game.piles[7].peek().isFaceUp());

        // go ahead and flip card by re-executing move
        game.pileViews[7].getMouseManager().handleMouseEvent(pr);
        assertTrue(game.piles[7].peek().isFaceUp());

        // undo twice.
        assertTrue(game.undoMove());
        assertTrue(game.undoMove());
        assertEquals(1, game.piles[1].count());

    }

    public void testPlaceAceFromWaste() {

        // first create a mouse event
        MouseEvent pr = createPressed(game, game.deckView, 0, 0);
        for (int i = 0; i < 11; i++) {
            game.deckView.getMouseManager().handleMouseEvent(pr);
        }

        // top card is now an ace. Click to place it home
        MouseEvent dbl = createDoubleClicked(game, game.wastePileView, 0, 0);
        game.wastePileView.getMouseManager().handleMouseEvent(dbl);

        assertEquals(1, game.foundation[1].count());

        //undo
        assertTrue(game.undoMove());
        assertEquals(0, game.foundation[1].count());

    }

    public void testPlaceFromBuildablePile() {

        // first create a mouse event
        MouseEvent pr = createPressed(game, game.deckView, 0, 0);
        for (int i = 0; i < 11; i++) {
            game.deckView.getMouseManager().handleMouseEvent(pr);
        }

        // top card is now an ace. Click to place it home
        MouseEvent dbl = createDoubleClicked(game, game.wastePileView, 0, 0);
        game.wastePileView.getMouseManager().handleMouseEvent(dbl);

        assertEquals(1, game.foundation[1].count());

        // Now clear room for the 2 to go down to empty buildablePile 2
        pr = createPressed(game, game.pileViews[2], 0, game.pileViews[2].getSmallOverlap());
        ColumnView cv = game.pileViews[2].getColumnView(pr);
        MoveColumnMove mcm = new MoveColumnMove(game.piles[2], game.piles[7], (Column) cv.getModelElement(), 1);
        mcm.doMove(game);

        assertEquals(1, game.piles[1].count());

        // move column 7 back to 1
        pr = createPressed(game, game.pileViews[7], 0, 6 * game.pileViews[7].getSmallOverlap());
        cv = game.pileViews[7].getColumnView(pr);
        mcm = new MoveColumnMove(game.piles[7], game.piles[1], (Column) cv.getModelElement(), 1);
        mcm.doMove(game);

        assertEquals(3, game.piles[1].count());

        game.getContainer().repaint();

        // flip that final card in 7
        FlipCardMove fcm = new FlipCardMove(game.piles[7]);
        assertTrue(fcm.valid(game));
        assertTrue(fcm.doMove(game));
        game.getContainer().repaint();

        // flip that final card in 2
        fcm = new FlipCardMove(game.piles[2]);
        assertTrue(fcm.valid(game));
        assertTrue(fcm.doMove(game));

        game.getContainer().repaint();

        // move 2 to 7
        // Now clear room for the 2 to go down to empty buildablePile 2
        pr = createPressed(game, game.pileViews[2], 0, 0);
        cv = game.pileViews[2].getColumnView(pr);
        if (cv == null) {
            System.out.println();
        }
        mcm = new MoveColumnMove(game.piles[2], game.piles[7], (Column) cv.getModelElement(), 1);
        mcm.doMove(game);

        game.getContainer().repaint();

        // move 2 diamonds down from waste pile
        Card card = game.wastePile.get();
        MoveWasteToPileMove mwp = new MoveWasteToPileMove(game.wastePile, card, game.piles[2]);
        assertTrue(mwp.doMove(game));

        // now ready to click it to foundation.
        dbl = createDoubleClicked(game, game.pileViews[2], 0, 0);
        game.pileViews[2].getMouseManager().handleMouseEvent(dbl);

        assertEquals(2, game.foundation[1].count());

    }

}
