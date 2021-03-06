package communication;

import common.Constants;
import mapGenerator.MyJApplet;
import objects.Floor;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 15.37
 * To change this template use File | Settings | File Templates.
 */
// FUNZIONI PER IL CARICAMENTO ASINCRONO DELLE IMMAGINI, CON CONSEGUENTE ABILITAZIONE ////////////////
// DEI METODI DI INPUT DELLA PAGINA
public class LoadImages implements Runnable, Constants {

    private final ToJS toJS;
    private final Floor[] floors;
    private final MyJApplet myJApplet;

    public LoadImages(Floor[] floors, ToJS toJS, MyJApplet myJApplet) {

        this.floors = floors;
        this.toJS = toJS;
        this.myJApplet = myJApplet;

        Thread ct = Thread.currentThread();
        ct.setName("Master Thread");
        Thread t = new Thread(this, "Load Images");
        t.start();
    }

    @Override
    public void run() {
        try {

            for (Floor f : floors) {
                f.loadImage();
            }

            myJApplet.setFloor(floors[0].getFloor());
            myJApplet.setOperation(TYPE_MARKER);
            toJS.enableInput();
        } catch (IOException e) {
        }

    }

}