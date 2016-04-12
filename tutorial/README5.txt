We are at the end of the tutorial, though the variation is not complete.

I leave the following loose ends for you to work out:

  * When the deck runs out you need to re-constitute the deck. This can be 
    detected within the mousePress of the NarcoticDeckController. Simply put,
    if the deck is empty, then you should form the deck as described in the
    use case:
    
        Player combines the four piles, right to left, one on top of the other, 
        until a single stack remains. The stack is turned upside down and now 
        becomes the new deck to use.

	Note that you will need to develop a Move subclass which knows of the deck
	and all four piles. Look at the methods available to the Deck and Pile
	elements to make sure you constitute the deck in the right fashion.
	
	By the way, is this move something you would want to undo? I don't think so!
	All you need to do is ensure that the undo method returns 'false' without
	performing any actions.
	
  * You still need to deal with the final move of the game, namely, to be able
    to remove all four cards when they are of the same rank. One way to do this
    is on the DealFour move, although that wouldn't be as fun since the user 
    wouldn't be able to see in time that the cards had been removed. Perhaps what
    you could do is make a controller that takes action on mouseClicked events
    (when the user presses and releases the mouse on a widget without moving it
    at all). In this way, then, you would add a mouseClicked method to the
    NarcoticPileController class and the controller would instantiate a Move
    subclass RemoveFourMove (or some other aptly named class)
	
  * Deal with dragging and releasing cards over the IntegerView objects. Look
    at the existing SolitaireReleasedAdapted and how you could use it "as is".
  
  * See what happens when you try to drag a card from a pile and return it to 
    the same pile. How would you go about fixing this problem?
    
  * Note that you can move the 9C to the 9H pile, but you can also move the 9H 
    to the 9C pile, which is not an allowed move.  How can you solve this problem?
    Well, you may have observed that the pile elements are named in a specific way,
    starting from "pile1" on the left to "pile4" on the right; perhaps this information
    would be useful to you.

If you continue to have questions on how to proceed (or indeed, any issues with 
the earlier parts of this tutorial) then be sure to see one of the TAs or the
professor.

