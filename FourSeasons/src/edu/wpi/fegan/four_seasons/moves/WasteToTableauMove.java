package edu.wpi.fegan.four_seasons.moves;

import edu.wpi.fegan.four_seasons.FourSeasons;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Created by frankegan on 4/1/16.
 */
public class WasteToTableauMove extends Move {
    /** The waste pile. */
    protected Pile waste;

    /** The Destination column. */
    protected Pile to;

    /** The card being dragged (if the move has already been made). */
    protected Card cardBeingDragged;

    public WasteToTableauMove(Pile waste, Card cardBeingDragged, Pile to) {
        this.waste = waste;
        this.to = to;
        this.cardBeingDragged = cardBeingDragged;
    }

    @Override
    public boolean doMove(Solitaire game) {
        // VALIDATE:
        if (!valid(game))
            return false;

        // EXECUTE:
        // move card from waste to to Pile.
        if (cardBeingDragged == null)
            to.add(waste.get());
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
        waste.add (to.get());
        return true;
    }

    @Override
    public boolean valid(Solitaire game) {
        //VALIDATION:
        boolean validation = false;

        //moveWasteToPile (Waste from, BuildablePile to) : to.empty()
        if (to.empty())
            validation = true;

        if (cardBeingDragged == null) {
            //moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and waste.rank() == to.rank() - 1 and to.peek().faceUp()
            if (!to.empty()
                    && (waste.rank() == (to.rank() - 1)))
                validation = true;

        } else if (to.empty()) {//building new season TODO no idea if this is right
            if (cardBeingDragged.getRank() == ((FourSeasons) game).getSeasonRank())
                validation = true;
        } else {
            //moveColumnBetweenPiles(Waste from,BuildablePile to) : not to.empty() and cardBeingDragged.rank() == to.rank() - 1 and to.peek().faceUp()
            if (!to.empty()
                    && (cardBeingDragged.getRank() == (to.rank() - 1))
                    || ((to.rank() == 13) && cardBeingDragged.getRank() == 1))
                validation = true;
        }
        return validation;
    }
}
