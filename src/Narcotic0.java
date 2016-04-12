import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.view.*;
import ks.launcher.Main;

public class Narcotic0 extends Solitaire {

	IntegerView scoreView;
	IntegerView numLeftView;

	/** Return the name of this solitaire variation. */
	@Override
	public String getName() {
		return "Narcotic0";
	}

	/** Determine whether game has been won. */
	public boolean hasWon() {
		return false; // not ready for this yet.
	}

	void initializeModel(int seed) {
		// initial score is set to ZERO (every Solitaire game by default has a score) 
		// and there are 52 cards left.

		numLeft = getNumLeft();
		numLeft.setValue(52);
		score = getScore();
		score.setValue(0);
	}

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

	void initializeController() {

	}

	/** Initialize solitaire variation. */
	@Override
	public void initialize() {
		// Initialize model, view, and controllers.
		initializeModel(getSeed());
		initializeView();
		initializeController();
	}

	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		GameWindow gw = Main.generateWindow(new Narcotic0(), 117);
		gw.setVisible(true);
	}
}
