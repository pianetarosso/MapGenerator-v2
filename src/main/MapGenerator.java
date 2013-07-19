package main;


import communication.LoadImages;
import communication.WithJS;
import graphic.jpanel.JPanelImmagine;
import objects.Floor;

import javax.swing.*;
import java.awt.*;


public class MapGenerator extends JApplet {

    private static final long serialVersionUID = 1674700860875805629L;
    // STRUTTURE GRAFICHE: ///////////////////////////////////////////////////
    // Sfondo principale
    private JPanel fondo = new JPanel();
    // Sfondo centrale
    private JScrollPane scrollImage;
    private JPanelImmagine immagine;
    /////////////////////////////////////////////////////////////////////////
    // VARIABILI

    private Floor[] floors;
    private WithJS cwjs;
    private boolean debug = false;

    public void start() {

        cwjs = new WithJS(this, debug);
        floors = cwjs.parseFloors(this.getCodeBase());

        new LoadImages(floors, cwjs, this);

        // imposto il layout del contenitore principale
        fondo.setLayout(new BorderLayout());

        // immagine (centro)
        scrollImage = buildImagePanel();

        fondo.add(scrollImage, BorderLayout.CENTER);

        this.add(fondo);

        Toolkit kit = this.getToolkit();
        Dimension dim = kit.getScreenSize();

        this.setBounds(dim.width / 4, dim.height / 4, dim.width / 4, dim.height / 4);
        this.setVisible(true);
        this.repaint();
    }

    // Creo il pannello di disegno dell'immagine
    private JScrollPane buildImagePanel() {

        immagine = new JPanelImmagine(floors, cwjs, debug);

        scrollImage = new JScrollPane(immagine,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //scrollImage.setPreferredSize(new Dimension(200, 200));
        scrollImage.setBackground(Color.BLACK);
        immagine.addResizeListener(scrollImage);

        return scrollImage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // FUNZIONI DI INTERAZIONE CON IL JS DELLA PAGINA


    // SET DEI PIANI E DEGLI OGGETTI DA DISEGNARE//////////////////////////////////////////////

    // FUNZIONE PER IMPOSTARE IL PIANO
    public void setFloor(int floor) {
        immagine.setSelectedFloor(floor);
        immagine.resetSelections();
    }

    // IMPOSTO IL TIPO DI OPERAZIONE (MARKER, PATH O NONE)
    @SuppressWarnings("UnusedDeclaration")
    public void setOperation(String type) {
        immagine.setDrawOperationType(type);
        immagine.resetSelections();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void operationComplete(boolean saved, int id, String type, boolean access) {

        if (saved)
            immagine.validateMarker(id, access);
        else
            immagine.delete(id, type);


        immagine.stopAll(false);
        immagine.resetSelections();
    }
}
