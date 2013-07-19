package graphic.path;

import objects.Point;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 19/07/13
 * Time: 12.37
 * To change this template use File | Settings | File Templates.
 */
public class Paths extends ArrayList<Path> {


    private Path drawingPath = null;
    private Path selectedPath = null;

    public Paths() {
        super();
    }

    // disegno delle path
    // aggiungere verifica che entrambi i punti appartengano al piano
    public void draw(Graphics2D g2) {

        if (drawingPath != null)
            drawingPath.draw(g2, drawingPath);

        for (Path p : this)
            if (!(p.isElevator() || p.isStair()))
                p.draw(g2, selectedPath);
    }

    public void setDrawingPath(Path drawingPath) {
        this.drawingPath = drawingPath;
    }

    public void updateDrawingPath(objects.Point a) {
        drawingPath.setA(a);
    }

    public void saveDrawingPath() {

        if (drawingPath.isLengthEnougth())
            this.add(drawingPath);

        drawingPath = null;
    }

    // scansionare tutte le path alla ricerca di quella che contenga questo punto
    // ricordarsi che ci si DEVE trovare in type Path

    public void setSelectedPath(objects.Point p) {

        if (p == null)
            selectedPath = null;
        else
            for (Path path : this)

                if (path.contains(p) != null) {
                    selectedPath = path;
                    break;
                }
    }


    public void addElevator(Point point, String identifier, int id) {

        addFloorConnection(point, true, identifier, id);
    }

    public void addStair(Point point, String identifier, int id) {

        addFloorConnection(point, false, identifier, id);
    }

    // metodo per aggiungere un nuovo ascensore/scala
    // si occupa anche di fare le "giunzioni" con i piani vicini
    private void addFloorConnection(objects.Point point, boolean elevator, String identifier, int id) {

        Path up = null;
        Path down = null;
        Path contains = null;

        for (Path path : this) {

            if ((path.isElevator() == elevator)
                    && (path.isStair() == !elevator)
                    && (path.getIdentifier() == identifier)) {


                // caso in cui c'è già una linea di ascensori/scale passante per il piano
                if (((path.getP().getFloor().getFloor() > point.getFloor().getFloor())
                        && (path.getA().getFloor().getFloor() < point.getFloor().getFloor()))
                        || ((path.getP().getFloor().getFloor() < point.getFloor().getFloor())
                        && (path.getA().getFloor().getFloor() > point.getFloor().getFloor()))) {

                    contains = path;
                    up = null;
                    down = null;

                    break;
                }

                if (path.getP().getFloor().getFloor() > point.getFloor().getFloor()) {

                    if (up == null)
                        up = path;
                    else {
                        int d1 = Math.min(path.getP().getFloor().getFloor(),
                                path.getA().getFloor().getFloor());
                        int d2 = Math.min(up.getP().getFloor().getFloor(),
                                up.getA().getFloor().getFloor());

                        if (d2 < d1)
                            up = path;
                    }
                } else {
                    if (down == null)
                        down = path;
                    else {
                        int d1 = Math.max(path.getP().getFloor().getFloor(),
                                path.getA().getFloor().getFloor());
                        int d2 = Math.max(up.getP().getFloor().getFloor(),
                                up.getA().getFloor().getFloor());

                        if (d2 > d1)
                            down = path;
                    }
                }

            }
        }

        Path new_p = new Path(point, id);

        if (elevator)
            new_p.setElevator(identifier);
        else
            new_p.setStair(identifier);


        // caso base, nessun altro ascensore/scala
        if ((up == null)
                && (down == null)
                && (contains == null))

            this.add(new_p);


        // caso in cui c'è una linea di ascensori/scale che attraversa il piano
        if (contains != null) {

            new_p.setA(contains.getA());
            contains.setA(point);

            this.add(new_p);
        }

        if (up != null) {

            if (up.getP().getFloor().getFloor() == up.getA().getFloor().getFloor())
                up.setA(point);

            else if (up.getP().getFloor().getFloor() < up.getA().getFloor().getFloor()) {
                new_p.setA(up.getP());
                this.add(new_p);
            } else {
                new_p.setA(up.getA());
                this.add(new_p);
            }
        }

        if (down != null) {

            if (down.getP().getFloor().getFloor() == down.getA().getFloor().getFloor())
                down.setA(point);

            else if (down.getP().getFloor().getFloor() > down.getA().getFloor().getFloor()) {
                new_p.setA(down.getP());
                this.add(new_p);
            } else {
                new_p.setA(down.getA());
                this.add(new_p);
            }
        }
    }

    public void deleteElevator(Point point) {
        deleteFloorConnection(point);
    }

    public void deleteStair(Point point) {
        deleteFloorConnection(point);
    }

    private void deleteFloorConnection(objects.Point point) {

        Path a = null;
        Path b = null;

        for (Path p : this) {

            if ((p.getP() == point) || (p.getA() == point)) {

                if (a == null)
                    a = p;
                else {
                    b = p;
                    break;
                }
            }
        }

        // unisco le due path in una
        if ((a != null) && (b != null)) {

            if (a.getP() != point) {

                if (b.getP() == point)
                    a.setA(b.getA());
                else
                    a.setA(b.getP());


            } else {

                if (b.getP() == point)
                    a.setP(b.getA());
                else
                    a.setP(b.getP());

            }

            this.remove(b);
        } else if (a != null) {

            if (a.getP() != point)
                a.setA(a.getP());
            else
                a.setP(a.getA());
        }
    }
}
