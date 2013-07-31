package graphic.marker;

import common.Constants;
import common.Helper;
import graphic.jpanel.JPanelImmagine;
import objects.Point;
import objects.Room;


public class Marker extends MyMouseListeners implements Constants {

    private static final long serialVersionUID = -2609466806655673458L;

    private Room room = null;

    public String getElevator() {
        return elevator;
    }

    public void setElevator(String elevator) {
        this.elevator = elevator;
    }

    public String getStair() {
        return stair;
    }

    public void setStair(String stair) {
        this.stair = stair;
    }

    private String elevator = null;
    private String stair = null;


    // COSTRUTTORI //
    public Marker(Point point,
                  JPanelImmagine jPanelImmagine) {

        super(point, jPanelImmagine);
        setMarker(this);

        jPanelImmagine.add(this);

        // imposto la posizione dell'oggetto sul JPanel
        this.setBounds();

        this.setVisible(true);
        this.setEnabled(true);

        // abilito i listener per il trascinamento
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }


    public boolean contains(java.awt.Point p) {

        return Helper.testDistance(p, this.getPoint().getPanelPosition(), DIAMETER);
    }

    public String toString() {

        String out = "Marker ";
        out += "Point: " + point.toString() + "; ";

        if (room != null)
            out += "Room: " + room.toString() + "; ";

        out += "Elevator: " + elevator + "; ";
        out += "Stair: " + stair + "; ";

        return out;
    }


}
