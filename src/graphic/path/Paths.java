package graphic.path;

import graphic.jpanel.MyJPanel;
import graphic.marker.Marker;
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


    private final MyJPanel myJPanel;
    public Path drawingPath = null;
    private Path selectedPath = null;

    public Paths(MyJPanel myJPanel) {
        super();
        this.myJPanel = myJPanel;
    }

    // disegno delle path
    // aggiungere verifica che entrambi i punti appartengano al piano
    public void draw(Graphics2D g2) {

        if (drawingPath != null)
            drawingPath.draw(g2, drawingPath);

        for (Path p : this) {
            //   if (!(p.isElevator() || p.isStair()))
            p.draw(g2, selectedPath);
        }
    }

    public void setDrawingPath(Point p, ArrayList<Point> points) {

        Point found = null;
        for (Point tp : points)
            if (tp.isNear(p)) {
                found = tp;
                break;
            }

        if (found == null)
            found = findPoint(p);

        if (found != null)
            this.drawingPath = new Path(found, myJPanel.getId());
        else
            this.drawingPath = new Path(p, myJPanel.getId());
    }

    public void updateDrawingPath(objects.Point a) {
        drawingPath.setA(a);
    }

    public void saveDrawingPath(Point a, ArrayList<Point> points) {

        Point found = null;

        for (Point tp : points)
            if (tp.isNear(a)) {
                found = tp;
                break;
            }

        if (found == null)
            found = findPoint(a);

        if (found != null)
            drawingPath.setA(found);
        else
            drawingPath.setA(a);

        myJPanel.toJS.debug("" + drawingPath.isLengthEnougth());

        if (drawingPath.isLengthEnougth()) {
            // verifico che non ci siano incroci
            testCross(drawingPath, points);
            myJPanel.toJS.debug(this.size() + "");
        }

        drawingPath = null;
    }

    void testCross(Path path, ArrayList<Point> points) {

        int min = Math.min(path.getA().getPanelPosition_Y(), path.getP().getPanelPosition_Y());
        int max = Math.max(path.getA().getPanelPosition_Y(), path.getP().getPanelPosition_Y());

        ArrayList<Path> found = new ArrayList<>();
        ArrayList<Point> points_found = new ArrayList<>();

        // verifico se l'equazione della retta è verticale
        if (path.getP().getPanelPosition_X() == path.getA().getPanelPosition_X()) {


            for (Path tp : this) {

                for (int y = min; y <= max; y++) {

                    Point p = new Point(myJPanel.getId(), path.getP().getPanelPosition_X(), y, myJPanel.getFloor(), myJPanel.getZoomManager());

                    Point point_found = tp.contains(p);

                    if (point_found != null) {
                        found.add(tp);
                        points_found.add(point_found);
                    }
                }
            }

        } else {
            // equazione Y = mX + z

            double m = (path.getP().getPanelPosition_Y() - path.getA().getPanelPosition_Y()) /
                    (path.getP().getPanelPosition_X() - path.getA().getPanelPosition_X());

            double z = path.getP().getPanelPosition_Y() - (m * path.getP().getPanelPosition_X());

            min = Math.min(path.getA().getPanelPosition_X(), path.getP().getPanelPosition_X());
            max = Math.max(path.getA().getPanelPosition_X(), path.getP().getPanelPosition_X());


            for (Path tp : this) {

                for (int x = min; x <= max; x++) {

                    int y = (int) (m * x + z);

                    Point p = new Point(myJPanel.getId(), x, y, myJPanel.getFloor(), myJPanel.getZoomManager());

                    Point point_found = tp.contains(p);

                    if (point_found != null) {
                        found.add(tp);
                        points_found.add(point_found);
                    }
                }
            }

        }

        // caso base, non interseca nessuna path
        if (found.size() == 0) {
            myJPanel.toJS.debug("0");
            if (!points.contains(path.getP()))
                points.add(path.getP());

            if (!points.contains(path.getA()))
                points.add(path.getA());

            this.add(path);
        } else {

            // la nostra path originale verrà spezzata in tanti segmenti,
            // ma non c'è modo di sapere su quale di questi segmenti si troverà la
            // prossima intersezione. non c'è un ordinamento.
            ArrayList<Path> new_paths = new ArrayList<>();
            new_paths.add(path);

            // scansiono l'array delle path trovate, le spezzo e le salvo insieme ai nuovi punti
            for (int i = 0; i < found.size(); i++) {

                Path f = found.get(i);
                Point fp = points_found.get(i);

                // spezzo la Path che ho trovato, e la salvo
                Point fA = f.getA();
                f.setA(fp);

                Path f1 = new Path(fp, myJPanel.getId());
                f1.setA(fA);

                this.add(f1);
                points.add(fp);


                // ora scansiono l'array delle nuove path da spezzare, quando trovo una che contiene
                // il punto fp, la spezzo e aggiungo la nuova path

                for (Path np : new_paths) {

                    if (np.contains(fp) != null) {

                        Point nA = np.getA();
                        np.setA(fp);

                        Path nnp = new Path(fp, myJPanel.getId());
                        nnp.setA(nA);

                        new_paths.add(nnp);

                        break;
                    }
                }

                // ora provvedo a salvare tutte le "nuove path"

                for (Path np : new_paths)
                    this.add(np);
            }
        }

    }

    // scansionare tutte le path alla ricerca di quella che contenga questo punto
    // ricordarsi che ci si DEVE trovare in type Path
    public boolean setSelectedPath(objects.Point p) {

        if (p == null)
            selectedPath = null;
        else
            for (Path path : this)

                if (path.contains(p) != null) {
                    selectedPath = path;
                    return true;
                }

        return false;
    }

    public void addNewMarker(Marker marker) {

        if (marker.getElevator() != null)
            addElevator(marker.getPoint(), marker.getElevator());

        else if (marker.getStair() != null)
            addStair(marker.getPoint(), marker.getElevator());

        else if (setSelectedPath(marker.getPoint())) {

            Point a = selectedPath.getA();
            selectedPath.setA(marker.getPoint());

            Path new_p = new Path(marker.getPoint(), myJPanel.getId());
            new_p.setA(a);

            this.add(new_p);

            selectedPath = null;
        }
    }

    public Point findPoint(Point tp) {

        for (Path path : this) {

            Point found = path.contains(tp);

            if (found != null)
                return found;
        }
        return null;
    }

    public boolean isPointUsed(Point tp) {

        for (Path path : this) {

            if ((path.getP() == tp) || (path.getA() == tp))
                return true;
        }
        return false;
    }

    void addElevator(Point point, String identifier) {

        addFloorConnection(point, true, identifier);
    }

    void addStair(Point point, String identifier) {

        addFloorConnection(point, false, identifier);
    }

    // metodo per aggiungere un nuovo ascensore/scala
    // si occupa anche di fare le "giunzioni" con i piani vicini
    private void addFloorConnection(objects.Point point, boolean elevator, String identifier) {

        Path up = null;
        Path down = null;
        Path contains = null;

        for (Path path : this) {

            if ((path.isElevator() == elevator)
                    && (path.isStair() == !elevator)
                    && (path.getIdentifier().equals(identifier))) {


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

        Path new_p = new Path(point, myJPanel.getId());

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

    // cancello un marker che connetteva degli ascensori o delle scale
    public void deleteFloorConnection(objects.Point point) {

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

    // cancello una path.
    // restituisco i due POINT per verificare se sono utilizzati
    // o se li posso cancellare tranquillamente (restituisco SOLO quelli che non
    // appartengono a nessun'altra path)
    public Point[] delete() {

        Point[] out = {selectedPath.getP(), selectedPath.getA()};

        this.remove(selectedPath);
        selectedPath = null;

        for (Path path : this) {

            if (out[1] != null)
                if ((out[1] == path.getP()) || (out[1] == path.getA()))
                    out[1] = null;

            if (out[0] != null)
                if ((out[0] == path.getP()) || (out[0] == path.getA()))
                    out[0] = null;

            if ((out[1] == null) && (out[0] == null))
                break;
        }

        return out;
    }

    // resetto le impostazioni del drawing e della selezione
    public void reset() {
        setSelectedPath(null);
        drawingPath = null;
    }
}
