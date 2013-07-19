package graphic.marker;

import graphic.ZoomManager;
import graphic.jpanel.JPanelImmagine;
import objects.Floor;


public class Marker extends MyMouseListeners {

    private static final long serialVersionUID = -2609466806655673458L;


    // COSTRUTTORI //
    public Marker(int x,
                  int y,
                  ZoomManager zoomManager,
                  int id,
                  Floor floor,
                  JPanelImmagine jPanelImmagine) {

        super(x, y, zoomManager, id, floor, jPanelImmagine);

        // abilito i listener per il trascinamento
        addMouseListener(this);
        addMouseMotionListener(this);
    }

}
