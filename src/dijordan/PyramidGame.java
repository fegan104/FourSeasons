package dijordan;

import ks.client.gamefactory.GameWindow;
import ks.common.model.*;
import ks.common.view.*;
import ks.common.games.*;
import ks.launcher.Main;
import dijordan.model.*;
import dijordan.view.*;

/******************************************
 * The "main" bit of the game of Pyramid
 * @author: Diane Jordan (dijordan@wpi.edu)
 * For A1.implementation in CS3733 B01
 * 
 * Upgraded to CS 3733/D08 by Heineman [3-09-2008]
 */
public class PyramidGame extends Solitaire {
  /* Model elements */
  protected Deck theDeck;
	/* Manages selection logic. */
  protected SelectionManager selectionManager;
  protected Pyramid thePyramid;
  protected Column theDiscards;     // this is a Column with an ExtendedRowView
  protected Pile theJustDrawn;	// this is a Pile with an ExtendedPileView

  /* View elements */
  protected DeckView theDeckView;
  protected PyramidView thePyramidView;
  protected ExtendedRowView theDiscardsView;
  protected ExtendedPileView theJustDrawnView;
  protected IntegerView theScoreView, theNumLeftView;


  /*************
   * Constructor
   */
  public PyramidGame() {
    super();
  }
  /****************
   * returns the name of the variation
   */
  public String getName() {
    return "Pyramid";
  }
  /************************
   * returns preffered size
   */
  public java.awt.Dimension getPreferredSize() {
    return new java.awt.Dimension (855, 635);
  }
  /**************
   * returns the version of the variation
   */
  public String getVersion() {
    return "1.1";
  }
/**
 * Return whether the game has been won.
 * @return boolean
 */
public boolean hasWon() {
	return theDeck.empty() && theDiscards.empty() && theJustDrawn.empty() && thePyramid.empty();
}
  /****************
   * initialization
   */
  public void initialize() {
	initializeModel(getSeed());
	initializeView();
	initializeController();

	thePyramid.deal(theDeck);
	updateNumberCardsLeft(-thePyramid.countCards());
  }  
  /*********************************
   * initialize the control elements
   */
  private void initializeController() {
	theDeckView.setMouseAdapter(new PyramidDeckController(this, theDeckView, selectionManager));
	theJustDrawnView.setMouseAdapter(new PyramidJustDrawnController(this, theJustDrawnView, selectionManager));
	thePyramidView.setMouseAdapter(new PyramidPyramidController(this, thePyramidView, selectionManager));
	theDiscardsView.setMouseAdapter(new PyramidDiscardsController(this, theDiscardsView, selectionManager));
}          
  /**********************************************
   * initialize the model elements given the seed
   */
  private void initializeModel(int seed) {
	theDeck = new Deck("theDeck");
	theDeck.create(seed);

	thePyramid = new Pyramid(7, "thePyramid");
	theDiscards = new Column("theDiscards");
	theJustDrawn = new Pile("theJustDrawn");

	selectionManager = new SelectionManager (thePyramid, theDiscards, theJustDrawn);
	
	MutableInteger numLeft = getNumLeft();
	numLeft.setValue(52);

	addModelElement(theDeck);
	addModelElement(thePyramid);
	addModelElement(theDiscards);
	addModelElement(theJustDrawn);
}    
  /******************************
   * initialize the view elements
   */
  private void initializeView() {
	CardImages cards = getCardImages();

	thePyramidView = new PyramidView(thePyramid);
	thePyramidView.calculateConstants (cards);
	thePyramidView.setBounds(165, 10, thePyramidView.getWidth(), thePyramidView.getHeight());
	addViewWidget(thePyramidView);

	theScoreView = new IntegerView(getScore());
	theScoreView.setBounds((thePyramidView.getWidth() + 170), 10, 100, 70);
	addViewWidget(theScoreView);

	theDeckView = new DeckView(theDeck);
	theDeckView.setBounds(10, (thePyramidView.getHeight() + 50), cards.getWidth(), cards.getHeight());
	addViewWidget(theDeckView);

	theNumLeftView = new IntegerView(getNumLeft());
	theNumLeftView.setBounds((cards.getWidth() + 20), (thePyramidView.getHeight() + cards.getHeight() - 20), 100, 70);
	addViewWidget(theNumLeftView);

	theJustDrawnView = new ExtendedPileView(theJustDrawn);
	theJustDrawnView.setBounds((cards.getWidth() + 130), (thePyramidView.getHeight() + 50), cards.getWidth(), cards.getHeight());
	addViewWidget(theJustDrawnView);

	theDiscardsView = new ExtendedRowView(theDiscards);
	theDiscardsView.setBounds(((cards.getWidth() * 2) + 140), (thePyramidView.getHeight() + 50), theDiscardsView.getDefaultWidth(getCardImages()), cards.getHeight());
	addViewWidget(theDiscardsView);

}
  
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = Main.generateWindow(new PyramidGame(), Deck.OrderBySuit);
		gw.setVisible(true);
	}
}
