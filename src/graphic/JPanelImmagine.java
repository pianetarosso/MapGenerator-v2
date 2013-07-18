package graphic;

import common.Constants;
import main.CommunicationWithJS;
import objects.Floor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 15.57
 * To change this template use File | Settings | File Templates.
 */
public class JPanelImmagine extends MyJPanel implements MouseListener, MouseMotionListener {


    public JPanelImmagine(Floor[] floors, CommunicationWithJS cwjs, boolean debug) {

        super(floors, cwjs, debug);

        // aggiungo i listeners
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // rendo evidente il costruttore del listener per il ridimensionamento
    public void addResizeListener(Container cp) {
        zoom.changeSizeListener(cp);
    }

    // funzione **UNICA** per la gestione del movimento dell'immagine nei listener
    private void mouseMovement(MouseEvent arg0, String type) {

        switch (type) {

            case "mouseDragged": {

                // trascinamento dell'immagine
                if (zoom.isPointOnImage(arg0.getPoint()))
                    move.moveImage(arg0);
                break;
            }

            case "mousePressed": {

                // inizio del trascinamento
                zoom.enableZoom(false);
                if (zoom.isPointOnImage(arg0.getPoint()))
                    move.setOriginPoint(arg0);
                break;
            }

            case "mouseReleased": {

                // fine del trascinamento
                zoom.enableZoom(true);
                if (zoom.isPointOnImage(arg0.getPoint()))
                    move.setOriginPoint(null);
                break;
            }
        }

        arg0.consume();
    }


    private void pathListener(MouseEvent arg0, String type) {

        PathArrayList paths = selected_floor.paths;

        if (!zoom.isPointOnImage(arg0.getPoint()))
            return;

        switch (type) {

            case "mouseClicked": {

                // individuo la path più vicina cliccata
                Point point = arg0.getPoint();
                paths.drawingPath = null;
                paths.selectedPath = CustomPoint.findNearestPath(point, paths, zoom);

                if (paths.selectedPath != null) {
                    paths.cwjs.deletePath();
                    if (this.debug)
                        delete(0, "path");
                    else
                        this.stopAll(true);
                }
                this.updatePanel();
                break;
            }

            case "mousePressed": {

                // inizio il disegno di una path
                if (paths.drawingPath == null) {
                    paths.addPath(arg0.getPoint());
                    paths.selectedPath = null;
                }
                break;
            }

            case "mouseReleased": {

                // termino il disegno di una path
                if (paths.drawingPath != null)
                    paths.saveNewPath(arg0.getPoint());
                if (paths.cwjs.debug && (paths.size() > 5))
                    this.delete(0, "marker");
                break;
            }

            case "mouseDragged": {

                // continuo a disegnare una path
                if (paths.drawingPath != null)
                    paths.drawingPath(arg0.getPoint());
                else
                    paths.drawingPath = null;
                break;
            }
        }

        arg0.consume();
    }


    @Override
    public void mouseDragged(MouseEvent arg0) {

        if (type == Constants.TYPE_PATH)
            pathListener(arg0, "mouseDragged");
        else
            mouseMovement(arg0, "mouseDragged");
    }

    @Override
    public void mousePressed(MouseEvent arg0) {

        if (type == Constants.TYPE_PATH)
            pathListener(arg0, "mousePressed");
        else
            mouseMovement(arg0, "mousePressed");
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

        if (type == Constants.TYPE_PATH)
            pathListener(arg0, "mouseReleased");
        else
            mouseMovement(arg0, "mouseReleased");
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override

    public void mouseClicked(MouseEvent arg0) {

        if (type == Constants.TYPE_MARKER)
            MarkerListener(arg0);
        else if (type == Constants.TYPE_PATH)
            pathListener(arg0, "mouseClicked");
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

}