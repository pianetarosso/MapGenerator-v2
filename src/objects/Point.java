package objects;

import common.Constants;
import common.Helper;
import graphic.ZoomManager;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.51
 * To change this template use File | Settings | File Templates.
 */
public class Point {

    private int id;
    private double x, y;
    private Floor floor;
    private String RFID;
    private boolean access = false;

    private boolean valid = false;

    public ZoomManager getZoomManager() {
        return zoomManager;
    }

    private ZoomManager zoomManager;


    public int getId() {
        return id;
    }

    public Point(int id, double x, double y, Floor floor, ZoomManager zoomManager) {
        this.id = id;

        this.x = zoomManager.getRealPosition_X(x);
        this.y = zoomManager.getRealPosition_Y(y);

        this.floor = floor;

        this.zoomManager = zoomManager;

    }

    public double getX() {
        return x;
    }

    public int getPanelPosition_X() {
        return zoomManager.getPanelPosition_X(x);
    }

    public void setX(double x) {
        this.x = zoomManager.getRealPosition_X(x);
    }

    public double getY() {
        return y;
    }

    public int getPanelPosition_Y() {
        return zoomManager.getPanelPosition_Y(y);
    }

    public void setY(double y) {
        this.y = zoomManager.getRealPosition_Y(y);
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;

        valid = false || access;
    }

    // testo se i punti sono troppo vicini tra loro (nel contesto scalato, NON reale)
    public boolean isNear(Point p) {

        if (floor != p.getFloor())
            return false;

        // se TRUE sono troppo vicini
        return Helper.testDistance(this, p, Constants.MIN_MARKER_DISTANCE);
    }

    // testo se i punti sono troppo vicini tra loro (nel contesto scalato, NON reale)
    public boolean isNear(java.awt.Point p) {

        Point pp = new Point(0, p.getX(), p.getY(), floor, zoomManager);

        // se TRUE sono troppo vicini
        return Helper.testDistance(this, pp, Constants.MIN_MARKER_DISTANCE);
    }


    public java.awt.Point getPanelPosition() {
        return new java.awt.Point(getPanelPosition_X(), getPanelPosition_Y());
    }

    public boolean isValid() {
        return valid;
    }

    public void resetValidation() {
        valid = false || access;
    }
}
