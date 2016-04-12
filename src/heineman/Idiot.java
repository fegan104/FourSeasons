package heineman;


import java.awt.Dimension;

import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.launcher.Main;
import heineman.idiot.*;

/**
 * The Idiot Plug-in
 *
 * Creation date: (9/30/01 9:22:27 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */

 /**
 Game Type: Idiot
 Base Type: --
 Model{
   d as Deck;
   col1, col2, col3, col4 as Column;
   score as Integer;
 }

 Actions {
   removeCard  map DoubleClick from:Column
   dealFour    map Click       d:Deck
   moveCard    map Drag Card   from:Column to:Column
 }

 Validation {
   dealFour(d) : not d.empty()
   moveCard (from,to) : to.empty() and not from.empty()
   removeCard (from) : not from.empty() and 
					   EXISTS Column c1 suchthat (from != c1) and (c1.suit() == from.suit()) and (c1.rank() > from.rank())
 }

 Description {
   dealFour (d) : col1.add (d.get()); col2.add(d.get()); col3.add(d.get()); col4.add (d.get());
   removeCard (from) : from.get(); score++;
   moveCard (from, to) : to.add (from.get());
 }

 Prepare {
   d.create (seed);
   d.dealFour();
 }
 
 **/
 
 
public class Idiot extends Solitaire {

	/** Each Game has a Deck. */
	protected Deck deck;

	/** And four Columns (vertically placed) */
	protected Column col1, col2, col3, col4;

	/** The view of the deck */
	protected DeckView deckView;

	/** The columns */
	protected ColumnView colView1;
	protected ColumnView colView2;
	protected ColumnView colView3;
	protected ColumnView colView4;

	/** The display for the score. */
	protected IntegerView scoreView;
	
	/** View for the number of cards left in the deck. */
	protected IntegerView numLeftView;

	
/**
 * Idiot constructor comment.
 */
public Idiot() {
	super();
}

/**
 * Return the name of this game.
 * @return java.lang.String
 */
public String getName() {
	return "Idiot";
}
/**
 * Return the version of this game.
 * @return java.lang.String
 */
public String getVersion () {
	return "1.1";
}
/**
 * Return whether the game has been won.
 * @return boolean
 */
public boolean hasWon() {
	return getScoreValue() == 48;
}
/**
 * Initialize the game and prepare for playing.
 * <p>
 * Creation date: (9/30/01 10:57:00 PM)
 */
public void initialize() {

	// Initialize model, view, and controllers.
	initializeModel(getSeed());
	initializeView();
	initializeController();

	// Prepare game AFTER all controllers are set up.
	DealFourMove dfm = new DealFourMove (deck, col1, col2, col3, col4); 
	if (! dfm.doMove(this)) {
		System.err.println ("Idiot::initialize(). Unable to deal initial hand.");
	}
}
/**
 * Prepare the controllers.
 *
 * Don't forget standard Undo adapters and standard Drag adapters.
 *
 * Creation date: (10/3/01 9:35:45 PM)
 */
private void initializeController() {
	// Initialize Controllers for DeckView
	deckView.setMouseAdapter(new IdiotDeckAdapter (this, deckView)); 

	/** For quick reference to these ColumnViews. */
	ColumnView colViews[] = { colView1, colView2, colView3, colView4 };

	// Now for each column.
	for (int i = 0; i < colViews.length; i++) {
		colViews[i].setMouseAdapter (new IdiotColumnAdapter (this, colViews[i]));
	}

	// Ensure that any releases (and movement) are handled by the non-interactive widgets
	numLeftView.setMouseAdapter (new SolitaireReleasedAdapter(this));
	scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
}

/**
 * Initialize the Model for Idiot.
 *
 * Don't forget to set the standard Score and numberCardsLeft model elements.
 *
 * Creation date: (10/3/01 9:32:51 PM)
 * @param seed int
 */
private void initializeModel(int seed) {
	deck = new Deck("d");
	deck.create(seed);
	model.addElement (deck);   // add to our model (as defined within our superclass).
	
	col1 = new Column("col1");
	col2 = new Column("col2");
	col3 = new Column("col3");
	col4 = new Column("col4");
	model.addElement (col1);
	model.addElement (col2);
	model.addElement (col3);
	model.addElement (col4);		
	
	// initial score is set to ZERO (every Solitaire game by default has a score) and there are 52 cards left.
	// NOTE: These will be added to the model by solitaire Base Class.
	this.updateScore(0);
	this.updateNumberCardsLeft(52);

	// Lastly, as part of the model, we will eventually provide a way to register the 
	// type of allowed moves. This feature will be necessary for SolitaireSolvers
}
/**
 * Prepare the view.
 * Creation date: (10/3/01 9:34:34 PM)
 */
private void initializeView() {

	CardImages ci = getCardImages();

	// initial height for columnViews. This can grow as needed
	int defaultColumnHeight = 12 * ci.getOverlap() + ci.getHeight();

	deckView = new DeckView (deck);
	deckView.setBounds (20,20, ci.getWidth(), ci.getHeight());
	addViewWidget (deckView);
	
	colView1 = new ColumnView (col1);
	colView1.setBounds (40 + ci.getWidth(), 20, ci.getWidth(), defaultColumnHeight);
	addViewWidget (colView1);

	colView2 = new ColumnView (col2);
	colView2.setBounds (60 + 2*ci.getWidth(), 20, ci.getWidth(), defaultColumnHeight);
	addViewWidget (colView2);
		
	colView3 = new ColumnView (col3);
	colView3.setBounds (80 + 3*ci.getWidth(), 20, ci.getWidth(), defaultColumnHeight);
	addViewWidget (colView3);

	colView4 = new ColumnView (col4);
	colView4.setBounds (100 + 4*ci.getWidth(), 20, ci.getWidth(), defaultColumnHeight);
	addViewWidget (colView4);

	scoreView = new IntegerView (getScore());
	scoreView.setBounds (100 + 5*ci.getWidth(), 20, 160, 60);
	addViewWidget (scoreView);

	numLeftView = new IntegerView (getNumLeft());
	numLeftView.setBounds (20, 20 + ci.getHeight() + 40, ci.getWidth(), 60);
	addViewWidget (numLeftView);

}

/** Code to launch solitaire variation. */
public static void main (String []args) {
	// Seed is to ensure we get the same initial cards every time.
	// Here the seed is to "order by suit."
	GameWindow gw = Main.generateWindow(new Idiot(), Deck.OrderBySuit);
	gw.setVisible(true);
}

}
