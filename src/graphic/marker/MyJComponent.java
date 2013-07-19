package graphic.marker;

import common.Constants;
import graphic.ZoomManager;
import graphic.jpanel.JPanelImmagine;
import objects.Floor;
import objects.Point;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class MyJComponent extends JComponent {

    private static final long serialVersionUID = -1934599507737914205L;


    // variabile per sapere se sull'oggetto è passato il mouse o meno
    protected boolean mouseEntered = false;

    // variabile per il trascinamento del marker
    protected boolean moveMarker = false;

    // variabile per il "click"
    protected boolean clicked = false;

    protected JPanelImmagine jPanelImmagine;

    protected Point point;


    // COSTRUTTORE //
    protected MyJComponent(double x,
                           double y,
                           ZoomManager zoomManager,
                           int id,
                           Floor floor,
                           JPanelImmagine jPanelImmagine) {

        super();

        point = new Point(id, x, y, floor, zoomManager);

        this.jPanelImmagine = jPanelImmagine;

        // abilito i metodi di input
        enableInputMethods(true);

        // imposto la dimensione predefinita
        setPreferredSize(new Dimension(Constants.DIAMETER, Constants.DIAMETER));

        // imposto il colore di background
        setBackground(Constants.TRANSPARENT_COLOR);

        // elimino il bordo predefinito
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }


    // funzione per impostare la POSIZIONE SULL'IMMAGINE
    public void setBounds() {

        setBounds(point.getPanelPosition_X() - Constants.DIAMETER / 2,
                point.getPanelPosition_Y() - Constants.DIAMETER / 2,
                Constants.DIAMETER, Constants.DIAMETER);
    }

    // METODI VARI //

    public void setCoordinates(double x, double y) {
        point.setX(x);
        point.setY(y);

        this.repaint();
    }

    // funzione per disegnare l'oggetto
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (point.getFloor() != jPanelImmagine.getFloor()) {
            this.setVisible(false);
            this.setEnabled(false);
            return;
        }

        this.setVisible(true);
        this.setEnabled(true);

        // abilito l'anti-aliasing
        Graphics2D antiAlias = (Graphics2D) g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        antiAlias.setStroke(new BasicStroke(1));


        Color designedColor = Constants.NOT_VALIDATED_COLOR;

        if (point.isValid())
            designedColor = Constants.VALIDATED_COLOR;

        g.setColor(designedColor);

        // disegno il cerchio della dimensione predisposta
        if (moveMarker && jPanelImmagine.isMarkerType()) {

            // "costuisco" un colore uguale a quello designato, ma più trasparente
            Color trans = new Color(
                    designedColor.getRed(),
                    designedColor.getGreen(),
                    designedColor.getBlue(),
                    Constants.ALPHA);

            g.setColor(trans);
        }

        // disegno il marker
        g.fillOval(0, 0, Constants.DIAMETER, Constants.DIAMETER);


        // gestisco la selezione
        if ((clicked || mouseEntered) && jPanelImmagine.isMarkerType())
            g.setColor(Constants.SELECTED_COLOR);
        else
            g.setColor(Constants.NOT_SELECTED_COLOR);
        g.drawOval(0, 0, Constants.DIAMETER - 1, Constants.DIAMETER - 1);

        // aggiorno la posizione dell'oggetto sulla mappa
        this.setBounds();

        this.repaint();
    }


    public Point getPoint() {
        return point;
    }
}
