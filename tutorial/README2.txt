Model Narcotic2
===============

As we move on to each successive variation, these README files will only 
contain the changes that you need to apply to the prior version.

  * T1: Provide a name for your variation
  * T2: Provide logic to be used when determining if the variation has been won
  * T3: Construct the Model which represents the variation
  * T4: Construct the View by which the model is shown to the user
  * T5: Construct the Controllers by which the user interacts with the view.
  * T6: Putting all initializations together
We will cover each in turn.

Task T1: 

	/** Return the name of this solitaire variation. */
	public String getName() {
		return "Narcotic2";
	}
	
Task T2: unchanged

Task T3: unchanged

Task T4: unchanged

Task T5: Controllers

Observe that each view element can have different kinds of controllers. The
two that are relevant for your variation are MouseListener and MouseMotionListener
which are found in the java.awt.event package. MouseListener handles (press, release
click, enter, exit) while MouseMotionListener handles (drag, move). The overall behavior 
of a controller is to respond to mouse events and attempt to make moves as directed by the player.

The use cases will be the key to everything, as we shall see. Here is one
from the Narcotic problem:

--------------------------------------------------------------------------
Use case Name: Deal four cards
Flow of Events:  1. From the top of the deck, player deals four cards, one 
                    at a time, face up to each of the four piles. In doing so,
                    only the topmost card should remain visible. The number of
                    cards left decreases by four

Entry Condition:      All visible cards have different ranks and the deck is not empty
Exit Condition:       Deck is reduced by four cards
Quality Requirements: The four cards should simply 'appear' in their proper places 
                      in the pile; no need to animate the "dealing" of the cards.
--------------------------------------------------------------------------

We are going to turn this use case into a controller. It is through controllers
that the model is manipulated. Controllers represent a slice of functionality 
of your system's behavior. It will encapsulate the logic of the use case, which 
is good since then any changes to that logic will be in a single place.

Here is the strategy we will follow:

  (A) Encapsulate the move as a subclass of ks.common.model.Move
  (B) Identify the mouse interaction that will represent the way by which the
      user will make the move
  (C) Write the controller to react to the mouse event and cause the move
      to happen.

(A) Encapsulate the move as a subclass of ks.common.model.Move
----------------------------------------------------------------
We delegate the logic for all moves into special Move subclasses of the 
ks.common.model.Move class. In this case, we have created a class 'DealFourMove' 
which will be responsible for dealing four cards from the top of the deck to each
of the four piles. For this to work, we need to create the move object so it can 
determine if the move is even allowed, and then to make the move if it is. For
this to work, the DealFourMove class will need to know about the Deck and the four
Pile objects. Note how the constructor for the DealFourMove class takes five 
parameters representing this information.

The DealFourMove class stores the Deck and four Pile objects as class attributes.
It is responsible for implementing three methods:

  valid (Solitaire)  -- is the move valid given the solitaire state as it exists
  undo (Solitaire)   -- undo the solitaire move assuming it was made on the state
  doMove (Solitaire) -- make the move on the state

Note how valid simply checks that the deck is not empty.  The doMove() method
must check 'valid' first before executing the state change, which is to deal four
cards from the deck to the individual piles, and then update the number of cards
left by subtracting four. We now know what number of cards left means -- it means
how many cards are left in the deck. Note that the score is not manipulated by this
move.

The undo method simply reverses the operations as found in 'doMove'. In Kombat
solitaire you will see that the right mouse press is reserved for requesting
the undo of a move.

(B) Identify the mouse interaction that will represent the way by which the
    user will make the move
----------------------------------------------------------------------------

We choose to act on the press of a left mouse button.

(C) Write the controller to react to the mouse event and cause the move to happen.
----------------------------------------------------------------------------------
We write a controller that extends java.awt.event.MouseAdapter and overrides just 
the method mousePressed. Please refer to the code in the class NarcoticDeeckController 
in the package tutorial.v2.

public class NarcoticDeckController extends java.awt.event.MouseAdapter {
   public void mousePressed(java.awt.event.MouseEvent me) {
      // logic happens here.
   }
}

The key steps are to instantiate an object of class DealFourMove and see if the
move is valid. If the move is valid, then you must (a) add the move to the 
solitaire games' list of moves. This is done using the 'pushMove' operation 
provided to you in the Solitaire base class. Then, since changes may have been
made, you need to refresh the widgets that  may have been affected by the 
state changes enacted by the move.

        // Deal four cards
        Move m = new DealFourMove(d, p1, p2, p3, p4);
        if (m.doMove(narcoticGame)) {
            // SUCCESS: have solitaire game store this move
            narcoticGame.pushMove(m);

            // have solitaire game refresh widgets that were affected 
            narcoticGame.refreshWidgets();
        }

Finally, you will need to install this controller, which is done within the
initializeController() method. Once installed, mouse presses on the deck
will be processed by your controller.

	void initializeController() {
		// Initialize Controllers for DeckView
		deckView.setMouseAdapter(new NarcoticDeckController (this));
	}

Execute Narcotic2
=================

Make sure your code has been changed to reflect the latest Narcotic variation.

	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = Main.generateWindow(new Narcotic2(), 117);
		gw.setVisible(true);
	}

Note: If you are continually updating a single "Narcotic" file then there is no 
problem. Here we must remember to change the name of the class being instantiated
otherwise we won't see our changes.

Once executing, press the mouse on the deck widget and you will see four new cards
dealt to the pile, while the number of cardsLeft decreases by four. You can make
this move 13 times before the deck is exhausted. There is a default undo capability
that is already provided if you click on any part of the background green image or
even on any of the available widgets.

