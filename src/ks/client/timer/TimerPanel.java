package ks.client.timer;

/**
 * Visible object representing Timer.
 * 
 * call start(int duration) to start things off.
 * 
 * Creation date: (10/16/01 7:16:20 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public class TimerPanel extends java.awt.Panel implements java.awt.event.ActionListener {

	/**
	 * Keep Eclipse happy.
	 */
	private static final long serialVersionUID = -1035117844065395650L;

	/** Create a timer, that automatically repeats and has one-second delay. */    
	protected Timer timer = new Timer (1000, true);

	/** Whom we should contact when expired. */
	protected TimerUpdate master = null;

	/** The Current time to be counted down */
	protected int currentTime = 0;

	/** Determine if we are in double time (to flash) */
	protected boolean doubleTime = false;

	/** Determine the number of ticks. If this doesn't equal 2 then clear timer label */
	protected int tick = 0;

	/** Size of timer font. */
	public int fontSize = 36;

	private java.awt.Label ivjtimerLabel = null;

	/** Number of seconds between Status Announcements (like a minor tick instead of a timer expired). */
	protected int announceRate = 5;

	/** Current number of times since last announcement. */
	protected int lastAnnouncement = 5;

	/**
	 * TimerPanel constructor comment.
	 */
	public TimerPanel() {
		super();
		initialize();
	}
	/**
	 * Retrieve events from timer and update label
	 * Creation date: (10/16/01 7:21:08 PM)
	 * @param ae java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent ae) {

		// If we are in double time, don't update, but just clear
		if (doubleTime) {
			if (++tick != 2) {
				gettimerLabel().setText(""); // clear
				return;
			}
		}

		currentTime--;
		gettimerLabel().setText(asString(currentTime));

		// make sure to reset tick count to prepare for next flashing
		if (doubleTime) {
			tick = 0;
		}

		// Once back to zero, beep and reset time to its initial state
		if (currentTime == 0) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			stop();
			doubleTime = false;

			// announce to the world we are expired.
			master.timerExpired();
			return;
		}

		// if we are here, we haven't expired; update minor tick status.
		if (--lastAnnouncement == 0) {
			lastAnnouncement = getAnnounceRate();
			if (master != null) {
				master.minorTick();
			} else {
				System.out.println("Unable to process master tick...");
			}
		}

		if (currentTime == 10) {
			// go to double time
			setNormalRate(false);
		}
	}
	/**
	 * Convert an integer time (in seconds) into MM:SS
	 * Creation date: (10/16/01 7:20:24 PM)
	 * @param Seconds int
	 */
	public String asString(int seconds) {
		// inefficient way. Can you think of a better way?
		int tenMinutes = seconds / 600;
		int minutes = (seconds - 600 * tenMinutes) / 60;
		int tenSeconds = (seconds - 600 * tenMinutes - 60 * minutes) / 10;
		seconds = seconds - 600 * tenMinutes - 60 * minutes - 10 * tenSeconds;

		return new String("" + tenMinutes + minutes + ":" + tenSeconds + seconds);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/22/01 8:52:26 PM)
	 * @return int
	 */
	public int getAnnounceRate() {
		return announceRate;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/16/01 7:59:07 PM)
	 * @return int
	 */
	public int getFontSize() {
		return fontSize;
	}
	/**
	 * Get the entity that we must contact upon expiration.
	 * Creation date: (10/16/01 7:53:18 PM)
	 * @return edu.wpi.cs.TimerPkg.TimerExpired
	 */
	public TimerUpdate getMaster() {
		return master;
	}
	
	/**
	 * Return the Label1 property value.
	 * @return java.awt.Label
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private java.awt.Label gettimerLabel() {
		if (ivjtimerLabel == null) {
			ivjtimerLabel = new java.awt.Label();
			ivjtimerLabel.setName("timerLabel");
			ivjtimerLabel.setFont(new java.awt.Font("dialog", 0, 24));
			ivjtimerLabel.setText("Label1");
			ivjtimerLabel.setBackground(new java.awt.Color(0,138,0));
			ivjtimerLabel.setBounds(8, 8, 95, 45);
			ivjtimerLabel.setBackground(getBackground());  // resort to background.
		}
		return ivjtimerLabel;
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		setName("TimerPanel");
		setLayout(null);
		setSize(85, 85);
		add(gettimerLabel(), gettimerLabel().getName());

		// user code begin {2}
		stop();  // initially timer is stopped
		timer.addActionListener (this);   // we are listening to the timer for output "ticks"
	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * @param args java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			java.awt.Frame frame = new java.awt.Frame();
			TimerPanel aTimerPanel;
			aTimerPanel = new TimerPanel();
			frame.add("Center", aTimerPanel);
			frame.setSize(aTimerPanel.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of java.awt.Panel");
			exception.printStackTrace(System.out);
		}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/22/01 8:52:26 PM)
	 * @param newAnnounceRate int
	 */
	public void setAnnounceRate(int newAnnounceRate) {
		announceRate = newAnnounceRate;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/16/01 7:59:07 PM)
	 * @param newFontSize int
	 */
	public void setFontSize(int newFontSize) {
		fontSize = newFontSize;
	}
	/**
	 * Declare the entity that we must contact upon timer expiration
	 * Creation date: (10/16/01 7:53:18 PM)
	 * @param newMaster edu.wpi.cs.TimerPkg.TimerExpired
	 */
	public void setMaster(TimerUpdate newMaster) {
		master = newMaster;
	}
	/**
	 * If false, then doubletime 
	 * Creation date: (10/16/01 7:19:06 PM)
	 * @param flag boolean
	 */
	public void setNormalRate(boolean flag) {
		doubleTime = !flag;
		if (doubleTime) {
			timer.setDelay(500); // blink in 1/2 second intervals
			tick = 0;
		} else {
			timer.setDelay(1000); // use 1 second delays
		}
	}
	
	/**
	 * Get timer going for set duration; reset automatically
	 * Creation date: (10/16/01 7:19:47 PM)
	 * @param duration int
	 */
	public void start(int duration) {
		currentTime = duration;
		gettimerLabel().setText(asString(currentTime));

		// Start Timer. Force a jump Start each time.
		timer.stop();
		setNormalRate(true);
		timer.start();
	}
	
	/**
	 * Stop Timer.
	 * Creation date: (10/16/01 7:18:24 PM)
	 */
	public void stop() {
		currentTime = 0;
		gettimerLabel().setText (asString (currentTime));

		// Start Timer.
		timer.stop();
	}
}
