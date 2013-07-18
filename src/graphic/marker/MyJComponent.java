package graphic.marker;

import common.Constants;
import common.Helper;
import graphic.ZoomManager;
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

    // variabile che abilita il click e il trascinamento di un marker
    protected boolean stopped = false;

    protected Point point;


    // VALIDAZIONE ///////////////////////////////////////////////////////

    // è valido SEMPRE => E' un ingresso
    public boolean valido = false;

    // valore acquisito con la connessione di più path
    public boolean validated = false;

    ///////////////////////////////////////////////////////////////////////


    // COSTRUTTORE //
    protected MyJComponent(double x, double y, ZoomManager zoomManager, int id, int floor) {

        super();

        point = new Point(id, x, y, floor, zoomManager);

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

        setBounds(point.getPanelPosition_X() - Constants.DIAMETER / 2, point.getPanelPosition_Y() - Constants.DIAMETER / 2,
                Constants.DIAMETER, Constants.DIAMETER);
    }

    // METODI VARI //


    // testo se i punti sono troppo vicini tra loro (nel contesto scalato, NON reale)
    public boolean testNear(Point p) {

        // se TRUE sono troppo vicini
        return Helper.testDistance(point.getPanelPosition_X(), point.getPanelPosition_Y(),
                p.getPanelPosition_X(), p.getPanelPosition_Y());
    }

    public void setCoordinates(double x, double y) {
        point.setX(x);
        point.setY(y);

        this.repaint();
    }

    // funzione per disegnare l'oggetto
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // abilito l'anti-aliasing
        Graphics2D antiAlias = (Graphics2D) g;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        antiAlias.setStroke(new BasicStroke(1));


        Color designedColor = Constants.NOT_VALIDATED_COLOR;

        if (valido || validated)
            designedColor = Constants.VALIDATED_COLOR;

        g.setColor(designedColor);

        // disegno il cerchio della dimensione predisposta
        if (moveMarker && !stopped) {

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
        if (clicked || (mouseEntered && !stopped))
            g.setColor(Constants.SELECTED_COLOR);
        else
            g.setColor(Constants.NOT_SELECTED_COLOR);
        g.drawOval(0, 0, Constants.DIAMETER - 1, Constants.DIAMETER - 1);

        // aggiorno la posizione dell'oggetto sulla mappa
        this.setBounds();

        this.repaint();
    }


}
