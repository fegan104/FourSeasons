package edu.wpi.fegan.four_seasons.moves;

import edu.wpi.fegan.four_seasons.FourSeasons;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Created by frankegan on 4/1/16.
 */
public class WasteToSeasonMove extends Move {
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
    protected Card theCard;

    public WasteToSeasonMove(Pile from, Card theCard, Pile to) {
        this.from = from;
        this.to = to;
        this.theCard = theCard;
    }

    @Override
    public boolean doMove(Solitaire game) {
        // VALIDATE:
        if (!valid(game))
            return false;

        // EXECUTE:
        // move card from waste to to Pile.
        if (theCard == null)
            to.add(from.get());
        else
            to.add(theCard);

        // advance score
        game.updateScore(1);
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
    public boolean valid(Solitaire game) {
        //VALIDATION:
        boolean validation = false;

        if (!to.empty() && to.peek().getRank() == (((FourSeasons) game).getSeasonRank() - 1))
            return false; //stops a Season form growing too big

        if (theCard == null) {
            System.out.println("Card Being dragged was null");
            if (!to.empty() && (from.rank() == (to.rank() + 1)))
                validation = true;

        } else if (to.empty()) {//building new season
            if (theCard.getRank() == ((FourSeasons) game).getSeasonRank()) {
                System.out.println("");
                validation = true;
            }
        } else {
            if (!to.empty() && (theCard.getRank() == (to.rank() + 1))
                    || ((to.rank() == 13) && theCard.getRank() == 1))
                validation = true;
        }
        return validation;
    }
}
