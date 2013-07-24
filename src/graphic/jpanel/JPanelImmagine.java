package graphic.jpanel;

import common.Constants;
import communication.WithJS;
import graphic.marker.Marker;
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


    public JPanelImmagine(Floor[] floors, WithJS cwjs) {

        super(floors, cwjs);

        // aggiungo i listeners
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // rendo evidente il costruttore del listener per il ridimensionamento
    public void addResizeListener(Container cp) {
        zoomManager.changeSizeListener(cp);
    }

    private void pathListener(MouseEvent arg0, String type) {

        if (!zoomManager.isPointOnImage(arg0.getPoint()))
            return;


        if (type == Constants.mouseClicked) {

            // individuo la path più vicina cliccata
            paths.drawingPath = null;

            if (paths.setSelectedPath(createPoint(arg0))) {
                this.stopAll(true);
                withJS.sendPath();
                this.updatePanel();
            }

        } else if (type == Constants.mousePressed) {

            // inizio il disegno di una path
            if (paths.drawingPath == null) {
                paths.setSelectedPath(null);
                paths.setDrawingPath(createPoint(arg0), points);
            }

        } else if (type == Constants.mouseReleased) {

            // termino il disegno di una path
            if (paths.drawingPath != null)
                paths.saveDrawingPath(createPoint(arg0), points);

        } else if (type == Constants.mouseDragged) {

            // continuo a disegnare una path
            if (paths.drawingPath != null)
                paths.updateDrawingPath(createPoint(arg0));
            else
                paths.drawingPath = null;

        }


        arg0.consume();
    }


    @Override
    public void mouseDragged(MouseEvent arg0) {

        if (isPathType())
            pathListener(arg0, Constants.mouseDragged);

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

        if (isPathType())
            pathListener(arg0, Constants.mousePressed);

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

        if (isPathType())
            pathListener(arg0, Constants.mouseReleased);

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override

    public void mouseClicked(MouseEvent arg0) {

        if (isMarkerType())
            MarkerListener(arg0);
        else if (isPathType())
            pathListener(arg0, Constants.mouseClicked);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    private objects.Point createPoint(MouseEvent arg0) {
        return new objects.Point(getId(), arg0.getX(), arg0.getY(), floor, zoomManager);
    }

    public void deleteMarker() {

        // cancello il punto dai marker
        markers.delete(paths, points, this);
    }

    // funzione per la gestione del listener sui markers
    private void MarkerListener(MouseEvent arg0) {
        if (zoomManager.isPointOnImage(arg0.getPoint()) && (floor != null)) {


            objects.Point found = null;
            objects.Point tp = createPoint(arg0);

            // verifico se mi trovo "nei pressi" di un point già esistente o di una path
            for (objects.Point p : points) {

                if (p.isNear(tp)) {
                    found = p;
                    break;
                }
            }

            // nel caso esista già un marker in quella posizione
            // gli invio il "click" e termino
            if (found != null) {
                for (Marker m : markers) {
                    if (m.getPoint() == found) {
                        m.mouseClicked(arg0);
                        return;
                    }
                }
            }

            // se non ho ancora trovato nulla, verifico se si trova su qualche path
            if (found == null)
                found = paths.findPoint(tp);

            // se non è neppure su di una path... utilizzo il punto creato prima
            if (found == null)
                found = tp;

            // fermo TUTTO (zoom, movimento e listener)
            this.stopAll(true);

            // creo un nuovo marker
            Marker marker = new Marker(found, this);

            // aggiungo l'oggetto al JPanel principale
            this.add(marker);

            // imposto la posizione dell'oggetto sul JPanel
            marker.setBounds();

            marker.setVisible(true);
            marker.setEnabled(true);

            markers.setSelected(marker);

            this.updatePanel();

            // comunico al JS che ho creato un nuovo marker
            // e aspetto che restituisca i dati del marker
            withJS.newMarker();


        }
    }

}