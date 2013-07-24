package objects;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.51
 * To change this template use File | Settings | File Templates.
 */
public class Path {

    protected Point p;
    protected Point a;

    public int getId() {
        return id;
    }

    protected int id;

    private boolean elevator;

    private boolean stair;

    private String identifier = "";


    public Path(Point p, int id) {
        this.p = p;
        this.a = p;
        this.id = id;
    }

    public Point getP() {
        return p;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public Point getA() {
        return a;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(String elevator) {
        this.identifier = elevator;
        this.elevator = true;
        this.stair = false;
    }

    public boolean isStair() {
        return stair;
    }

    public void setStair(String stair) {
        this.identifier = stair;
        this.stair = true;
        this.elevator = false;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return "Path Id: " + id + "; " +
                "Elevator: " + elevator + "; " +
                "Stair: " + stair + "; " +
                "Identifier: " + identifier + "; " +
                "P: " + p.toString() + "; " +
                "A: " + a.toString() + "; ";
    }
}
