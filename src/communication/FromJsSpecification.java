package communication;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 31/07/13
 * Time: 8.31
 * To change this template use File | Settings | File Templates.
 */
public interface FromJsSpecification {

    // FUNZIONE PER IMPOSTARE IL PIANO
    public void setFloor(int floor);

    // IMPOSTO IL TIPO DI OPERAZIONE (MARKER, PATH O NONE)
    @SuppressWarnings("UnusedDeclaration")
    public void setOperation(String type);

    // NUOVO MARKER /////////////////////////////////////////////////////////////
    @SuppressWarnings("UnusedDeclaration")
    public void dismissNewMarker();

    @SuppressWarnings("UnusedDeclaration")
    public void saveNewMarker(
            String RFID,
            boolean access,
            String elevator,
            String stair,
            String room_name,
            String room_people,
            String room_link,
            String room_other);

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
            String room_other);

    @SuppressWarnings("UnusedDeclaration")
    public void deleteMarker();


    // restituisco la lista di nomi usati nelle stanze
    @SuppressWarnings("UnusedDeclaration")
    public String getRoomNames();


    // restituisco la lista di codici RFID
    @SuppressWarnings("UnusedDeclaration")
    public String getRFIDs();


    // restituisco la lista degli ascensori divisi per piano
    @SuppressWarnings("UnusedDeclaration")
    public String getLifts();

    String getLiftOrStairs(boolean isLift);


    // restituisco la lista delle scale divise per piano
    @SuppressWarnings("UnusedDeclaration")
    public String getStairs();
}
