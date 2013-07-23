package graphic.marker;

import graphic.jpanel.JPanelImmagine;
import graphic.jpanel.MyJPanel;
import graphic.path.Paths;
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

    public Marker getSelected() {
        return selected;
    }


    public void resetSelection() {

        if (selected != null)
            selected.setClicked(false);
    }

    private Marker selected = null;

    public Markers() {
        super();
    }

    public boolean contains(Point p) {

        for (Marker m : this) {

            if (m.point == p)
                return true;
        }
        return false;
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
            if (paths.isPointUsed(selected.getPoint()))
                points.remove(selected.getPoint());

            this.remove(selected);

            selected = null;

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

        selected = null;
    }

    public void dismiss(JPanelImmagine jPanelImmagine) {

        if (selected != null) {
            selected.setEnabled(false);
            selected.setVisible(false);

            jPanelImmagine.remove(selected);

            selected = null;
        }
    }

}