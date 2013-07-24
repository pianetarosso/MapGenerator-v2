package graphic.marker;

import graphic.jpanel.JPanelImmagine;
import objects.Point;
import objects.Room;


public class Marker extends MyMouseListeners {

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
