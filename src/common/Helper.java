package common;

import objects.Point;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.34
 * To change this template use File | Settings | File Templates.
 */
public class Helper {


    public static int calculateDistance(int x1, int y1, int x2, int y2) {

        int delta_x = x1 - x2;
        int delta_y = y1 - y2;

        int distance = (int) Math.sqrt(Math.pow(delta_x, 2) + Math.pow(delta_y, 2));

        return distance;
    }

    /*funzione per testare la distanza tra due punti.
    Restituisce TRUE se minore o uguale alla distanza minima impostata*/
    public static boolean testDistance(int x1, int y1, int x2, int y2, int test_distance) {

        return test_distance <= calculateDistance(x1, y1, x2, y2);
    }

    public static boolean testDistance(Point p, Point a, int test_distance) {
        return testDistance(
                p.getPanelPosition_X(),
                p.getPanelPosition_Y(),
                a.getPanelPosition_X(),
                a.getPanelPosition_Y(),
                test_distance);
    }

    public static boolean testDistance(java.awt.Point p, java.awt.Point a, int test_distance) {
        return testDistance(
                (int) p.getX(),
                (int) p.getY(),
                (int) a.getX(),
                (int) a.getY(),
                test_distance);
    }
}
