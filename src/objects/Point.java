package objects;

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
    private int floor;
    private String RFID;
    private boolean access;

    private ZoomManager zoomManager;


    public int getId() {
        return id;
    }

    public Point(int id, double x, double y, int floor, ZoomManager zoomManager) {
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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
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
    }
}
