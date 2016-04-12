package ks.common.view;


import java.awt.Color;
import java.awt.Font;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.MutableString;
import ks.common.view.CardImages;
import ks.launcher.Main;

public class TestStringView extends TestCase {

	/** String view and String model (accessed within dummy). */
	final String test = "Testing";
	MutableString str;
	StringView sv;
	
	/** Container. */
	GameWindow  gw;

	class Dummy extends Solitaire {
		@Override
		public String getName() { return "dummy"; }

		@Override
		public boolean hasWon() { return false; }

		@Override
		public void initialize() {
			
			CardImages ci = this.getCardImages();
			int w = ci.getWidth();
			int h = ci.getHeight();
			
			// model
			str = new MutableString(test);
			addModelElement(str);
			
			// view
			sv = new StringView(str);
			sv.setBounds (20+w, 10, w, h);
			
			addViewWidget(sv);
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
		//  not much to do.
		assertEquals (test, str.getValue());
		
		MutableString ms = (MutableString) sv.getModelElement();
		String target = ms.getValue();
		assertEquals (test, target);
		
		sv.setFontSize(24);
		assertEquals (24, sv.getFontSize());
				
		sv.setColor(Color.red);
		assertEquals (Color.red, sv.getColor());
		
		sv.setFont(new Font("Arial", Font.PLAIN, 10));
		assertEquals(new Font("Arial", Font.PLAIN, 10), sv.getFont());
	}
	
	public void testConstructor() {
		StringView sv2 = new StringView("Sample");
		assertEquals ("Sample", ((MutableString)sv2.getModelElement()).getValue());
	}	
	
}
