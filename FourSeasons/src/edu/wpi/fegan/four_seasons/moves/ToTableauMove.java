package edu.wpi.fegan.four_seasons.moves;

import edu.wpi.fegan.four_seasons.FourSeasons;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Created by frankegan on 4/12/16.
 */
public class ToTableauMove extends Move {
    /**
     * The waste pile.
     */
    protected Pile from;

    /**
     * The Destination column.
     */
    protected Pile to;

    /**
     * The card being dragged (if the move has already been made).
     */
    protected Card cardBeingDragged;

    public ToTableauMove(Pile from, Card cardBeingDragged, Pile to) {
        this.from = from;
        this.to = to;
        this.cardBeingDragged = cardBeingDragged;
    }

    @Override
    public boolean doMove(Solitaire theGame) {
        // VALIDATE:
        if (!valid(theGame))
            return false;

        // EXECUTE:
        // move card from waste to to Pile.
        if (cardBeingDragged == null)
            to.add(from.get());
        else
            to.add(cardBeingDragged);

        return true;
    }

    @Override
    public boolean undo(Solitaire game) {
        // VALIDATE:
        if (to.empty()) return false;

        // EXECUTE:
        // remove card and move to from pile.
        from.add(to.get());
        return true;
    }

    @Override
    public boolean valid(Solitaire theGame) {
        //VALIDATION:
        boolean validation = false;

        //moveWasteToPile (Waste from, BuildablePile to) : to.empty()
        if (to.empty())
            validation = true;

        if (cardBeingDragged == null) {
            //moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and waste.rank() == to.rank() - 1 and to.peek().faceUp()
            if (!to.empty()
                    && (from.rank() == (to.rank() - 1)))
                validation = true;

        } else if (to.empty()) {//building new season TODO no idea if this is right
            if (cardBeingDragged.getRank() == ((FourSeasons) theGame).getSeasonRank())
                validation = true;
        } else {
            //moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and cardBeingDragged.rank() == to.rank() - 1 and to.peek().faceUp()
            if (!to.empty() && (cardBeingDragged.getRank() == (to.rank() - 1))
                    || ((to.rank() == Card.ACE) && cardBeingDragged.getRank() == Card.KING))//round the corner
                validation = true;
        }
        return validation;
    }
}
