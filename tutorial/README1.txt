Model Narcotic1
===============

As we move on to each successive variation, these README files will only 
contain the changes that you need to apply to the prior version.

 * T1: Provide a name for your variation
 * T2: Construct the Model which represents the variation
 * T3: Construct the View by which the model is shown to the user
 * T4: Construct the Controllers by which the user interacts with the view.
 * T5: Provide logic to be used when determining if the variation has been won
 * T6: Putting all initializations together
    
We will cover each in turn. Copy Narcotic0 class and create a new class Narcotic1.

Task T1: 

To provide a name for your variation modify the following method:

    /** Return the name of this solitaire variation. */
	public String getName() {
		return "Narcotic1";
	}
	
T2: Construct Model

We can add four piles and one deck to our model. Note how each is created
and then added to the model elements for the Solitaire game. Each model 
element has a name, and we store references to these model elements as
class attributes so we can refer back to them later.

	private void initializeModel(int seed) {
		numLeft = getNumLeft();
		numLeft.setValue(52);
		score = getScore();
		score.setValue(0);
		
		// add to our model a deck, properly shuffled using the seed. 
		deck = new Deck("deck");
		deck.create(seed);
		addModelElement(deck);

		pile1 = new Pile("pile1");
		pile2 = new Pile("pile2");
		pile3 = new Pile("pile3");
		pile4 = new Pile("pile4");

		// add to our model a set of four piles
		addModelElement(pile1);
		addModelElement(pile2);
		addModelElement(pile3);
		addModelElement(pile4);
	}

You will need to add 'deck' and 'pile1' - 'pile4' as class attributes.

T3: Construct View

The view will correspond, 1-to-1, with the model.

	private void initializeView() {
		// Get the card artwork to be used. This is needed for the dimensions.
		CardImages ci = getCardImages();

		// add to our view (as defined within our superclass). Similar for other widgets
		deckView = new DeckView(deck);
		deckView.setBounds(20, 20, ci.getWidth(), ci.getHeight());
		addViewWidget(deckView);

		pileView1 = new PileView(pile1);
		pileView1.setBounds(40 + ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		addViewWidget(pileView1);

		pileView2 = new PileView(pile2);
		pileView2.setBounds(60 + 2 * ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		addViewWidget(pileView2);

		pileView3 = new PileView(pile3);
		pileView3.setBounds(80 + 3 * ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		addViewWidget(pileView3);

		pileView4 = new PileView(pile4);
		pileView4.setBounds(100 + 4 * ci.getWidth(), 20, ci.getWidth(), ci.getHeight());
		addViewWidget(pileView4);

		scoreView = new IntegerView(getScore());
		scoreView.setBounds(100 + 5 * ci.getWidth(), 20, 100, 60);
		addViewWidget(scoreView);

		numLeftView = new IntegerView(getNumLeft());
		numLeftView.setBounds(200 + 5* ci.getWidth(), 20, 100, 60);
		addViewWidget(numLeftView);
	}


You will need to add 'deckView' and 'pileView1' - 'pileView4' as class attributes.

Note how each of the view elements is appropriately placed. Note that you will
receive a warning if two view elements overlap when you attempt to add them
using the 'addViewWidget' method.

T4: Construct Controllers

In this example, there are no controllers.

T5: Logic 

At this point, we now have all model elements. The game is over when the deck and 
all piles are empty. We can't just check either of these states; both must be true.

	public boolean hasWon() {
		return deck.empty() &&
			   pile1.empty() && pile2.empty() && pile3.empty() && pile4.empty();
	}	
	

T6: Ensure initialization properly starts with four cards visible

Modify the initialize() method to deal out four cards from the deck.

    /** Initialize solitaire variation. */
	public void initialize() {
		// Initialize model, view, and controllers.
		initializeModel(getSeed());
		initializeView();
		initializeController();

		// Prepare game AFTER all controllers are set up.
		// each column gets a card from the deck.
		pile1.add (deck.get());
		pile2.add (deck.get());
		pile3.add (deck.get());
		pile4.add (deck.get());

		// we have dealt four cards.
		updateNumberCardsLeft (-4);	
	}
	
Execute Narcotic1
=================

To execute, make sure your solitaire variation has the following main method:

	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = Main.generateWindow(new Narcotic1(), 117);
		gw.setVisible(true);
	}

This will properly place the variation within its own window. Note that the 
value '117' represents the initial "seed" which guarantees that decks are
properly shuffled. When you run, you shall see the following image:

  <DECK>   Q-Diamonds  9-Diamonds  2-Diamonds  J-Clubs

Of course there is no behavior yet! And that is why we must construct some controllers.
The purpose of the controller is to manipulate the state of the solitaire game
through the views. This is done, naturally, using mouse events such as MousePress
and MouseDrag. More details are found in Narcotic2
