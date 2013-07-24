package objects;

import graphic.ZoomManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Floor {


    private int floor;

    private ArrayList<Point> points;

    private URL link;
    private int id;

    private BufferedImage image = null;


    public Floor(int floor, URL link, int id) {
        this.floor = floor;
        this.link = link;
        this.id = id;

        points = new ArrayList<Point>();
    }

    public void loadImage() throws IOException {
        image = ImageIO.read(link);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getFloor() {
        return floor;
    }


    public Point addPoint(double x, double y, ZoomManager zoomManager, int counter) {

        Point p = new Point(counter, x, y, this, zoomManager);

        for (Point point : points)
            if (p.isNear(point))
                return point;


        points.add(p);

        return p;
    }


    public String toString() {
        return "Floor Id: " + id + "; " +
                "Floor: " + floor + "; " +
                "Link: " + link.toString() + "; ";
    }
}
