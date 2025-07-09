/**
 * 
 */
package test;

import java.util.Observable;

/**
 * @author Christian
 * 
 */
public class ProgressTestThread extends Observable implements Runnable {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            setChanged();
            notifyObservers("nr. 1");
            Thread.sleep(2500);
            setChanged();
            notifyObservers("nr. 2");
            Thread.sleep(2500);
            setChanged();
            notifyObservers("nr. 3");
            Thread.sleep(2500);
            setChanged();
            notifyObservers();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
