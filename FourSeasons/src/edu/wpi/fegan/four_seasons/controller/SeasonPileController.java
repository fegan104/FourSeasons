package edu.wpi.fegan.four_seasons.controller;

import edu.wpi.fegan.four_seasons.FourSeasons;
import edu.wpi.fegan.four_seasons.moves.ToSeasonMove;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

import java.awt.event.MouseEvent;

/**
 * Created by frankegan on 4/2/16.
 */
public class SeasonPileController extends java.awt.event.MouseAdapter{
    /**
     * The game that we are partly controlling.
     */
    protected FourSeasons theGame;
    /**
     * The src PileView that initiated the event.
     */
    protected PileView src;

    /**
     * PileController constructor comment.
     */
    public SeasonPileController(FourSeasons theGame, PileView pv) {
        super();

        this.theGame = theGame;
        this.src = pv;
    }
    /**
     * Coordinate reaction to the beginning of a Drag Event.
     * <p/>
     * Note: There is no way to differentiate between a press that
     * will become part of a double click vs. a click that will
     * be held and dragged. Only mouseReleased will be able to
     * help us out with that one.
     * <p/>
     * Creation date: (10/4/01 6:05:55 PM)
     *
     * @param me java.awt.event.MouseEvent
     */
    //TODO added clickability
//    public void mousePressed(MouseEvent me) {
//
//        // The container manages several critical pieces of information; namely, it
//        // is responsible for the draggingObject; in our case, this would be a CardView
//        // Widget managing the card we are trying to drag between two piles.
//        Container c = theGame.getContainer();
//
//        /** Return if there is no card to be chosen. */
//        Pile theP = (Pile) src.getModelElement();
//        if (theP.count() == 0) {
//            return;
//        }
//
//        // Get a column of cards to move from the PileView
//        // Note that this method will alter the model for PileView if the condition is met.
//        CardView topCard = src.getCardViewForTopCard(me);
//
//        // an invalid selection (either all facedown, or not in faceup region)
//        if (topCard == null) {
//            return;
//        }
//
//        // If we get here, then the user has indeed clicked on the top card in the PileView and
//        // we are able to now move it on the screen at will. For smooth action, the bounds for the
//        // cardView widget reflect the original card location on the screen.
//        Widget w = c.getActiveDraggingObject();
//        if (w != Container.getNothingBeingDragged()) {
//            System.err.println("PileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
//            return;
//        }
//
//        // Tell container which object is being dragged, and where in that widget the user clicked.
//        c.setActiveDraggingObject(topCard, me);
//
//        // Tell container which PileView is the source for this drag event.
//        c.setDragSource(src);
//
//        // we simply redraw our source pile to avoid flicker,
//        // rather than refreshing all widgets...
//        src.redraw();
//    }

    /**
     * Coordinate reaction to the completion of a Drag Event.
     * <p/>
     * A bit of a challenge to construct the appropriate move, because cards
     * can be dragged both from the WastePile (as a CardView widget) and the
     * PileView (as a ColumnView widget).
     * <p/>
     *
     * @param me java.awt.event.MouseEvent
     */
    public void mouseReleased(MouseEvent me) {
        Container c = theGame.getContainer();

        /** Return if there is no card being dragged chosen. */
        Widget w = c.getActiveDraggingObject();
        if (w == Container.getNothingBeingDragged()) {
            c.releaseDraggingObject();
            return;
        }

        /** Recover the from Pile OR waste Pile */
        Widget fromWidget = c.getDragSource();
        if (fromWidget == null) {
            System.err.println("PileController::mouseReleased(): somehow no dragSource in container.");
            c.releaseDraggingObject();
            return;
        }

        // Determine the To Pile
        Pile toPile = (Pile) src.getModelElement();
        CardView cardView = (CardView) w;
        Card theCard = (Card) cardView.getModelElement();
        if (theCard == null) {
            System.err.println("BuildablePileController::mouseReleased(): somehow CardView model element is null.");
            return;
        }

        //Card from waste pile or Tableau released on Season
        Pile wastePile = (Pile) fromWidget.getModelElement();
        Move m = new ToSeasonMove(wastePile, theCard, toPile);
        if (m.doMove(theGame)) {
            // Successful move! add move to our set of moves
            theGame.pushMove(m);
        } else {
            // Invalid move. Restore to original waste pile. NO MOVE MADE
            wastePile.add(theCard);
        }

        //Which move should we use?TODO reduce move types so this logic isn't necessary
//        if (fromWidget instanceof TableauPileView) {
//            //Tableau to tableau move
//            Pile tableauPile = (Pile) fromWidget.getModelElement();
//            Move m = new TableauToSeasonMove(tableauPile, theCard, toPile);
//            if (m.doMove(theGame)) {
//                // Successful move! add move to our set of moves
//                theGame.pushMove(m);
//            } else {
//                // Invalid move. Restore to original waste pile. NO MOVE MADE
//                tableauPile.add(theCard);
//            }
//        } else if (fromWidget instanceof WastePileView) {
//            //Card from waste pile released on Tableau
//            Pile wastePile = (Pile) fromWidget.getModelElement();
//            Move m = new WasteToSeasonMove(wastePile, theCard, toPile);
//            if (m.doMove(theGame)) {
//                // Successful move! add move to our set of moves
//                theGame.pushMove(m);
//            } else {
//                // Invalid move. Restore to original waste pile. NO MOVE MADE
//                wastePile.add(theCard);
//            }
//        } else {
//            System.err.println("Released an impossible view onto Tableau");
//        }

        // release the dragging object, (container will reset dragSource)
        c.releaseDraggingObject();
        c.repaint();
    }
}
