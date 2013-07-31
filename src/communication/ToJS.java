package communication;

import common.Constants;
import graphic.marker.Marker;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import objects.Floor;

import java.applet.Applet;
import java.net.URL;

public class ToJS implements ToJsSpecification, Constants {


    private JSObject window = null;
    private static ToJS toJS = null;


    private ToJS(Applet applet) {
        this.window = JSObject.getWindow(applet);
    }

    public static ToJS getToJS(Applet applet) {
        if (toJS == null)
            toJS = new ToJS(applet);

        return toJS;
    }

    @Override
    public Floor[] parseFloors(URL codebase) {

        Floor[] floors;

        JSObject jsonFloors = (JSObject) window.eval(GET_FLOORS);

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
                int floor = Integer.parseInt(jsonFloor.getMember(NUMERO_DI_PIANO) + "");
                URL link = new URL(codebase, jsonFloor.getMember(LINK).toString());
                int id = Integer.parseInt(jsonFloor.getMember(ID).toString());

                floors[t] = new Floor(floor, link, id);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return floors;
    }

    @Override
    public void enableInput() {
        window.eval(ENABLE_JS_INPUT);
    }

    @Override
    public void newMarker() {
        window.eval(NEW_MARKER);
    }


    @Override
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

        window.call(SEND_MARKER, out);
    }


    @Override
    public void sendPath() {
        window.eval(SEND_PATH);
    }

    @Override
    public void debug(String value) {

        Object[] out = {value};

        window.call(DEBUG, out);
    }

}
