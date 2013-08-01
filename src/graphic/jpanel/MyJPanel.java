package graphic.jpanel;

import common.Constants;
import communication.ToJS;
import graphic.marker.Markers;
import graphic.path.Paths;
import objects.Floor;
import objects.Point;
import zoomManager.ZoomManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyJPanel extends JPanel implements Constants {

    private static final long serialVersionUID = 1L;

    public Floor getFloor() {
        return floor;
    }

    public Floor[] getFloors() {
        return floors;
    }

    public Markers getMarkers() {
        return markers;
    }

    public Paths getPaths() {
        return paths;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    final Markers markers;
    final Paths paths;
    final ArrayList<objects.Point> points;

    // piano selezionato
    Floor floor = null;
    private final Floor[] floors;

    private int counter = 0;

    // tipo di operazione che si effettua con il click (o trascinamento) del mouse
    private String type = "";
    private String temp_type;

    // variabili per la gestione dei zoom e spostamento
    final ZoomManager zoomManager;

    public final ToJS toJS;

    //////////////////////////////////////////////////////////////////////
    // METODI DI CLASSE e BASE
    // ///////////////////////////////////////////////////////////////////

    // COSTRUTTORE
    MyJPanel(Floor[] floors, ToJS toJS) {

        this.floors = floors;
        this.toJS = toJS;

        // elimino il layout, per impostare gli oggetti con le coordinate
        setLayout(null);

        // imposto il background
        setBackground(BACKGROUND);

        markers = new Markers();
        paths = new Paths(this);
        points = new ArrayList<>();

        // creo uno ZoomManager
        zoomManager = new ZoomManager(this, markers);
    }

    public int getId() {
        counter++;
        return counter;
    }

    // Disegno del JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (floor != null) {
            // disegno l'immagine scalata secondo il fattore di zoom
            g2.drawRenderedImage(floor.getImage(), zoomManager.scaleBufferedImage());

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            paths.draw(g2);

            updatePanel();
        }
    }

    // funzione per il redraw della finestra
    public void updatePanel() {
        this.revalidate();
        this.repaint();
    }

    // blocco movimento e creazione di punti, path, oltre allo zoom e al movimento
    public void stopAll(boolean stop) {

        zoomManager.disableZoom(stop);

        if (!stop)
            this.setDrawOperationType(temp_type);

        else {
            temp_type = type;
            type = TYPE_NONE;
        }
        //toJS.debug("Type: " + type);
    }


    // COMUNICAZIONE //////////////////////////////////////////////////////////////


    // Imposto una nuova immagine nel panel, e calcolo lo zoom per visualizzare
    // l'immagine a pieno schermo
    public void setSelectedFloor(int nf) {


        // trovo l'immagine corrispondente al piano
        for (Floor f : floors)
            if (f.getFloor() == nf) {
                if (this.floor != null)
                    zoomManager.resetZoom();

                this.floor = f;
                this.setDrawOperationType(type);
                markers.setVisible(floor);
                break;
            }

        // imposto lo zoomManager e il movimento
        zoomManager.setImage(floor.getImage());

        // aggiorno il panel
        updatePanel();


    }


    // imposto il "metodo" di disegno selezionato
    public void setDrawOperationType(String type) {

        if (type.contains(TYPE_MARKER)) {
            this.type = TYPE_MARKER;
            paths.reset();
        } else if (type.contains(TYPE_PATH)) {
            this.type = TYPE_PATH;
            markers.resetSelection();
        } else

            this.updatePanel();
    }


    public boolean isMarkerType() {
        return type.equals(TYPE_MARKER);
    }

    boolean isPathType() {
        return type.equals(TYPE_PATH);
    }

    public ZoomManager getZoomManager() {
        return zoomManager;
    }
}
