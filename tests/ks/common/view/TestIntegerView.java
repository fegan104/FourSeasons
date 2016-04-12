package ks.common.view;

import java.awt.Color;
import java.awt.Font;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.MutableInteger;
import ks.common.view.IntegerView;
import ks.launcher.Main;

public class TestIntegerView extends TestCase {

	/** Integer view and mutable integer model (accessed within dummy). */
	MutableInteger mi;
	IntegerView iv;
	
	/** Container. */
	GameWindow  gw;

	class Dummy extends Solitaire {
		@Override
		public String getName() { return "dummy"; }

		@Override
		public boolean hasWon() { return false; }

		@Override
		public void initialize() {
			mi = new MutableInteger(33);
			iv = new IntegerView(mi);
			iv.setBounds (10, 10, 200, 100);
			addModelElement(mi);
			addViewWidget(iv);
		}
	}
	
	/** Solitaire variation. */
	Solitaire sol;
	
	protected void setUp() {
		// Seed is to ensure we get the same initial cards every time.
		gw = Main.generateWindow(sol = new Dummy(), 117);
	}
	
	
	protected void tearDown() {
		gw.setVisible(false);
		gw.dispose();
	}
	
	public void testBase() {
		iv.setFontSize(24);
		assertEquals (24, iv.getFontSize());
				
		iv.setColor(Color.red);
		assertEquals (Color.red, iv.getColor());
		
		iv.setFont(new Font("Arial", Font.PLAIN, 10));
		assertEquals(new Font("Arial", Font.PLAIN, 10), iv.getFont());
	}
}
