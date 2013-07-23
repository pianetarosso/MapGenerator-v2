package main;


import graphic.marker.Marker;
import objects.Point;
import objects.Room;


public class MapGenerator extends MyJApplet {


    // NUOVO MARKER /////////////////////////////////////////////////////////////
    @SuppressWarnings("UnusedDeclaration")
    public void dismissNewMarker() {

        jPanelImmagine.markers.dismiss(jPanelImmagine);
        jPanelImmagine.stopAll(false);
        jPanelImmagine.updatePanel();
    }

    @SuppressWarnings("UnusedDeclaration")
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

        jPanelImmagine.markers.save(
                RFID,
                access,
                room,
                elevator,
                stair,
                jPanelImmagine.paths,
                jPanelImmagine.points);

        jPanelImmagine.stopAll(false);
        jPanelImmagine.updatePanel();

    }

    ///////////////////////////////////////////////////////////////////////////////////


    // EDIT & DELETE DI UN MARKER ESISTENTE ///////////////////////////////////////////


    @SuppressWarnings("UnusedDeclaration")
    public void editMarker(
            String RFID,
            boolean access,
            String elevator,
            String stair,
            String room_name,
            String room_people,
            String room_link,
            String room_other) {


        Point point = jPanelImmagine.markers.getSelected().getPoint();

        deleteMarker();

        Marker marker = new Marker(point, jPanelImmagine);

        jPanelImmagine.markers.setSelected(marker);

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


    @SuppressWarnings("UnusedDeclaration")
    public void deleteMarker() {
        jPanelImmagine.deleteMarker();
    }

}
