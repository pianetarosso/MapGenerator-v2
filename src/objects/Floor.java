package objects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Floor {


    private int floor;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    private URL link;
    private int id;

    private BufferedImage image = null;


    public Floor(int floor, URL link, int id) {
        this.floor = floor;
        this.link = link;
        this.id = id;
    }

    public void loadImage() throws IOException {
        image = ImageIO.read(link);
    }

    public BufferedImage getImage() {
        return image;
    }

    public String toString() {
        return id + " "
                + floor + " "
                + link.toString() + " ";
    }


}
