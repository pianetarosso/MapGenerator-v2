package graphic.path;

import common.Constants;
import common.Helper;

import java.awt.*;
import java.awt.geom.Line2D;

public class Path extends objects.Path {


    // Una PATH può essere tirata da/verso un punto su di un'altra PATH o un MARKER

    // Quindi è necessario tenere conto di
    // 	coordinate generiche
    //	oggetti marker
    // 	oggeti path

    // Inoltre bisogna aggiungere metodi di controllo per verificare le CANCELLAZIONI
    // e la validazione


    // le PATH sono cosituite da 2 CUSTOMPOINT, questi sono in grado di gestire il processo di
    // scansione di marker e path per individuare i più vicini


    private boolean validated = false;


    // costruttore
    Path(objects.Point p, int id) {
        super(p, id);
    }

    // disegno la linea (per ora senza giochini di validazione riguardo al colore)
    // se la path in input e this sono uguali, vuol dire che è stata selezionata
    void draw(Graphics2D g2, Path path) {

        Line2D line = new Line2D.Double(p.getPanelPosition(), a.getPanelPosition());

        // disegno del bordo
        g2.setColor(Color.white);

        if (path != null)
            if (path == this)
                g2.setColor(Constants.SELECTED_PATH_COLOR);

        g2.setStroke(new BasicStroke(Constants.SPESSORE + 2));
        g2.draw(line);

        // disegno della linea
        if (validated)
            g2.setColor(Constants.VALIDATED_PATH_COLOR);
        else
            g2.setColor(Constants.NOT_VALIDATED_PATH_COLOR);
        g2.setStroke(new BasicStroke(Constants.SPESSORE));
        g2.draw(line);

        ///////////////////////////////////////////////////////////
    }

    // restituisco la lunghezza della path
    public boolean isLengthEnougth() {
        return Helper.testDistance(p, a, Constants.MINIMUM_PATH_LENGTH);
    }


    // cancello la validazione della path e dei suoi CustomPoint
    public void resetValidation() {
        p.resetValidation();
        a.resetValidation();
        validated = false;
    }


    // metodo per trovare se due path si trovano sulla stessa retta
    // restituisce "true" nel caso e modifica questa path
    public boolean join(Path path) {

        if (this.isElevator()
                || this.isStair()
                || path.isElevator()
                || path.isStair())
            return false;

        double m1 = calculateM();

        double m2 = path.calculateM();

        double difference = Math.abs(1 - (m1 / m2));

        if (difference <= Constants.MAX_M_DIFFERENCE) {

            if (p == path.getP())
                setP(path.getA());
            else if (p == path.getA())
                setP(path.getP());
            else if (a == path.getP())
                setA(path.getA());
            else if (a == path.getA())
                setA(path.getP());
            else {
                System.out.println("FATAL ERROR!\nJoin function error!");
                return false;
            }
            return true;
        }

        return false;
    }


    private double calculateM() {
        return (p.getPanelPosition_Y() - a.getPanelPosition_Y()) /
                (p.getPanelPosition_X() - a.getPanelPosition_X());
    }

    // metodo per "spezzare" questa path.
    //modifica il punto "di arrivo" o "di partenza" di questa
    // restituisce l'altra metà come nuova Path
    public Path cut(objects.Point point, int id) {

        Path new_p = new Path(point, id);
        new_p.setA(a);

        this.setA(point);

        return new_p;
    }

    // metodo utilizzato per verificare se si sta cliccando "vicino" ad una path
    // restituisce il punto sulla retta
    public objects.Point contains(objects.Point ps) {


        if (this.isElevator()
                || this.isStair()
                || (ps.getFloor() != p.getFloor()))
            return null;


        // costruisco l'equazione della retta Y = Mx + Z
        // caso particolare: ay == py => retta verticale!

        if (a.getPanelPosition_X() == p.getPanelPosition_X()) {

            int min = Math.min(a.getPanelPosition_Y(), p.getPanelPosition_Y());
            int max = Math.max(a.getPanelPosition_Y(), p.getPanelPosition_Y());

            if ((ps.getPanelPosition_Y() >= min)
                    && (ps.getPanelPosition_Y() <= max)
                    && (Math.abs(ps.getPanelPosition_X() - a.getPanelPosition_X()) <= Constants.MIN_PATH_DISTANCE)) {

                ps.setX(a.getPanelPosition_X());

                return ps;
            }

            return null;
        }

        double m = calculateM();

        double z = p.getPanelPosition_Y() - (m * p.getPanelPosition_X());

        int min = Math.min(a.getPanelPosition_X(), p.getPanelPosition_X());
        int max = Math.max(a.getPanelPosition_X(), p.getPanelPosition_X());


        for (; min <= max; min++) {

            int y = (int) ((m * min) + z);

            Point out = new Point(min, y);

            if (Helper.testDistance(
                    new Point(ps.getPanelPosition_X(), ps.getPanelPosition_Y()),
                    out,
                    Constants.MIN_PATH_DISTANCE)) {

                ps.setX(out.getX());
                ps.setY(out.getY());

                return ps;
            }
        }

        return null;
    }


    public String toString() {
        return p.toString() + "," + a.toString();
    }
}
