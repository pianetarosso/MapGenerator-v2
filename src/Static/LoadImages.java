package Static;

import Objects.Floor;
import main.CommunicationWithJS;
import main.MapGenerator;

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
public class LoadImages implements Runnable {

    private final CommunicationWithJS cwjs;
    private Floor[] floors;
    private final MapGenerator mg;

    public LoadImages(Floor[] floors, CommunicationWithJS cwjs, MapGenerator mg) {

        this.floors = floors;
        this.cwjs = cwjs;
        this.mg = mg;

        Thread ct = Thread.currentThread();
        ct.setName("Master Thread");
        Thread t = new Thread(this, "Load Images");
        t.start();
    }

    @Override
    public void run() {
        try {

            for (int i= 0; i < floors.length; i++) {
                Floor f = floors[i];
                f.loadImage();
            }

            mg.setFloor(floors[0].getFloor());
            mg.setOperation(Constants.TYPE_MARKER);
            cwjs.enableInput();
        } catch (IOException e) {}

    }

}