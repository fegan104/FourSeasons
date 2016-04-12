package edu.wpi.fegan.four_seasons;

import edu.wpi.fegan.four_seasons.controller.FourSeasonsDeckController;
import edu.wpi.fegan.four_seasons.controller.SeasonPileController;
import edu.wpi.fegan.four_seasons.controller.TableauPileController;
import edu.wpi.fegan.four_seasons.controller.WastePileController;
import edu.wpi.fegan.four_seasons.view.TableauPileView;
import edu.wpi.fegan.four_seasons.view.WastePileView;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.*;

import javax.swing.*;

/**
 * Created by frankegan on 3/30/16.
 */
public class FourSeasons extends Solitaire {
    //TODO implement SolvableSolitaire
    public static final int TABLEAU_SIZE = 5;
    public static final int SEASON_SIZE = 4;
    private static int SEASON_RANK;
    /**
     * Each Game has a Deck.
     */
    protected Deck stock;
    /**
     * The view of the deck
     */
    protected DeckView stockView;
    /**
     * And a waste pile.
     */
    protected Pile wastePile;
    /**
     * Each Game may have cards dragged from the WastePile.
     */
    protected WastePileView wastePileView;
    /**
     * And the tableau
     */
    protected Pile tableau[] = new Pile[TABLEAU_SIZE];
    /**
     * The views for the tableau
     */
    protected TableauPileView tableauViews[] = new TableauPileView[TABLEAU_SIZE];
    /**
     * And the seasons
     */
    protected Pile seasons[] = new Pile[SEASON_SIZE];
    /**
     * The views for the seasons
     */
    protected PileView seasonViews[] = new PileView[SEASON_SIZE];
    /**
     * The display for the score.
     */
    protected IntegerView scoreView;
    /**
     * The display for the stock left.
     */
    protected IntegerView stockRemView;
    /**
     * FourSeasons constructor just calls super.TODO necessary?
     */
    public FourSeasons() {
        super();
    }

    @Override
    public String getName() {
        return "Four Seasons";
    }

    @Override
    public boolean hasWon() {
        return getScore().getValue() == 52;
    }

    @Override
    public void initialize() {
        // initialize model
        initializeModel(getSeed());
        initializeView();
        initializeControllers();

        // prepare game by dealing facedown cards to tableau, then one season
        for (int pileNum = 0; pileNum < TABLEAU_SIZE; pileNum++)
            tableau[pileNum].add(stock.get());

        //game starts with one season
        Card firstSeason = stock.get();
        seasons[0].add(firstSeason);
        SEASON_RANK = firstSeason.getRank();
        updateScore(1);

        updateNumberCardsLeft(-5);
    }

    private void initializeControllers() {
        // Initialize Controllers for DeckView
        stockView.setMouseAdapter(new FourSeasonsDeckController(this, stock, wastePile));
        stockView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        stockView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Now for each Tableau.
        for (int i = 0; i < TABLEAU_SIZE; i++) {
            tableauViews[i].setMouseAdapter(new TableauPileController(this, tableauViews[i]));
            tableauViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
            tableauViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
        }

        // Now for each Season.
        for (int i = 0; i < SEASON_SIZE; i++) {
            seasonViews[i].setMouseAdapter(new SeasonPileController(this, seasonViews[i]));
            seasonViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
            seasonViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
        }

        // WastePile
        wastePileView.setMouseAdapter(new WastePileController(this, wastePileView));
        wastePileView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        wastePileView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Ensure that any releases (and movement) are handled by the non-interactive widgets
        stockRemView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        stockRemView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        stockRemView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // same for scoreView
        scoreView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        scoreView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        scoreView.setUndoAdapter(new SolitaireUndoAdapter(this));

        // Finally, cover the Container for any events not handled by a widget:
        getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        getContainer().setMouseAdapter(new SolitaireReleasedAdapter(this));
        getContainer().setUndoAdapter(new SolitaireUndoAdapter(this));
    }

    private void initializeView() {
        CardImages ci = getCardImages();
        final int W = ci.getWidth();
        final int H = ci.getHeight();

        stockView = new DeckView(stock);
        stockView.setBounds(20, 20, W, H);
        container.addWidget(stockView);

        // create The tableau adding one PileView at a time
        for (int pileNum = 0; pileNum < TABLEAU_SIZE; pileNum++) {
            tableauViews[pileNum] = new TableauPileView(tableau[pileNum]);
        }

        tableauViews[0].setBounds(3 * W + 100, 20, W, H);
        tableauViews[1].setBounds(2 * W + 80, H + 40, W, H);
        tableauViews[2].setBounds(3 * W + 100, H + 40, W, H);
        tableauViews[3].setBounds(4 * W + 120, H + 40, W, H);
        tableauViews[4].setBounds(3 * W + 100, 2 * H + 60, W, H);

        for (int pileNum = 0; pileNum < TABLEAU_SIZE; pileNum++) {
            container.addWidget(tableauViews[pileNum]);
        }

        // create Seasons, one after the other.
        for (int pileNum = 0; pileNum < SEASON_SIZE; pileNum++) {
            seasonViews[pileNum] = new PileView(seasons[pileNum]);
        }

        seasonViews[0].setBounds(5 * W + 160, 20, W, H);
        seasonViews[1].setBounds(6 * W + 180, 20, W, H);
        seasonViews[2].setBounds(5 * W + 160, 40 + H, W, H);
        seasonViews[3].setBounds(6 * W + 180, 40 + H, W, H);

        for (int pileNum = 0; pileNum < SEASON_SIZE; pileNum++) {
            container.addWidget(seasonViews[pileNum]);
        }

        //Add waste pile
        wastePileView = new WastePileView(wastePile);
        wastePileView.setBounds(20 * 2 + W, 20, W, H);
        container.addWidget(wastePileView);

        //add score view with label
        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setBounds(5 * W + 160, 2 * H + 80, W, 20);
        scoreView = new IntegerView(getScore());
        scoreView.setFontSize(14);
        scoreView.setBounds(scoreLabel.getWidth() + scoreLabel.getX(), scoreLabel.getY(), W, 20);
        container.add(scoreLabel);
        container.addWidget(scoreView);

        //add stock remaining counter view with label
        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setBounds(5 * W + 160, 2 * H + 100, W, 20);
        stockRemView = new IntegerView(getNumLeft());
        stockRemView.setFontSize(14);
        stockRemView.setBounds(stockLabel.getWidth() + stockLabel.getX(), stockLabel.getY(), W, 20);
        container.add(stockLabel);
        container.addWidget(stockRemView);
    }

    private void initializeModel(int seed) {
        stock = new Deck("d");
        stock.create(seed);
        model.addElement(stock);   // add to our model (as defined within our superclass).

        // tableau appears here
        for (int i = 0; i < TABLEAU_SIZE; i++) {
            tableau[i] = new Pile("tableau" + i);
            model.addElement(tableau[i]);
        }

        // develop seasons
        for (int i = 0; i < SEASON_SIZE; i++) {
            seasons[i] = new Pile("season" + i);
            model.addElement(seasons[i]);
        }

        wastePile = new Pile("waste");
        model.addElement(wastePile);

        // initial score is set to ZERO (every Solitaire game by default has a score) and there are 52 cards left.
        // NOTE: These will be added to the model by solitaire Base Class.
        this.updateNumberCardsLeft(52);
    }

    public int getSeasonRank(){
        return SEASON_RANK;
    }
    /**
     * Code to launch solitaire variation.
     */
    public static void main(String[] args) {
        // Seed is to ensure we get the same initial cards every time.
        // Here the seed is to "order by suit."
        ks.launcher.Main.generateWindow(new FourSeasons(), Deck.OrderBySuit);
    }
}
