package communication;

import common.Constants;
import graphic.marker.Marker;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import objects.Floor;

import java.applet.Applet;
import java.net.URL;

public class WithJS {


    private JSObject window = null;


    public WithJS(Applet applet) {
        this.window = JSObject.getWindow(applet);

    }

    /* Funzione per parsare il JSON proveniente dal JS

     */
    public Floor[] parseFloors(URL codebase) {

        Floor[] floors;

        JSObject jsonFloors = (JSObject) window.eval(Constants.GET_FLOORS);

        int i = 0;

        for (; i < 50; i++)
            try {
                if (jsonFloors.getSlot(i) == null)
                    break;
            } catch (JSException jse) {
                break;
            }

        floors = new Floor[i];

        for (int t = 0; t < i; t++) {

            JSObject jsonFloor = (JSObject) jsonFloors.getSlot(t);

            try {
                int floor = Integer.parseInt(jsonFloor.getMember(Constants.NUMERO_DI_PIANO) + "");
                URL link = new URL(codebase, jsonFloor.getMember(Constants.LINK).toString());
                int id = Integer.parseInt(jsonFloor.getMember(Constants.ID).toString());

                floors[t] = new Floor(floor, link, id);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return floors;
    }

    // abilito i campi di input della pagina
    public void enableInput() {
        window.eval(Constants.ENABLE_JS_INPUT);
    }

    // comunico alla pagina di aprire i campi per l'introduzione dei dati di un nuovo marker
    public void newMarker() {
        window.eval(Constants.NEW_MARKER);
    }


    // invio i dati di un marker per l'edit
    public void sendMarker(Marker m) {

        // RFID

        Object[] out = new String[]{"", "", "", "", "", "", "", ""};

        out[0] = m.getPoint().getRFID();
        out[1] = m.getPoint().isAccess() + "";

        if (m.getStair() != null)
            out[2] = m.getStair();

        if (m.getElevator() != null)
            out[3] = m.getElevator();

        if (m.getRoom() != null) {
            out[4] = m.getRoom().getName();
            out[5] = m.getRoom().getPeople();
            out[6] = m.getRoom().getLink();
            out[7] = m.getRoom().getOther();
        }

        window.call(Constants.SEND_MARKER, out);
    }


    // prompt cancellazione di una path
    public void sendPath() {

        window.eval(Constants.SEND_PATH);
    }


}
