Model Narcotic0
===============

Let's write our first Solitaire variation!  Of course it will be totally brain-dead
and do nothing, but your task is to learn the structure of a solitaire variation.

First some basics. Each solitaire variation extends the ks.common.games.Solitaire
class, the common base class for all solitaire variations. 

   public class Narcotic0 extends Solitaire {
      
   }

The Solitaire superclass will manage much of the responsibility for you, yet you
must take care of the following tasks:

  * T1: Provide a name for your variation
  * T2: Construct the Model which represents the variation
  * T3: Construct the View by which the model is shown to the user
  * T4: Construct the Controllers by which the user interacts with the view.
  * T5: Provide logic to be used when determining if the variation has been won
  * T6: Putting all initialization together
    
We will cover each in turn.

Task T1: 

To provide a name for your variation override the following method:

    /** Return the name of this solitaire variation. */
	public String getName() {
		return "Narcotic0";
	}

T2: Construct Model

The model represents the information needed to represent the solitaire game.
By default, every solitaire variation has two model elements: the Score and
the NumberCardsLeft. Each of these is of type ks.common.model.MutableInteger
which allows a value to be manipulated. These model elements are retrieved
by the methods getScore() and getNumLeft() inherited from the Solitaire class. 

Each model element will be a subclass of the ks.common.model.Element class.
Every model element has a name and may have a registered listener, who will
be notified whenever any change is made to the model element. In this way,
the Model/View/Controller design pattern will be followed.

When a solitaire variation is instantiated it is given an integer seed, which 
will be used later to properly shuffle the deck. For now, we have no deck so 
this value is ignored.

The model must be constructed as part of the initialize() method for the 
Solitaire variation. In this variation we set the stage in initialize for separating
the initialization of the model from the initialization of the view.

For our purposes, we set the score to 0 and the number of cards left to 52. The
logic can be found in initializeModel(int seed):

	void initializeModel(int seed) {
		// initial score is set to ZERO (every Solitaire game by default has a score) 
		// and there are 52 cards left.

		numLeft = getNumLeft();
		numLeft.setValue(52);
		score = getScore();
		score.setValue(0);
	}

T3: Construct View

The view represents the means by which the model is shown to the user. Every 
model class has a corresponding view class that is used to represent that information.
For example, the MutableInteger model element has an IntegerView class which 
shows the number on the screen.

Each View element extends the ks.common.view.Widget class. Each Widget object 
will be given a Model element to display to the user, and each of the various
Widget subclasses corresponds to the various Element subclasses.

To construct an IntegerView to show the score at a specific location, we would 
include the following when initializing the view:

	void initializeView() {
		// Get the card artwork to be used. This is needed for the dimensions.
		CardImages ci = getCardImages();

		scoreView = new IntegerView(score);
		scoreView.setBounds(100 + 5 * ci.getWidth(), 20, 100, 60);
		addViewWidget(scoreView);

		numLeftView = new IntegerView(numLeft);
		numLeftView.setBounds(200 + 5* ci.getWidth(), 20, 100, 60);
		addViewWidget(numLeftView);
	}

You need to create two attributes of the Narcotic0 class for these view elements.

  IntegerView scoreView;
  IntegerView numLeftView;

Note that the Solitaire base class makes it possible to retrieve the card
images used to display the variation. This is useful since then one can 
locate cards based on the sizes of the cards themselves. In the above example,
the 'scoreView' Widget is created to display the value found in the 'score'
Model element. The bounds of the widget are set (using x, y, width, height
coordinates). Thus the first two declares the anchor (x,y) point which is 
the upper-left corner of where the widget shall appear and (width x height) 
declares the dimensions. Note that all widgets appear in a green Canvas and
the upper left corner is the location (0,0), where increasing x values move to
the right and increasing y values move downwards. 

T4: Construct Controllers

  void initializeController() {
		
  }

In this example, there are no controllers.

T5: Logic 

At this point, it is premature to encode the logic since we are just beginning
the tutorial. So for now, we blindly return 'false' in the overridden method:

	/** Determine whether game has been won. */
	public boolean hasWon() {
		return false; // not ready for this yet.
	}
	
	
T6: Putting all initialization together

Once above methods are written, then you must override the initialize() method to 
properly invoke these methods.

    /** Initialize solitaire variation. */
	public void initialize() {
		// Initialize model, view, and controllers.
		initializeModel(getSeed());
		initializeView();
	}
	
Execute Narcotic0
=================

To execute, make sure your solitaire variation has the following main method:

	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = Main.generateWindow(new Narcotic0(), 117);
		gw.setVisible(true);
	}

This will properly place the variation within its own window. Note that the 
value '117' represents the initial "seed" which guarantees that decks are
properly shuffled. We will see decks in the Narcotic1 variation.





