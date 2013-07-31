package communication;

import graphic.marker.Marker;
import objects.Floor;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 31/07/13
 * Time: 9.15
 * To change this template use File | Settings | File Templates.
 */
public interface ToJsSpecification {

    // Funzione per parsare il JSON proveniente dal JS
    public Floor[] parseFloors(URL codebase);

    // abilito i campi di input della pagina
    public void enableInput();

    // comunico alla pagina di aprire i campi per l'introduzione dei dati di un nuovo marker
    public void newMarker();

    // invio i dati di un marker per l'edit
    public void sendMarker(Marker m);

    // prompt cancellazione di una path
    public void sendPath();

    // invio stringhe di debug affinché siano visualizzate direttamente nella console
    // della pagina web
    public void debug(String value);
}
