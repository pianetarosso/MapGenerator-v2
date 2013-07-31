package mapGenerator;


import com.google.gson.Gson;
import graphic.marker.Marker;
import objects.Floor;
import objects.Path;
import objects.Point;
import objects.Room;

import java.util.ArrayList;
import java.util.HashMap;


public class MapGenerator extends MyJApplet {


    // NUOVO MARKER /////////////////////////////////////////////////////////////
    @Override
    public void dismissNewMarker() {

        jPanelImmagine.getMarkers().dismiss(jPanelImmagine);
        jPanelImmagine.stopAll(false);
        jPanelImmagine.updatePanel();
    }

    @Override
    public void saveNewMarker(
            String RFID,
            boolean access,
            String elevator,
            String stair,
            String room_name,
            String room_people,
            String room_link,
            String room_other) {

        Room room = null;

        if (room_name.length() > 0) {
            room = new Room(room_name, jPanelImmagine.getId());
            room.setOther(room_other);
            room.setLink(room_link);
            room.setPeople(room_people);
        }

        if (elevator.length() <= 0)
            elevator = null;

        if (stair.length() <= 0)
            stair = null;

        jPanelImmagine.getMarkers().save(
                RFID,
                access,
                room,
                elevator,
                stair,
                jPanelImmagine.getPaths(),
                jPanelImmagine.getPoints());

        jPanelImmagine.stopAll(false);
        jPanelImmagine.updatePanel();


        toJS.debug(jPanelImmagine.getMarkers().toString());

        debug();

    }

    private void debug() {

        for (Path p : jPanelImmagine.getPaths())
            toJS.debug(p.toString());

        for (Marker m : jPanelImmagine.getMarkers())
            toJS.debug(m.toString());

        for (Point p : jPanelImmagine.getPoints())
            toJS.debug(p.toString());
    }
    ///////////////////////////////////////////////////////////////////////////////////


    // EDIT & DELETE DI UN MARKER ESISTENTE ///////////////////////////////////////////


    @Override
    public void editMarker(
            String RFID,
            boolean access,
            String elevator,
            String stair,
            String room_name,
            String room_people,
            String room_link,
            String room_other) {


        Point point = jPanelImmagine.getMarkers().getSelected().getPoint();

        deleteMarker();

        Marker marker = new Marker(point, jPanelImmagine);

        jPanelImmagine.getMarkers().setSelected(marker);

        saveNewMarker(
                RFID,
                access,
                elevator,
                stair,
                room_name,
                room_people,
                room_link,
                room_other);
    }


    @Override
    public void deleteMarker() {
        jPanelImmagine.deleteMarker();
        jPanelImmagine.stopAll(false);
    }


    @Override
    public String getRoomNames() {

        ArrayList<Room> rooms = new ArrayList<>();

        for (Marker m : jPanelImmagine.getMarkers()) {
            if (m.getRoom() != null)
                rooms.add(m.getRoom());
        }

        String[] out = new String[rooms.size()];

        for (int i = 0; i < rooms.size(); i++)
            out[i] = rooms.get(i).getName();

        Gson gson = new Gson();
        return gson.toJson(out);
    }


    @Override
    public String getRFIDs() {

        int counter = 0;

        for (Point p : jPanelImmagine.getPoints())
            if (p.getRFID() != null)
                counter++;

        String[] out = new String[counter];

        int i = 0;

        for (Point p : jPanelImmagine.getPoints())
            if (p.getRFID() != null) {
                out[i] = p.getRFID();
                i++;
            }

        Gson gson = new Gson();
        return gson.toJson(out);

    }


    @Override
    public String getLifts() {

        return getLiftOrStairs(true);
    }

    @Override
    public String getLiftOrStairs(boolean isLift) {

        Floor[] floors = jPanelImmagine.getFloors();

        HashMap<Floor, ArrayList<Marker>> map = new HashMap<>();

        for (Floor f : floors)
            map.put(f, new ArrayList<Marker>());

        for (Marker m : jPanelImmagine.getMarkers())
            if (((isLift && (m.getElevator() != null)) ||
                    (!isLift && (m.getStair() != null))))
                map.get(m.getPoint().getFloor()).add(m);


        Object[] out = new Object[floors.length];

        for (int v = 0; v < floors.length; v++) {

            ArrayList<Marker> m = map.get(floors[v]);

            String[] list = new String[m.size()];

            for (int i = 0; i < m.size(); i++)
                if (isLift)
                    list[i] = m.get(i).getElevator();
                else
                    list[i] = m.get(i).getStair();

            out[v] = list;

        }

        Gson gson = new Gson();
        return gson.toJson(out);
    }


    @Override
    public String getStairs() {

        return getLiftOrStairs(false);
    }

}
