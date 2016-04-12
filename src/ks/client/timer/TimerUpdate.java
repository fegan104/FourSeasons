package ks.client.timer;

/**
 * Interface for methods to call when timer has expired.
 * Creation date: (10/16/01 7:51:52 PM)
 * @author: George T. Heineman (heineman@cs.wpi.edu)
 */
public interface TimerUpdate {
/**
 * Every n seconds we can receive notification.
 *
 * Creation date: (10/22/01 8:54:20 PM)
 */
void minorTick();
/**
 * When timer has expired, this method is invoked.
 *
 * Creation date: (10/16/01 7:52:03 PM)
 */
void timerExpired();
}
