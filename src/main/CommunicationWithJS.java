package main;

import common.Constants;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import objects.Floor;

import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;

public class CommunicationWithJS {


    private JSObject window = null;
    // funzione che chiamo per abilitare il debug in eclipse
    private boolean debug = true;

    public CommunicationWithJS(Applet applet, boolean debug) {
        if (!debug)
            this.window = JSObject.getWindow(applet);
        this.debug = debug;
    }

    /* Funzione per parsare il JSON proveniente dal JS

     */
    public Floor[] parseFloors(URL codebase) {

        Floor[] floors;

        if (debug) {

            floors = new Floor[2];

            try {
                floors[0] = new Floor(0, new URL(Constants.TEST_URL1), 23);
                floors[1] = new Floor(1, new URL(Constants.TEST_URL2), 43);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
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
        }
        return floors;
    }

    // abilito i campi di input della pagina
    public void enableInput() {
        if (debug)
            System.out.println("enableInput:" + true);
        else
            window.eval(Constants.ENABLE_JS_INPUT);
    }

}
