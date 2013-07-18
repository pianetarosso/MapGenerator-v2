package Graphic;

import Static.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ZoomManager {

	// VARIABILI
	// ////////////////////////////////////////////////////////////////////////

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    // variabili che gestiscono lo zoom e lo zoom minimo dell'immagine
	private double zoom = 1.0;
	private double min_zoom = 1.0;

	// variabili di offset per il centraggio dell'immagine
	protected double offset_x = 0.0, offset_y = 0.0;

	// variabile per abilitare / disabilitare l'uso dello zoom
	// necessaria per evitare interferenze con gli "strumenti di azione"
	private boolean zoom_enabled = true;

	// immagine da gestire
	private BufferedImage image = null;

	// JPanel su cui è da abilitare lo zoom
	private JPanel imagebox;

	// dimensione dell'immagine
	private int image_w = 0, image_h = 0;

	// componente che controlla il movimento del JScrollPane
	private JViewport viewport;

	// METODI BASE
	// /////////////////////////////////////////////////////////////////////////

	// costruttore
	protected ZoomManager(JPanel imagebox) {
		setJPanelObject(imagebox);
	}

	// abilita / disabilita lo zoom
	protected void disableZoom(boolean disable) {
		this.zoom_enabled = !disable;
	}

	// imposto l'immagine
	protected void setImage(BufferedImage image) {
		this.image = image;
		image_w = image.getWidth();
		image_h = image.getHeight();
		this.viewport = (JViewport) imagebox.getParent();
		setInitialZoom();
	}

	// funzione per il calcolo dello zoom e il posizionamento centrato
	// dell'immagine
	// durante il DRAW
	protected AffineTransform scaleBufferedImage() {

		AffineTransform at = null;

		if (image != null) {

			double[] offset = calcolaOffset(zoom);

			at = AffineTransform.getTranslateInstance(offset[0], offset[1]);
			at.scale(zoom, zoom);
		}
		return at;
	}

	// funzione per ricavare la posizione reale sull'immagine di un punto
	public int getRealPosition_X(double x) {
		return (int) ((x - offset_x) / zoom);
	}

    // funzione per ricavare la posizione reale sull'immagine di un punto
    public int getRealPosition_Y(double y) {
        return (int) ((y - offset_y) / zoom);
    }

	// funzione per ottenere, partendo da un punto sull'immagine, la sua
	// posizione nell'imagebox
	public int getPanelPosition_X(double x) {
		return (int) (x * zoom + offset_x);
	}

    public int getPanelPosition_Y(double y) {
        return (int) (y * zoom + offset_y);
    }

	// funzione per il calcolo dell'offset per tenere l'immagine posizionata
	// al centro del JPanel
	protected double[] calcolaOffset(double zoom) {

		double[] offset = new double[2];

		int w = imagebox.getWidth();
		int h = imagebox.getHeight();

		offset_x = (w - zoom * image_w) / 2;
		offset_y = (h - zoom * image_h) / 2;

		offset[0] = offset_x;
		offset[1] = offset_y;

		return offset;
	}

	// funzione che verifica se il punto indicato è sull'immagine
	public boolean isPointOnImage(Point p) {

		boolean test = true;

		test = test && (p.getX() >= (int) offset_x);
		test = test && (p.getY() >= (int) offset_y);

		test = test && (p.getX() <= (int) (offset_x + zoom * image_w));
		test = test && (p.getY() <= (int) (offset_y + zoom * image_h));

		return test;
	}

	// LISTENER E VARI
	// ////////////////////////////////////////////////////////////////////////

	// imposto il JPanel dove lavoro
	private void setJPanelObject(JPanel imagebox) {
		this.imagebox = imagebox;
		setMouseWheelListener();
	}

	// listener all'oggetto superiore per verificare se è avvenuta una modifica
	// alla dimensione
	// della finestra
	protected void changeSizeListener(Container up) {
		up.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				if (image != null)
					setInitialZoom();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	// Implemento il listener della rotella del mouse per lo zoom
	private void setMouseWheelListener() {
		imagebox.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				Point p = arg0.getPoint();
				boolean movement = (1 == arg0.getWheelRotation());
				if (zoom_enabled && (image != null))
					setZoom(p, movement);
			}
		});
	}

	// FUNZIONI DI CALCOLO
	// ////////////////////////////////////////////////////////////////////////////

	// funzione che calcola e imposta lo zoom minimo dell'immagine,
	// viene chiamata SOLO per:
	// # il caricamento dell'immagine
	// # al ridimensionamento del pannello JScollPane
	private void setInitialZoom() {

		int jpane_w = viewport.getWidth();
		int jpane_h = viewport.getHeight();

		double zoom_w = (double) jpane_w / (double) image_w;
		double zoom_h = (double) jpane_h / (double) image_h;

		zoom = Math.min(zoom_w, zoom_h);
		min_zoom = zoom;

		// questo passo è necessario per gestire in modo corretto un eventuale
		// ridimensionamento del box esterno, durante un'operazione di zoom
		Point p = new Point(jpane_w / 2, jpane_h / 2);
		setZoom(p, zoom);
	}

	// funzione per impostare lo zoom
	private void setZoom(Point p, boolean movement) {

		if (isPointOnImage(p)) {
			double new_zoom;

			if (movement) // sto ingrandendo
				new_zoom = zoom + Constants.ZOOM_STEP;
			else
				// sto diminuendo
				new_zoom = zoom - Constants.ZOOM_STEP;

			setZoom(p, new_zoom);
		}
	}

	// funzione di base per impostare lo zoom
	private void setZoom(Point p, double new_zoom) {
		double max_zoom = min_zoom + Constants.MAX_ZOOM_INCREMENT;
		if ((new_zoom >= min_zoom) && (new_zoom <= max_zoom)) {
			double old_zoom = zoom;
			zoom = new_zoom;
			imagebox.setPreferredSize(new Dimension((int) (image_w * zoom),
					(int) (image_h * zoom)));
			mantainZoomedPosition(p, old_zoom);
		}
	}

	// funzione per mantenere lo zoom dove si trova il mouse
	private void mantainZoomedPosition(Point p, double old_zoom) {

		// recupero la dimensione della vista
		Dimension d = viewport.getSize();

		// calcolo gli offset in base allo zoom
		double[] old_offset = calcolaOffset(old_zoom);
		double[] new_offset = calcolaOffset(zoom);

		// calcolo la posizione del nuovo punto di riferimento (in alto a sx)
		// tenendo come centro il punto passato, eliminando l'offset
		int vp_x = (int) (p.x - old_offset[0] - d.getWidth() / 2);
		int vp_y = (int) (p.y - old_offset[1] - d.getHeight() / 2);

		if (vp_x < 0)
			vp_x = 0;
		if (vp_y < 0)
			vp_y = 0;

		// creo un nuovo punto con le coordinate trovate
		Point vp = new Point(vp_x, vp_y);

		// calcolo la posizione del punto con il nuovo livello di zoom
		// considerando anche l'eventuale offset
		double new_position_x = (vp.x * zoom / old_zoom) + new_offset[0];
		double new_position_y = (vp.y * zoom / old_zoom) + new_offset[1];

		// calcolo la differenza tra i due punti
		int offset_old_new_x = (int) (vp.x - new_position_x);
		int offset_old_new_y = (int) (vp.y - new_position_y);

		// sposto il punto nelle nuove coordinate
		vp.translate(offset_old_new_x, offset_old_new_y);

		// sposto l'area visibile e faccio un update della vista
		movePosition(new Rectangle(vp, viewport.getSize()));
	}

	// sposto la posizione dell'immagine e aggiorno la vista
	private void movePosition(Rectangle r) {

		// sposto l'area visibile
		imagebox.scrollRectToVisible(r);

		// faccio un update della vista
		imagebox.revalidate();
		imagebox.repaint();
	}

}
