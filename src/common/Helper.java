package common;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.34
 * To change this template use File | Settings | File Templates.
 */
public class Helper {

    /*funzione per testare la distanza tra due punti.
    Restituisce TRUE se minore o uguale alla distanza minima impostata*/
    public static boolean testDistance(int x1, int y1, int x2, int y2) {

        int delta_x = x1 - x2;
        int delta_y = y1 - y2;

        int distance = (int) Math.sqrt(Math.pow(delta_x, 2) + Math.pow(delta_y, 2));

        return Constants.MIN_DISTANCE <= distance;
    }
}
