package containers;

import common.Helper;
import objects.Point;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 14.50
 * To change this template use File | Settings | File Templates.
 */
public class Points extends ArrayList<Point> {

    @Override
    public boolean add(Point point) {

        for (Point p : this) {

            if ((p.getFloor() == point.getFloor())
                    && (p.getX() == point.getX())
                    && (p.getY() == point.getY()))
                return false;
        }
        return super.add(point);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Point findNearest(int x, int y, int zoom) {

        for (Point p : this) {

            if (Helper.testDistance(x, y, p.getX(), p.getY(), zoom))
                return p;
        }
        return null;
    }
}
