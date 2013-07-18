package graphic.marker;

import graphic.ZoomManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 16.58
 * To change this template use File | Settings | File Templates.
 */
public abstract class MyMouseListeners extends MyJComponent implements MouseListener, MouseMotionListener {


    public MyMouseListeners(int x, int y, ZoomManager zoom, int id, int floor) {
        super(x, y, zoom, id, floor);

        // abilito i mouselistener
        addMouseListener(this);
    }

    // ascoltatori del mouse
    @Override
    public void mouseEntered(MouseEvent arg0) {
        mouseEntered = true;
        arg0.consume();
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        mouseEntered = false;
        arg0.consume();
    }
}
