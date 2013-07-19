package graphic.jpanel;

import common.Constants;
import communication.WithJS;
import graphic.ZoomManager;
import objects.Floor;

import javax.swing.*;
import java.awt.*;

public class MyJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public Floor getFloor() {
        return floor;
    }

    // piano selezionato
    private Floor floor = null;
    public Floor[] floors;

    private boolean debug = false;

    // tipo di operazione che si effettua con il click (o trascinamento) del mouse
    protected String type = "";
    private String temp_type;

    // variabili per la gestione dei zoom e spostamento
    public ZoomManager zoomManager;

    //////////////////////////////////////////////////////////////////////
    // METODI DI CLASSE e BASE
    // ///////////////////////////////////////////////////////////////////

    // COSTRUTTORE
    public MyJPanel(Floor[] floors, WithJS withJS, boolean debug) {

        this.floors = floors;
        this.debug = debug;

        // elimino il layout, per impostare gli oggetti con le coordinate
        setLayout(null);

        // imposto il background
        setBackground(Constants.BACKGROUND);

        // creo uno ZoomManager
        zoomManager = new ZoomManager(this);

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

            floor.paths.draw(g2);

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
            this.setDrawOperationType("None");
        }
    }


    // COMUNICAZIONE //////////////////////////////////////////////////////////////


    // Imposto una nuova immagine nel panel, e calcolo lo zoom per visualizzare
    // l'immagine a pieno schermo
    public void setSelectedFloor(int nf) {


        // trovo l'immagine corrispondente al piano
        for (Floor f : floors)
            if (f.getFloor() == nf) {
                this.floor = f;
                this.setDrawOperationType(type);
                break;
            }

        // imposto lo zoomManager e il movimento
        zoomManager.setImage(floor.getImage());

        // aggiorno il panel
        updatePanel();

        if (debug)
            setDrawOperationType(Constants.TYPE_MARKER);
    }


    // imposto il "metodo" di disegno selezionato
    public void setDrawOperationType(String type) {

        if (type.contains(Constants.TYPE_MARKER))
            this.type = Constants.TYPE_MARKER;

        if (type.contains(Constants.TYPE_PATH))
            this.type = Constants.TYPE_PATH;

        this.updatePanel();
    }


    // Cancellazione di un marker o path (funzione chiamata dal JS)
    public void delete(int id, String type) {

        if (type.contains("marker"))
            floor.markers.deleteMarker(id);
        else
            floor.paths.delete();

        floor.paths.validate();
    }

    /////////////////////////////////////////////////////////////////////////////////////


    public boolean isMarkerType() {
        return type == Constants.TYPE_MARKER;
    }

    public boolean isPathType() {
        return type == Constants.TYPE_PATH;
    }
}
