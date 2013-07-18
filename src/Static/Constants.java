package Static;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.33
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    public static final String GET_FLOORS = "getFloors();";
    public static final String NUMERO_DI_PIANO = "numero_di_piano";
    public static final String LINK = "immagine";
    public static final String BEARING = "bearing";
    public static final String ID = "id";

    public static final String NEW_MARKER = "createNewMarker";
    public static final String EDIT_MARKER = "editMarker";

    public static final String ENABLE_JS_INPUT = "enableInputs()";
    public static final String UPDATE_POSITION = "updatePosition";

    public static final String DELETE_PATH = "deletePath()";
    public static final String IS_VALID = "isValid";

    public static final int MIN_DISTANCE = 17;

    // incremento massimo dello zoom, a partire dallo zoom minimo
    public static final double MAX_ZOOM_INCREMENT = 1.0;

    // passo di incremento dello zoom
    public static final double ZOOM_STEP = 0.1;

    // colore dello sfondo
    public static final Color BACKGROUND = Color.black;




    // COLORI //////////////////////////////////////////////////////////////

    // Sfondo
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

    // Colore bordo durante passaggio sopra con il mouse
    public static final Color SELECTED_COLOR = Color.white;

    // Colore bordo se il marker non è selezionato
    public static final Color NOT_SELECTED_COLOR = Color.black;

    // colore del marker se la validatzione non è corretta
    public static final Color VALIDATED_COLOR = Color.green;

    // colore del marker se la validazione non è valida
    public static final Color NOT_VALIDATED_COLOR = Color.red;

    // valore di trasparenza da applicare quando trascinato
    public static final int ALPHA = 50;

    /////////////////////////////////////////////////////////////////////////////


    // DIMENSIONI //////////////////////////////////////////////////////////////

    // diametro dell'oggetto
    public static final int DIAMETER = 10;



    ////////////////////////////////////////////////////////////////////////////

    public static final String TYPE_MARKER = "marker";
    public static final String TYPE_PATH = "path";

    public static final String TEST_URL1 = "http://127.0.0.1:8000/media/floors/IMG_20111009_172117_3.jpg";
    public static final String TEST_URL2 = "http://127.0.0.1:8000/media/floors/IMG_20111009_171138_9.jpg";
}
