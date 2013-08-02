package graphic.marker;

import graphic.jpanel.JPanelImmagine;
import graphic.path.Path;
import zoomManager.ZoomManager;

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
abstract class MyMouseListeners extends MyJComponent implements MouseListener, MouseMotionListener {

    private final ZoomManager zoomManager;
    private Marker marker;

    MyMouseListeners(
            objects.Point point,
            JPanelImmagine jPanelImmagine) {

        super(point, jPanelImmagine);

        this.zoomManager = point.getZoomManager();

    }

    void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType()) {

            jPanelImmagine.stopAll(true);
            jPanelImmagine.getMarkers().setSelected(marker);

            jPanelImmagine.toJS.sendMarker(jPanelImmagine.getMarkers().getSelected());

            jPanelImmagine.updatePanel();

        } else
            jPanelImmagine.mouseClicked(buildNewEvent(arg0, point.getPanelPosition_X(), point.getPanelPosition_Y()));

        arg0.consume();
    }


    // ascoltatori del mouse
    @Override
    public void mouseEntered(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType()) {
            mouseEntered = true;
            arg0.consume();

            this.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        if (jPanelImmagine.isMarkerType()) {
            mouseEntered = false;
            arg0.consume();

            this.repaint();
        }
    }

    // MOUSE CLICK LISTENER per visualizzare un marker o cancellarlo //////////////////////////////


    // MOUSE DRAGGED LISTENER
    // implementa il trascinamento dell'oggetto
    // gestisce anche le collisioni

    @Override
    public void mouseDragged(MouseEvent arg0) {

        Point p = calculateRelativePointPosition(arg0.getPoint());

        if (jPanelImmagine.isMarkerType() && moveMarker) {

            if (zoomManager.isPointOnImage(p)) {

                objects.Point tp = new objects.Point(jPanelImmagine.getId(), p.x, p.y, jPanelImmagine.getFloor(), zoomManager);

                for (objects.Point otp : jPanelImmagine.getPoints()) {
                    if (otp.isNear(tp) && (otp != point)) {
                        moveMarker = false;
                        break;
                    }
                }

                for (Path otp : jPanelImmagine.getPaths()) {

                    if ((otp.getP() != tp) && (otp.getA() != tp))
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
            jPanelImmagine.updatePanel();

        } else
            jPanelImmagine.mouseDragged(buildNewEvent(arg0, (int) p.getX(), (int) p.getY()));

        arg0.consume();
    }


    private Point calculateRelativePointPosition(Point p) {
        p.x += point.getPanelPosition_X() - DIAMETER / 2;
        p.y += point.getPanelPosition_Y() - DIAMETER / 2;

        return p;
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        if (jPanelImmagine.isMarkerType())
            moveMarker = true;

        else
            jPanelImmagine.mousePressed(buildNewEvent(arg0, point.getPanelPosition_X(), point.getPanelPosition_Y()));

        arg0.consume();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

        if (jPanelImmagine.isMarkerType())
            moveMarker = false;
        else {
            Point p = calculateRelativePointPosition(arg0.getPoint());
            jPanelImmagine.mouseReleased(buildNewEvent(arg0, (int) p.getX(), (int) p.getY()));
        }

        arg0.consume();

    }


    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    private MouseEvent buildNewEvent(MouseEvent arg0, int new_x, int new_y) {
        return new MouseEvent(arg0.getComponent(), arg0.getID(), arg0.getWhen(), arg0.getModifiers(), new_x, new_y, arg0.getClickCount(), arg0.isPopupTrigger());
    }
}
