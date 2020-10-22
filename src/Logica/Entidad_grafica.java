package Logica;

import javax.swing.ImageIcon;

public class Entidad_grafica {
	protected ImageIcon grafico;
	protected String[] imagenes;

	public Entidad_grafica() {
		grafico = new ImageIcon();
		imagenes = new String[] { "/img/bola0.png", "/img/bola1.png", "/img/bola2.png", "/img/bola3.png",
				"/img/bola4.png", "/img/bola5.png", "/img/bola6.png", "/img/bola7.png", "/img/bola8.png",
				"/img/bola9.png"};

	}

	public void actualizar(int indice) {
		if (indice < this.imagenes.length) {
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(imagenes[indice]));
			this.grafico.setImage(imageIcon.getImage());
		}
	}

	public ImageIcon getGrafico() {
		return this.grafico;
	}

	public void setGrafico(ImageIcon grafico) {
		this.grafico = grafico;
	}

	public String[] getImagenes() {
		return this.imagenes;
	}

	public void setImagenes(String[] imagenes) {
		this.imagenes = imagenes;
	}
}
