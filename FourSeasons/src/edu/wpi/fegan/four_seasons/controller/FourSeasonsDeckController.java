package edu.wpi.fegan.four_seasons.controller;

import edu.wpi.fegan.four_seasons.FourSeasons;
import heineman.klondike.DealCardMove;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * Created by frankegan on 4/2/16.
 */
public class FourSeasonsDeckController extends SolitaireReleasedAdapter {
    /**
     * The game.
     */
    protected FourSeasons theGame;

    /**
     * The WastePile of interest.
     */
    protected Pile wastePile;

    /**
     * The Deck of interest.
     */
    protected Deck deck;

    /**
     * KlondikeDeckController constructor comment.
     */
    public FourSeasonsDeckController(FourSeasons theGame, Deck d, Pile wastePile) {
        super(theGame);

        this.theGame = theGame;
        this.wastePile = wastePile;
        this.deck = d;
    }

    /**
     * Coordinate reaction to the beginning of a Drag Event. In this case,
     * no drag is ever achieved, and we simply deal upon the press.
     */
    public void mousePressed(java.awt.event.MouseEvent me) {

        // Attempting a DealFourCardMove
        Move m = new DealCardMove(deck, wastePile);
        if (m.doMove(theGame)) {
            theGame.pushMove(m);     // Successful DealFour Move
            theGame.refreshWidgets(); // refresh updated widgets.
        }
    }

}
