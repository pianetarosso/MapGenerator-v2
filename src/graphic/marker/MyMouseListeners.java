package graphic.marker;

import common.Constants;
import graphic.ZoomManager;
import graphic.jpanel.JPanelImmagine;
import graphic.path.Path;

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
            objects.Point point,
            JPanelImmagine jPanelImmagine) {

        super(point, jPanelImmagine);

        this.zoomManager = point.getZoomManager();
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
            jPanelImmagine.markers.setSelected((Marker) arg0.getSource());
            arg0.consume();

            // inviare i dati del marker fuori per l'editing
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

            objects.Point tp = new objects.Point(0, p.x, p.y, jPanelImmagine.getFloor(), zoomManager);

            for (objects.Point otp : jPanelImmagine.points) {
                if (otp.isNear(tp)) {
                    moveMarker = false;
                    break;
                }
            }

            for (Path otp : jPanelImmagine.paths) {

                if (otp.contains(tp) != null) {
                    moveMarker = false;
                    break;
                }
            }

            if (moveMarker) {
                point.setX(p.getX());
                point.setY(p.getY());
            }
        }
        arg0.consume();
    }


    @Override
    public void mousePressed(MouseEvent arg0) {
        if (jPanelImmagine.isMarkerType()) {
            moveMarker = true;
            arg0.consume();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType()) {
            moveMarker = false;

            // aggiungere controllo per le path nel caso si sovrappongano dopo lo spostamento del marker (funzione cross)
            arg0.consume();
        }
    }


    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

}
