package heineman;

import heineman.klondike.*;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.games.SolvableSolitaire;
import ks.common.model.*;
import ks.common.view.*;
import ks.launcher.Main;

/**
 * Standard Klondike PlugIn.
 * <p/>
 * Layout: Deal seven packets of cards in a row, all face down.
 * The first consists of one card, the second of two, the third of three,
 * and so on to the seventh of seven. Then turn the top card of each
 * packet face up.
 * <p/>
 * Object: To found the four Aces as and when they appear, and to
 * build each one up in suit and sequence to the King.
 * <p/>
 * Play: Turn cards from the deck and play them if possible or else
 * discard them face up to a single wastepile. Exposed cards on the
 * packets are to be packed in descending sequence and alternating color,
 * with their constituent cards spread towards you in columns. A card
 * or properly packed sequence of cards may be transferred from one
 * column to another provided that the join follows the
 * descending-alternating rule. Whenever a down-card is exposed, turn it
 * face up. Whenever a space is made by clearing out a column, fill it
 * only with a King (and any other cards that may be packed upon it in
 * proper sequence). Multiple cards can be moved at once. There is no
 * redeal.
 * <p/>
 * Note: In traditional "solitaire" as we know it in America, three
 * cards are dealt at a time, and any card can be placed on an empty
 * column; so pay attention to the rules above:
 * <p/>
 * Note: As part of the implementation of Klondike, all controllers ensure
 * that a ColumnView object is being dragged; this simplifies some logic,
 * but adds some complexity to the WastePile controller, who manages the
 * PileView object; see WastePileController for details.
 * <p/>
 * Creation date: (11/9/01 9:44:37 PM)
 *
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class Klondike extends Solitaire implements SolvableSolitaire {

    /**
     * Each Game has a Deck.
     */
    protected Deck deck;

    /**
     * Each Game may have cards dragged from BuildablePiles.
     */
    protected BuildablePileView fromPile;

    /**
     * Each Game may have cards dragged from the WastePile.
     */
    protected PileView fromWaste;

    /**
     * And seven BuildablePiles. Extra size is to enable easier algorithms.
     */
    protected BuildablePile piles[] = new BuildablePile[8];

    /**
     * And four Piles as foundation. Extra size is to enable easier algorithms.
     */
    protected Pile foundation[] = new Pile[5];

    /**
     * Latest version.
     */
    protected String version = "1.1";

    /**
     * And a waste pile.
     */
    protected Pile wastePile;

    /**
     * The view of the deck
     */
    protected DeckView deckView;

    /**
     * The columns
     */
    protected BuildablePileView pileViews[] = new BuildablePileView[8];

    /**
     * The home locations
     */
    protected PileView foundationViews[] = new PileView[8];

    /**
     * The display for the score.
     */
    protected IntegerView scoreView;

    /**
     * The display for the wastePile.
     */
    protected PileView wastePileView;

    /**
     * View for the number of cards left in the deck.
     */
    protected IntegerView numLeftView;

    /**
     * Klondike constructor comment.
     */
    public Klondike() {
        super();
    }

    /**
     * Return list of available moves.
     * <p/>
     *
     * @return java.util.Enumeration
     */
    public java.util.Enumeration<Move> availableMoves() {
        java.util.Vector<Move> v = new java.util.Vector<>();

        // 1. try to move waste Pile to foundation
        if (!wastePile.empty()) {
            for (int f = 1; f <= 4; f++) {
                Move m = new MoveWasteToFoundationMove(wastePile, null, foundation[f]);
                if (m.valid(this)) {
                    v.addElement(m);
                    break;
                }
            }
        }

        // 2. try to flip cards in BuildablePiles
        for (int c = 1; c <= 7; c++) {
            // nothing to be flipped
            if (piles[c].empty())
                continue;

            if (piles[c].getNumFaceUp() == 0) {
                Move m = new FlipCardMove(piles[c]);
                if (m.valid(this))
                    v.addElement(m);
            }
        }

        // 3. try to move cards from BuildablePiles up to the foundation.
        for (int c = 1; c <= 7; c++) {
            // nothing to be removed
            if (piles[c].empty())
                continue;

            // See if we can move this one card.
            for (int f = 1; f <= 4; f++) {

                Move m = new MoveCardToFoundationMove(piles[c], null, foundation[f]);
                if (m.valid(this)) {
                    v.addElement(m);
                }
            }
        }

        // 4. Try to move columns (of any size starting with largest in pile) from right-most
        //    buildablepile and moving them to the left-most pile available. make two types
        //    of moves for forward progress: (1) you can move all faceup cards AND there is
        //    a facedown card underneath; and (2) you can move all faceup cards to a non-empty
        //    column, thus opening up a column
        for (int c = 7; c >= 1; c--) {
            // nothing to be removed
            if (piles[c].empty())
                continue;

            int numFaceUp = piles[c].getNumFaceUp();
            if (numFaceUp == 0) continue;                 // all facedown? nothing to move.

            for (int c2 = 1; c2 <= 7; c2++) {
                if (c == c2) continue;  // no self-moves allowed

                if (numFaceUp == piles[c].count()) {
                    if (piles[c2].empty()) continue;   // nothing to reveal? no progress...
                }

                Move m = new MoveColumnMove(piles[c], piles[c2], null, numFaceUp);
                if (m.valid(this)) {
                    v.addElement(m);
                    break;   // once a move is established, that's it for this column
                }
            }
        }

        // 5. Try to move waste down to an available pile (left to right)
        if (!wastePile.empty()) {
            for (int c = 1; c <= 7; c++) {
                Move m = new MoveWasteToPileMove(wastePile, null, piles[c]);
                if (m.valid(this)) {
                    v.addElement(m);
                }
            }
        }

        // 6. Finally, deal card
        if (!deck.empty()) {
            Move m = new DealCardMove(deck, wastePile);
            if (m.valid(this)) {
                v.addElement(m);
            }
        }

        return v.elements();
    }

    /**
     * getName method comment.
     */
    public String getName() {
        return "Klondike";
    }

    /**
     * getName method comment.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Return whether the game has been won.
     * <p/>
     * In Klondike, the game is won once the score reaches 52, which means that
     * all thirteen cards of each of the four suits has made it to the foundation.
     *
     * @return boolean
     */
    public boolean hasWon() {
        return getScoreValue() == 52;
    }

    /**
     * initialize method comment.
     */
    public void initialize() {
        // initialize model
        initializeModel(getSeed());
        initializeView();
        initializeControllers();

        // prepare game by dealing facedown cards to all columns, then one face up
        for (int pileNum = 1; pileNum <= 7; pileNum++) {
            for (int num = 2; num <= pileNum; num++) {
                Card c = deck.get();

                c.setFaceUp(false);
                piles[pileNum].add(c);
            }

            // This one is face up.
            piles[pileNum].add(deck.get());
        }

        updateNumberCardsLeft(-28);
    }

    /**
     * initialize controllers comment.
     */
    protected void initializeControllers() {
        // Initialize Controllers for DeckView
        deckView.setMouseAdapter(new KlondikeDeckController(this, deck, wastePile));
        deckView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        deckView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Now for each BuildablePile.
        for (int i = 1; i <= 7; i++) {
            pileViews[i].setMouseAdapter(new BuildablePileController(this, pileViews[i]));
            pileViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
            pileViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
        }

        // Now for each Foundation.
        for (int i = 1; i <= 4; i++) {
            foundationViews[i].setMouseAdapter(new FoundationController(this, foundationViews[i]));
            foundationViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
            foundationViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
        }

        // WastePile
        wastePileView.setMouseAdapter(new WastePileController(this, wastePileView));
        wastePileView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        wastePileView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Ensure that any releases (and movement) are handled by the non-interactive widgets
        numLeftView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        numLeftView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        numLeftView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // same for scoreView
        scoreView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        scoreView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        scoreView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Finally, cover the Container for any events not handled by a widget:
        getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        getContainer().setMouseAdapter(new SolitaireReleasedAdapter(this));
        getContainer().setUndoAdapter(new SolitaireUndoAdapter(this));

    }

    /**
     * initialize method comment.
     */
    protected void initializeModel(int seed) {
        deck = new Deck("d");
        deck.create(seed);
        model.addElement(deck);   // add to our model (as defined within our superclass).

        // each of the columns appears here
        for (int i = 1; i <= 7; i++) {
            piles[i] = new BuildablePile("pile" + i);
            model.addElement(piles[i]);
        }

        // develop foundations
        for (int i = 1; i <= 4; i++) {
            foundation[i] = new Pile("foundation" + i);
            model.addElement(foundation[i]);
        }

        wastePile = new Pile("waste");
        model.addElement(wastePile);

        // initial score is set to ZERO (every Solitaire game by default has a score) and there are 52 cards left.
        // NOTE: These will be added to the model by solitaire Base Class.
        this.updateNumberCardsLeft(52);

        // Lastly, as part of the mode, we will eventually provide a way to register the
        // type of allowed moves. This feature will be necessary for SolitaireSolvers
    }

    /**
     * initialize view comment.
     */
    protected void initializeView() {
        CardImages ci = getCardImages();

        deckView = new DeckView(deck);
        deckView.setBounds(20, 30, ci.getWidth(), ci.getHeight());
        container.addWidget(deckView);

        // create BuildablePileViews, one after the other (default to 13 full cards -- more than we'll need)
        for (int pileNum = 1; pileNum <= 7; pileNum++) {
            pileViews[pileNum] = new BuildablePileView(piles[pileNum]);
            pileViews[pileNum].setBounds(20 * pileNum + (pileNum - 1) * ci.getWidth(), ci.getHeight() + 50, ci.getWidth(), 13 * ci.getHeight());
            container.addWidget(pileViews[pileNum]);
        }

        // create PileViews, one after the other.
        for (int pileNum = 1; pileNum <= 4; pileNum++) {
            foundationViews[pileNum] = new PileView(foundation[pileNum]);
            foundationViews[pileNum].setBounds(20 * (pileNum + 3) + ci.getWidth() * (pileNum + 2), 30, ci.getWidth(), ci.getHeight());
            container.addWidget(foundationViews[pileNum]);
        }

        wastePileView = new PileView(wastePile);
        wastePileView.setBounds(20 * 2 + ci.getWidth(), 30, ci.getWidth(), ci.getHeight());
        container.addWidget(wastePileView);

        scoreView = new IntegerView(getScore());
        scoreView.setBounds(20 * 7 + 7 * ci.getWidth(), 30, 160, 60);
        container.addWidget(scoreView);

        numLeftView = new IntegerView(getNumLeft());
        numLeftView.setFontSize(14);
        numLeftView.setBounds(20 + ci.getWidth() / 4, 10, ci.getWidth(), 20);
        container.addWidget(numLeftView);
    }

    /**
     * Code to launch solitaire variation.
     */
    public static void main(String[] args) {
        // Seed is to ensure we get the same initial cards every time.
        // Here the seed is to "order by suit."
        Main.generateWindow(new Klondike(), Deck.OrderBySuit);
    }
}
