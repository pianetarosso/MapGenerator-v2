package common;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 18/07/13
 * Time: 12.33
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {


    // FUNZIONI JS //////////////////////////////////////////////////////////////////////////


    //recupero e parsing dei piani
    public static final String GET_FLOORS = "getFloors();";
    public static final String NUMERO_DI_PIANO = "numero_di_piano";
    public static final String LINK = "immagine";
    public static final String ID = "id";

    // abilito i campi del JS (DOPO il caricamento delle immagini)
    public static final String ENABLE_JS_INPUT = "enableInputs()";

    // comunico che ho creato un nuovo marker, affinchè l'utente possa inserire i dati
    public static final String NEW_MARKER = "createNewMarker()";

    // invio i dati di un marker per l'edit
    public static final String SEND_MARKER = "setMarkerData";

    // prompt cancellazione di una path
    public static final String SEND_PATH = "setPath()";


    public static final String UPDATE_POSITION = "updatePosition";

    public static final String DELETE_PATH = "deletePath()";
    public static final String IS_VALID = "isValid";

    public static final String DEBUG = "debug";


    // COSTANTI GRAFICHE  /////////////////////////////////////////////////////////////////
    public static final int MIN_MARKER_DISTANCE = 17;

    public static final int MIN_PATH_DISTANCE = 10;

    // incremento massimo dello zoom, a partire dallo zoom minimo
    public static final double MAX_ZOOM_INCREMENT = 1.0;

    // passo di incremento dello zoom
    public static final double ZOOM_STEP = 0.1;

    // colore dello sfondo
    public static final Color BACKGROUND = Color.black;

    // Sfondo
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

    // Colore bordo durante passaggio sopra con il mouse
    public static final Color SELECTED_COLOR = Color.green;

    // Colore bordo se il marker non è selezionato
    public static final Color NOT_SELECTED_COLOR = Color.gray;

    // colore del marker se la validatzione non è corretta
    public static final Color VALIDATED_COLOR = Color.green;

    // colore del marker se la validazione non è valida
    public static final Color NOT_VALIDATED_COLOR = Color.red;

    // valore di trasparenza da applicare quando trascinato
    public static final int ALPHA = 50;

    // diametro dell'oggetto
    public static final int DIAMETER = 10;
    public static final double MAX_M_DIFFERENCE = 0.1;

    public static final Color VALIDATED_PATH_COLOR = Color.green;
    public static final Color NOT_VALIDATED_PATH_COLOR = Color.red;
    public static final Color SELECTED_PATH_COLOR = Color.yellow;

    public static final int SPESSORE = 2;
    public static final int MINIMUM_PATH_LENGTH = 10;


    // EVENTI DEL MOUSE ///////////////////////////////////////////////////////////////////////
    public static final String mouseDragged = "mouseDragged";
    public static final String mousePressed = "mousePressed";
    public static final String mouseReleased = "mouseReleased";
    public static final String mouseClicked = "mouseClicked";


    //OPERAZIONI CONSENTITE ///////////////////////////////////////////////////////////////////
    public static final String TYPE_MARKER = "marker";
    public static final String TYPE_PATH = "path";
    public static final String TYPE_NONE = "none";

}
