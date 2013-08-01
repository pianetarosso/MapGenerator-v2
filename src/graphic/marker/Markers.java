package graphic.marker;

import communication.ToJS;
import graphic.jpanel.JPanelImmagine;
import graphic.jpanel.MyJPanel;
import graphic.path.Paths;
import objects.Floor;
import objects.Point;
import objects.Room;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 20/07/13
 * Time: 12.28
 * To change this template use File | Settings | File Templates.
 */
public class Markers extends ArrayList<Marker> {


    public void setVisible(Floor floor) {

        for (Marker m : this) {

            m.setVisible(m.getPoint().getFloor() == floor);
            m.setEnabled(m.getPoint().getFloor() == floor);
        }
    }

    public void setSelected() {

        for (Marker m : this)
            if (m.isClicked()) {
                this.selected = m;
                break;
            }
    }

    public void setSelected(Marker m) {

        resetSelection();
        m.setClicked(true);

        selected = m;
    }

    public void refresh() {
        for (Marker m : this) {
            m.repaint();

        }
    }

    public void setSelected(java.awt.Point p) {

        resetSelection();

        for (Marker m : this)
            if (m.contains(p)) {
                m.setClicked(true);
                selected = m;
                break;
            }
    }

    public Marker getSelected() {
        return selected;
    }


    public void resetSelection() {

        if (selected != null) {
            selected.setClicked(false);
            selected = null;
        }
    }

    private Marker selected = null;
    ToJS toJS;

    public Markers(ToJS toJS) {
        super();
        this.toJS = toJS;
    }

    public void delete(Paths paths, ArrayList<Point> points, MyJPanel myJPanel) {

        if (selected != null) {

            selected.setEnabled(false);
            selected.setVisible(false);

            myJPanel.remove(selected);

            // verifico se si tratta di un ascensore o di una scala e
            // cancello eventuali riferimenti
            if ((selected.getElevator() != null) || (selected.getStair() != null))
                paths.deleteFloorConnection(selected.getPoint());

            // verifico se è utilizzato all'interno delle path
            // se non lo è lo elimino dalla lista di points
            if (!paths.isPointUsed(selected.getPoint()))
                points.remove(selected.getPoint());

            this.remove(selected);

            resetSelection();
        }
    }

    // salvo un nuovo marker
    public void save(
            String RFID,
            boolean access,
            Room room,
            String elevator,
            String stair,
            Paths paths,
            ArrayList<Point> points) {

        selected.getPoint().setRFID(RFID);
        selected.getPoint().setAccess(access);
        selected.setRoom(room);
        selected.setElevator(elevator);
        selected.setStair(stair);

        paths.addNewMarker(selected);

        if (!points.contains(selected.getPoint()))
            points.add(selected.getPoint());

        this.add(selected);

        resetSelection();
    }

    public void dismiss(JPanelImmagine jPanelImmagine) {

        if (selected != null) {

            if (!this.contains(selected)) {
                selected.setEnabled(false);
                selected.setVisible(false);

                jPanelImmagine.remove(selected);
            }
        }
        resetSelection();
    }

    public String toString() {

        String out = "Markers:\n";

        for (Marker m : this)
            out += m.toString() + "\n";

        return out;
    }
}
