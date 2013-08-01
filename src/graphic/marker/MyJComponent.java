package graphic.marker;

import common.Constants;
import graphic.jpanel.JPanelImmagine;
import objects.Point;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

abstract class MyJComponent extends JComponent implements Constants {

    private static final long serialVersionUID = -1934599507737914205L;


    // variabile per sapere se sull'oggetto è passato il mouse o meno
    boolean mouseEntered = false;

    // variabile per il trascinamento del marker
    boolean moveMarker = false;

    public boolean isClicked() {
        return clicked;
    }

    // variabile per il "click"

    public void setClicked(boolean clicked) {
        this.clicked = clicked;

        if (!clicked)
            mouseEntered = false;

        this.repaint();
    }

    private boolean clicked = false;

    final JPanelImmagine jPanelImmagine;

    final Point point;


    // COSTRUTTORE //
    MyJComponent(Point point,
                 JPanelImmagine jPanelImmagine) {

        super();

        this.point = point;

        this.jPanelImmagine = jPanelImmagine;

        // abilito i metodi di input
        enableInputMethods(true);

        // imposto la dimensione predefinita
        setPreferredSize(new Dimension(DIAMETER, DIAMETER));

        // imposto il colore di background
        setBackground(TRANSPARENT_COLOR);

        // elimino il bordo predefinito
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }


    // funzione per impostare la POSIZIONE SULL'IMMAGINE
    public void setBounds() {

        setBounds(point.getPanelPosition_X() - DIAMETER / 2,
                point.getPanelPosition_Y() - DIAMETER / 2,
                DIAMETER, DIAMETER);
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

        // aggiorno la posizione dell'oggetto sulla mappa
        this.setBounds();

        super.paintComponent(g);


        // abilito l'anti-aliasing
        Graphics2D antiAlias = (Graphics2D) g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        antiAlias.setStroke(new BasicStroke(1));


        Color designedColor = NOT_VALIDATED_COLOR;

        if (point.isValid())
            designedColor = VALIDATED_COLOR;

        g.setColor(designedColor);

        // disegno il cerchio della dimensione predisposta
        if (moveMarker && jPanelImmagine.isMarkerType()) {

            // "costuisco" un colore uguale a quello designato, ma più trasparente
            Color trans = new Color(
                    designedColor.getRed(),
                    designedColor.getGreen(),
                    designedColor.getBlue(),
                    ALPHA);

            g.setColor(trans);
        }

        // disegno il marker
        g.fillOval(0, 0, DIAMETER, DIAMETER);


        // gestisco la selezione
        if (clicked || (mouseEntered && jPanelImmagine.isMarkerType()))
            g.setColor(SELECTED_COLOR);
        else
            g.setColor(NOT_SELECTED_COLOR);

        g.drawOval(0, 0, DIAMETER - 1, DIAMETER - 1);

        this.repaint();
    }

    public Point getPoint() {
        return point;
    }
}
