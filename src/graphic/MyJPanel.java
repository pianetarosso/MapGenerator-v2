package graphic;

import common.Constants;
import main.CommunicationWithJS;
import objects.Floor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MyJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // piano selezionato
    private Floor selected_floor = null;
    public Floor[] floors;


    private boolean debug = false;

    // tipo di operazione che si effettua con il click (o trascinamento) del mouse
    protected String type = "";
    private String temp_type;

    // variabili per la gestione dei zoom e spostamento
    public ZoomManager zoom;

    //////////////////////////////////////////////////////////////////////
    // METODI DI CLASSE e BASE
    // ///////////////////////////////////////////////////////////////////

    // COSTRUTTORE
    public MyJPanel(Floor[] floors, CommunicationWithJS cwjs, boolean debug) {

        this.floors = floors;
        this.debug = debug;

        // elimino il layout, per impostare gli oggetti con le coordinate
        setLayout(null);

        // imposto il background
        setBackground(Constants.BACKGROUND);

        // creo uno ZoomManager
        zoom = new ZoomManager(this);


        for (Floor f : floors)
            f.initializePathsAndMarkers(this, zoom, cwjs);
    }


    // Disegno del JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (selected_floor != null) {
            // disegno l'immagine scalata secondo il fattore di zoom
            g2.drawRenderedImage(selected_floor.getImage(), zoom.scaleBufferedImage());

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            selected_floor.paths.draw(g2);

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

        zoom.disableZoom(stop);

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
    public void setSelectedFloor(int floor) {


        // trovo l'immagine corrispondente al piano
        for (Floor f : floors)
            if (f.getFloor() == floor) {
                this.selected_floor = f;
                this.setDrawOperationType(type);
                break;
            }

        // imposto lo zoom e il movimento
        zoom.setImage(selected_floor.getImage());

        // imposto la visibilità dei markers a seconda di quale piano viene selezionato
        for (Floor f : floors)
            f.setVisibleMarkers(selected_floor.numero_di_piano);

        // aggiorno il panel
        updatePanel();

        if (debug)
            setDrawOperationType(TYPE_MARKER);
    }


    // imposto il "metodo" di disegno selezionato
    public void setDrawOperationType(String type) {

        if (type.contains(Constants.TYPE_MARKER)) {
            this.type = Constants.TYPE_MARKER;
            selected_floor.markers.stopAllMarkers(false);
        } else (type.contains(Constants.TYPE_PATH)) {
            this.type = Constants.TYPE_PATH;
            selected_floor.markers.stopAllMarkers(true);
        }


        this.updatePanel();
    }


    // Cancellazione di un marker o path (funzione chiamata dal JS)
    public void delete(int id, String type) {

        if (type.contains("marker"))
            selected_floor.markers.deleteMarker(id);
        else
            selected_floor.paths.delete();

        selected_floor.paths.validate();
    }

    /////////////////////////////////////////////////////////////////////////////////////


    // funzione per la gestione del listener sui markers
    private void MarkerListener(MouseEvent arg0) {
        if (zoom.isPointOnImage(arg0.getPoint()) && (selected_floor != null)) {

            MarkerMap markers = selected_floor.markers;
            Marker new_m = markers.addMarker(arg0.getPoint());

            if (new_m != null) {

                // fermo TUTTO (zoom, movimento e listener)
                if (!markers.cwjs.debug)
                    this.stopAll(true);

                // aggiungo l'oggetto al JPanel principale
                this.add(new_m);

                // imposto la posizione dell'oggetto sul JPanel
                new_m.setBounds();

                new_m.setVisible(true);
                new_m.setEnabled(true);

                markers.setMarkerSelected(new_m.id);

                this.updatePanel();

                markers.cwjs.sendNewMarker(new_m, selected_floor.numero_di_piano);

                if (this.debug && (markers.size() > 4)) {
                    this.setDrawOperationType(TYPE_PATH);
                    markers.get(0).valido = true;
                }
            }
        }
    }
}
