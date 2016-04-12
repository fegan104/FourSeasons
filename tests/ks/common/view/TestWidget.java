package ks.common.view;

import java.awt.Point;
import java.awt.Rectangle;

import junit.framework.TestCase;
import ks.common.model.Card;

public class TestWidget extends TestCase {

	public void testBad() {
		try {
			new CardView(null);
			fail ("Must detect null model element");
		} catch (Exception e) {
			// success
		}
		
		// all should fail because widget not added to container
		Widget w = new CardView (new Card (Card.KING, Card.SPADES));
		try {
			w.setMouseAdapter(null);
			fail("Must detect when detached widget.");
		} catch (Exception e) {
			// success
		}
		
		try {
			w.setMouseManager(null);
			fail("Must detect when detached widget.");
		} catch (Exception e) {
			// success
		}
		
		try {
			w.setMouseMotionAdapter(null);
			fail("Must detect when detached widget.");
		} catch (Exception e) {
			// success
		}
		
		try {
			w.setUndoAdapter(null);
			fail("Must detect when detached widget.");
		} catch (Exception e) {
			// success
		}
		
		// invalid names
		try {
			w.setName(null);
			fail ("must prevent null names.");
		} catch (Exception e) {
			// success
		}
		
		// validate default return is false
		Widget w2 = new EmptyWidget();
		w2.redraw(); // just for coverage
		assertFalse (w2.returnWidget(w));
	}
	
	public void testDimensions() {
		Widget w = new CardView (new Card (Card.KING, Card.SPADES));
		
		w.setX(13);
		assertEquals (13, w.getX());
		
		w.setY(167);
		assertEquals (167, w.getY());
		
		w.setXY(10, 20);
		assertEquals (10, w.getX());
		assertEquals (20, w.getY());
	}
	
	public void testToStringExists() {
		Widget w = new CardView (new Card (Card.KING, Card.SPADES));
		assertTrue (w.toString() != null);
	}
	
	public void testInBoundsCheck() {
		Widget w = new CardView (new Card (Card.KING, Card.SPADES));
		w.setBounds(new Rectangle (10, 10, 200, 100));
		assertTrue (w.inBounds(new Point (10,10)));
		assertTrue (w.inBounds(new Point (210,10)));
		assertTrue (w.inBounds(new Point (210,110)));
		assertTrue (w.inBounds(new Point (10,110)));
		
		assertFalse (w.inBounds(new Point (10,8)));
		assertFalse (w.inBounds(new Point (212,10)));
		assertFalse (w.inBounds(new Point (210,112)));
		assertFalse (w.inBounds(new Point (8,110)));
		
		
	}
	
}
