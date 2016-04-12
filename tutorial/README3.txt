Model Narcotic3
===============

As we move on to each successive variation, these README files will only 
contain the changes that you need to apply to the prior version.

  * T5: Construct the Controllers by which the user interacts with the view.
    
We will cover each in turn.

Task T5: 

Now that we have the DealFour working in Narcotic2, run it and advance two
more times (that is, deal two sets of four cars) until you see the following:

    9H 7H 8S 7C
    
You are now viewing four PileView widgets, each of which is able to show a 
Pile on the screen by only showing its top cards. Indeed, when considering
these piles from left to right, here are their actual contents

    pile1: [QD 8C 9H]
    pile2: [9D JH 7H]
    pile3: [2D 9C 8S]
    pile4: [JC 6S 7C]
    
Note how only the last card (known as the top card in the pile) is shown to the
screen.     

According to the rules, I can place the 7C on the 7H because they have the same
rank. To make this happen, I will show how to select a card from one pile and
drag it to be placed on top of another pile. This action is broken up into three 
steps:

  S1 MousePress on some source Widget
  S2 MouseDrag to a new location, passing over anything beneath it
  S3 MouseRelease on destination Widget
  
Recall that each Widget has a source Model Element which it shows on the screen.
In technical terms, the model Element "backs" or "supports" the Widget on screen.
This relationship is the key to understanding everything that follows in this 
discussion. For example, when you see the 9H on the screen, you are looking at
a PileView widget which is responsible for making visible the pile that supports
the widget.

So we must translate user mouse actions on a widget into actions that affect the
model's state. The simplest is when, for example, we want to move just a single
card from one model element to another.  

Let's make our first step towards realizing this overall goal. The following 
controller, found in tutorial.v3.NarcoticPileController, will simply eliminate
the top card from a Pile. See actual class for full documentation:

public class NarcoticPileController extends java.awt.event.MouseAdapter {
    protected Solitaire narcoticGame = null;
    PileView pileview;

    /** NarcoticDeckController constructor comment. */
    public NarcoticPileController(Solitaire game, PileView pileview) {
        super();

        narcoticGame = game;
        this.pileview = pileview;
    }
    
    public void mousePressed(java.awt.event.MouseEvent me) {
    	// get the element backing the view widget
        Pile pile = (Pile) pileview.getModelElement();

        // delete its top card.
        Card c = pile.get();
        System.out.println ("Removed " + c);
        
        // have solitaire game refresh widgets that were affected 
        narcoticGame.refreshWidgets();
    }
}

To register this controller, review the initializeController() 
method in Narcotic3.java

Note how this controller reacts to mousePressed events by simply
finding the model element backing the PileView. From this pile,
the top card is removed. To ensure that everything is redrawn
properly, we simply ask the solitaire game to refresh the widgets
that have been updated since the last time they were drawn.

This is a poor excuse for a controller since there is no actual
move made, as there was with DealFourMove. You can't undo the 
removal of a card (try it and see!).

Now for the undeniable fact: When the mouse is pressed we can't 
actually know the move that is to be made by the player. The move
will only be known when the mouse is released! We must store the 
card being removed until it is needed when the mouse is released.
Indeed we do more than that, we note that the Container within which
the solitaire variation executes is able to keep track of an "active
dragging" object. The active dragging object is a Widget that will 
be dragged wherever the mouse is dragged. 

To see the dragging behavior in action, change the mousePressed method
in the above NarcoticPileController instead to the following:

    public void mousePressed(java.awt.event.MouseEvent me) {

        // Ask PileView to retrieve the top card as a CardView
        // Widget
        CardView cardView = pileview.getCardViewForTopCard(me);

        // Have container track this object now. Record where it came from
        Container c = narcoticGame.getContainer();
        c.setActiveDraggingObject (cardView, me);
        c.setDragSource(pileview);

        // we simply redraw our source pile to avoid flicker,
        // rather than refreshing all widgets...
        pileview.redraw();
    }

You will see that the widget classes are quite powerful (and you will need 
to follow their documentation properly). In this example, the top card is 
retrieved as a 'widget' and handed off to the container to manage.  You can
drag it around as much as you like. Note how when you stop dragging, the card
returns to its original location (by means of a special controller that is 
pre-installed for the Container, known as SolitaireReleasedAdapter). However,
if you drag and release the mouse over one of the IntegerViews or the DeckView
or indeed another one of the PileViews, the card just floats and it is lost.

Clearly we need to coordinate the press and release events, and that is the 
fourth example.