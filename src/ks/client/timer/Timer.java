package ks.client.timer;

import java.awt.event.*;
import java.awt.AWTEventMulticaster;
/**
 * Manages a timer. Sets a timer to wait before an action event is posted to a component.
 * The caller can specify the time delay and whether this repeats.
 *
 * The timer is implemented as a thread.  The one of the start(...) methods should
 * be called to start the thread.
 *
 * Creation date: (10/16/01 7:27:56 PM)
 * @author: Extracted from Symantec Cafe 2.5 (c) Symantec
 */
public class Timer implements Runnable, java.io.Serializable {
	/**
	 * Keep Eclipse happy
	 */
	private static final long serialVersionUID = 1L;

   	protected boolean				repeat;
	protected boolean				execute;
	protected boolean				live;
	protected int					delay;
	protected String				actionCommand;
	protected ActionListener		actionListener = null;
	transient protected Thread	    thread;
	
/**
  * Creates a timer with the default delay.
  * After 1000 miliseconds this timer will fire an ActionEvent.
  * It will not repeat.
  * Creation date: (10/16/01 7:29:12 PM)
  */
public Timer() {
	this(1000, false);
}
/**
 * Creates a timer with specified delay.
 * After the specified delay this timer will fire an ActionEvent.
 * It will not repeat.
 * Creation date: (10/16/01 7:29:55 PM)
 * @param d the delay in milliseconds
 */
public Timer(int d) {
	this(d, false);
}
/**
  * Creates a timer with specified delay and repeat setting.
  * After the specified delay this timer will fire an ActionEvent.
  * It may repeat, depending on r.
  * Creation date: (10/16/01 7:31:34 PM)
  * @param d the delay in milliseconds
  * @param r if true, reset and repeat after generating the event
  */
public Timer(int d, boolean r) {
	delay = d;
	repeat = r;
	execute = false;
	live = false;
	thread = new Thread(this);
}
/**
  * Creates a timer with specified repeat setting and the default delay.
  * After 1000 miliseconds this timer will fire an ActionEvent.
  * It may repeat, depending on r.
  * Creation date: (10/16/01 7:30:40 PM)
  * @param r if true, reset and repeat after generating the event
  */
public Timer(boolean r) {
	this(1000, r);
}
/**
  * Adds the specified action listener to receive action events
  * from this button.
  * Creation date: (10/16/01 7:38:53 PM)
  * @param l the action listener
  */
public void addActionListener(ActionListener l) {
	actionListener = AWTEventMulticaster.add(actionListener, l);
}
/**
 * Returns the command name of the action event fired by this button.
 * @see #setActionCommand
 *
 * Creation date: (10/16/01 7:40:03 PM)
 * @return java.lang.String
 */
public java.lang.String getActionCommand() {
	return actionCommand;
}
/**
  * Obtains the delay time setting for this timer.
  * Creation date: (10/16/01 7:40:03 PM)
  * @return the current delay setting for this timer, in milliseconds
  * @see #setDelay(int)
  */
public int getDelay() {
	return delay;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/01 7:40:13 PM)
 * @return boolean
 */
public boolean isEnabled() {
	return live;
}
/**
 * Obtains the repeat setting of the timer.
 * Creation date: (10/16/01 7:40:03 PM)
 * @return true if this timer is set to repeat, false if this timer does not repeat
 * @see #setRepeat
 * @return boolean
 */
public boolean isRepeat() {
	return repeat;
}
/**
  * Pauses the timer.
  * Differs from stop in that the timer
  * is continued from whatever state it was in before
  * pausing.
  * <p>
  * start() and stop() overrule this function.
  * Creation date: (10/16/01 7:32:48 PM)
  * @see #resume
  * @see #start
  * @see #stop
  */
@SuppressWarnings("deprecation")
public synchronized void pause() {
	execute = false;
	if (thread.isAlive())
		thread.suspend();
	if (thread.isAlive())
		thread.suspend();
}
/**
 * Creation date: (10/16/01 7:45:37 PM)
 */
private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();

        execute   = false;
        thread    = new Thread(this);
    }
/**
 * Removes the specified action listener so it no longer receives
 * action events from this button.
 * @param l the action listener
 * Creation date: (10/16/01 7:37:28 PM)
 */
public void removeActionListener(ActionListener l) {
	actionListener = AWTEventMulticaster.remove(actionListener, l);
}
/**
 * Restarts the timer immediately with the current delay value.
 * This will start a stopped timer.
 * @see #start()
 * @see #stop()
 */
public synchronized void restart() {
	stop();
	start();
}
/**
  * Resumes the timer.
  * Differs from start in that the timer
  * is continued from whatever state it was in before
  * pausing.
  * <p>
  * start() and stop() overrule this function
  * Creation date: (10/16/01 7:33:46 PM)
  * @see #pause
  * @see #start
  * @see #stop
  */
@SuppressWarnings("deprecation")
public synchronized void resume() {
	if (!execute) {
		execute = true;
		if (thread.isAlive())
			thread.resume();
		else
			start();
	}
}
/**
  * The thread body.  This method is called by the Java virtual machine in response to a
  * start call by the user.
  * Creation date: (10/16/01 7:35:20 PM)
  * @see #start()
  * @see #start(int)
  * @see #start(boolean)
  * @see #start(int, boolean)
  * @see #stop
  */
@SuppressWarnings("deprecation")
public void run() {
	boolean repeating;
	if (!execute) {
		thread.suspend();
	}
	while (live) {
		do {
			repeating = repeat;
			try {
				Thread.sleep(delay);
				if (execute)
					sourceActionEvent();
			} catch (InterruptedException e) {
				return;
			}
		} while (repeating && live);
		if ((!execute && live) || !repeating)
			thread.suspend();
	}
}
/**
 * Sets the command name of the action event fired by this button.
 * Creation date: (10/16/01 7:40:03 PM)
 * @param command Tthe name of the action event command fired by this button
 * @see #getActionCommand
 * if the specified property value is unacceptable
 * @param newActionCommand java.lang.String
 */
public void setActionCommand(java.lang.String newActionCommand) {
	actionCommand = newActionCommand;
}
/**
  * Sets the delay time for this timer.
  * Creation date: (10/16/01 7:40:03 PM)
  *    
  * @param d the delay in milliseconds.  This delay will be used starting
  *          after the current delay elapses
  *
  * if the specified property value is unacceptable
  * @see #getDelay()
  */
public void setDelay(int newDelay) {
	delay = newDelay;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/01 7:40:13 PM)
 * @param newLive boolean
 */
void setLive(boolean newLive) {
	live = newLive;
}
/**
  * Changes the repeat setting of the timer.
  * If the repeat setting is false a single event will be generated.  When
  * set to true the timer produces a series of events.
  * Creation date: (10/16/01 7:40:03 PM)
  *
  * @param f reset and repeat after generating the event
  * if the specified property value is unacceptable
  * @see #isRepeat
  */
public void setRepeat(boolean newRepeat) {
	repeat = newRepeat;
}
/**
  * Fires an action event to the listeners.
  * Creation date: (10/16/01 7:36:15 PM)
  * @see #setActionCommand
  */
private void sourceActionEvent() {
	if (actionListener != null)
		actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand));
}
/**
  * Starts the timer with existing settings.
  * Creation date: (10/16/01 7:34:31 PM)
  * @see #start(int)
  * @see #start(boolean)
  * @see #start(int, boolean)
  * @see #stop
  * @see #run
  */
@SuppressWarnings("deprecation")
public synchronized void start() {
	execute = true;
	live = true;
	if (thread.isAlive() && !thread.isInterrupted()) {
		if (thread.isAlive())
			thread.resume();
	} else {
		thread = new Thread(this);
		thread.start();
	}
}
/**
  * Starts the timer using the specified delay setting
  * @param d the delay in milliseconds
  *
  * @see #start()
  * @see #start(int)
  * @see #start(boolean)
  * @see #stop
  * @see #run
  */
public synchronized void start(int d) {
	setDelay(d);
	start();
}
/**
  * Starts the timer using the specified delay and repeat settings.
  * @param d the delay in milliseconds
  * @param r reset and repeat after generating the event
  * if the specified property value is unacceptable
  * @see #start()
  * @see #start(int)
  * @see #start(boolean)
  * @see #stop
  * @see #run
  */
public synchronized void start(int d, boolean r) {
	setDelay(d);
	setRepeat(r);
	start();
}
/**
 * Starts the timer using the specified repeat setting.
 * @param r reset and repeat after generating the event
 * if the specified property value is unacceptable
 * @see #start()
 * @see #start(int)
 * @see #start(boolean)
 * @see #stop
 * @see #run
 */
public synchronized void start(boolean r) {
	setRepeat(r);
	start();
}
/**
 * Stops the timer.  After return the timer will generate no more events.
 * Creation date: (10/16/01 7:46:08 PM)
 * @see #start
 */
public synchronized void stop() {
	execute = false;
	live = false;
	if (thread.isAlive())
		thread.interrupt();
}
}
