package vfinal;

import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

public class DealFourMove extends Move {
    /** The deck. */
    protected Deck deck;

    /** The piles. */
    protected Pile pile1;
    protected Pile pile2;
    protected Pile pile3;
    protected Pile pile4;
    
    /**
     * Class Representing the action of dealing four cards to each Pile.
     */
    public DealFourMove(Deck d, Pile p1, Pile p2, Pile p3, Pile p4) {
        super();

        /** Store all parameters with the Move Object. */
        this.deck = d;
        this.pile1 = p1;
        this.pile2 = p2;
        this.pile3 = p3;
        this.pile4 = p4;
    }
    
    /**
     * Each move should knows how to execute itself.
     * <p>
     * Note: this interface wasn't in place in time for the course, so this
     * method returns false, and can be overridden by the necessary subclasses.
     * <p>
     * @param theGame    the game being played.
     * @return boolean
     */
    public boolean doMove(Solitaire theGame) {
        // VALIDATE:
        if (valid(theGame) == false)
            return false;

        // EXECUTE:
        pile1.add(deck.get());
        pile2.add(deck.get());
        pile3.add(deck.get());
        pile4.add(deck.get());

        // update count in deck.
        theGame.updateNumberCardsLeft(-4);
        return true;
    }
    
    /**
     * To undo this move, we move the cards from the piles back into the Deck
     * in the order they were played.
     * @param theGame 
     */
    public boolean undo(Solitaire theGame) {
        // VALIDATE:
        if (pile1.empty() || pile2.empty() || pile3.empty() || pile4.empty())
            return false;

        // UNDO:
        // carefully reverse order of operations (changes to models will cause views to be marked as dirty)
        deck.add(pile4.get());
        deck.add(pile3.get());
        deck.add(pile2.get());
        deck.add(pile1.get());

        // update count in deck.
        theGame.updateNumberCardsLeft(+4);
        return true;
    }
    
    /**
     * Validate DealFourMove.
     * @param theGame Solitaire 
     */
    public boolean valid(Solitaire theGame) {
        boolean validation = false;

        // VALIDATE:
        //    dealFour(d) : not d.empty()
        if (!deck.empty()) {
            validation = true;
        }

        return validation;
    }
}

