package graphic.marker;

import common.Constants;
import graphic.ZoomManager;
import graphic.jpanel.JPanelImmagine;
import objects.Floor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 16.58
 * To change this template use File | Settings | File Templates.
 */
public class MyMouseListeners extends MyJComponent implements MouseListener, MouseMotionListener {

    private ZoomManager zoomManager;

    public MyMouseListeners(
            int x,
            int y,
            ZoomManager zoomManager,
            int id,
            Floor floor,
            JPanelImmagine jPanelImmagine) {
        super(x, y, zoomManager, id, floor, jPanelImmagine);

        this.zoomManager = zoomManager;
    }

    // ascoltatori del mouse
    @Override
    public void mouseEntered(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType()) {
            mouseEntered = true;
            arg0.consume();
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if (jPanelImmagine.isMarkerType()) {
            mouseEntered = false;
            arg0.consume();
        }
    }

    // MOUSE CLICK LISTENER per visualizzare un marker o cancellarlo //////////////////////////////

    @Override
    public void mouseClicked(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType()) {

            jPanelImmagine.stopAll(true);
            arg0.consume();
        } else
            jPanelImmagine.mouseClicked(arg0);
    }


    // MOUSE DRAGGED LISTENER
    // implementa il trascinamento dell'oggetto
    // gestisce anche le collisioni

    @Override
    public void mouseDragged(MouseEvent arg0) {

        Point p = arg0.getPoint();

        if (jPanelImmagine.isMarkerType()
                && moveMarker
                && zoomManager.isPointOnImage(p)) {

            // correggo le coordinate, quelle lette infatti sono in relazione
            // alla posizione in alto a sx del marker
            p.x = p.x + point.getPanelPosition_X() - Constants.DIAMETER / 2;
            p.y = p.y + point.getPanelPosition_Y() - Constants.DIAMETER / 2;


            if (floor.testNear(p, id))
                moveMarker = false;
            else {
                this.setCoordinates(p);
                cwjs.updateLocation(id, p.x, p.y);
                jpi.isValid();
            }
        }
        arg0.consume();
    }

}


    @Override
    public void mousePressed(MouseEvent arg0) {
        if (!stopped) {
            moveMarker = true;
            arg0.consume();
        } else {
            JPanelImmagine jpi = (JPanelImmagine) this.getParent();
            // MouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger)
            Point coord = this.getScaledMarkerPosition();
            MouseEvent me = new MouseEvent(arg0.getComponent(), arg0.getID(), arg0.getWhen(), arg0.getModifiers(), coord.x, coord.y, arg0.getClickCount(), false);
            jpi.mousePressed(me);
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

        if (!stopped) {
            moveMarker = false;
            arg0.consume();
        } else {
            JPanelImmagine jpi = (JPanelImmagine) this.getParent();
            Point p = arg0.getPoint();

            Point old_marker_s = zoom.getPanelPosition(new Point(x, y));

            // correggo le coordinate, quelle lette infatti sono in relazione
            // alla posizione in alto a sx del marker
            p.x = p.x + old_marker_s.x - Constants.DIAMETER / 2;
            p.y = p.y + old_marker_s.y - Constants.DIAMETER / 2;

            MouseEvent me = new MouseEvent(arg0.getComponent(), arg0.getID(), arg0.getWhen(), arg0.getModifiers(), p.x, p.y, arg0.getClickCount(), false);

            jpi.mouseReleased(me);
        }
    }


    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

}
