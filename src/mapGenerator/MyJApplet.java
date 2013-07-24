package mapGenerator;

import communication.LoadImages;
import communication.WithJS;
import graphic.jpanel.JPanelImmagine;
import objects.Floor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: marco
 * Date: 22/07/13
 * Time: 15.44
 * To change this template use File | Settings | File Templates.
 */
public class MyJApplet extends JApplet {

    private static final long serialVersionUID = 1674700860875805629L;


    // Sfondo principale
    private JPanel fondo = new JPanel();

    // Sfondo centrale
    private JScrollPane scrollImage;
    protected JPanelImmagine jPanelImmagine;


    // VARIABILI

    private Floor[] floors;
    protected WithJS withJS;

    public void start() {

        withJS = new WithJS(this);
        floors = withJS.parseFloors(this.getCodeBase());

        new LoadImages(floors, withJS, this);

        // imposto il layout del contenitore principale
        fondo.setLayout(new BorderLayout());

        // jPanelImmagine (centro)
        scrollImage = buildImagePanel();

        fondo.add(scrollImage, BorderLayout.CENTER);

        this.add(fondo);

        Toolkit kit = this.getToolkit();
        Dimension dim = kit.getScreenSize();

        this.setBounds(dim.width / 4, dim.height / 4, dim.width / 4, dim.height / 4);
        this.setVisible(true);
        this.repaint();
    }

    // Creo il pannello di disegno dell'jPanelImmagine
    private JScrollPane buildImagePanel() {

        jPanelImmagine = new JPanelImmagine(floors, withJS);

        scrollImage = new JScrollPane(jPanelImmagine,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //scrollImage.setPreferredSize(new Dimension(200, 200));
        scrollImage.setBackground(Color.BLACK);
        jPanelImmagine.addResizeListener(scrollImage);

        return scrollImage;
    }


    // COMUNICAZIONE DI BASE ////////////////////////////////////////////////

    // FUNZIONE PER IMPOSTARE IL PIANO
    public void setFloor(int floor) {
        jPanelImmagine.setSelectedFloor(floor);
    }

    // IMPOSTO IL TIPO DI OPERAZIONE (MARKER, PATH O NONE)
    @SuppressWarnings("UnusedDeclaration")
    public void setOperation(String type) {
        jPanelImmagine.setDrawOperationType(type);
    }

}
