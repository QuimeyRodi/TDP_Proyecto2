package Logica;

public class Celda {
	private Integer valor;
	private Entidad_grafica entidad;
	private int fila;
	private int columna;
	private int cuadrante;
	private boolean habilitada;
	private boolean tiene_error;

	public Celda() {
		this.valor = null;
		this.entidad = new Entidad_grafica();
		fila = columna = cuadrante = 0;
		habilitada = true;
		tiene_error = false;
	}

	public void actualizar(int valor_nuevo) {
		if (valor_nuevo < this.getCantElementos()) {
			valor= valor_nuevo;
		}
		entidad.actualizar(valor);
	}

	public void set_error(boolean error) {
		tiene_error = error;
	}
	
	public boolean tiene_error() {
		return tiene_error;
	}
	public int getCantElementos() {
		return this.entidad.getImagenes().length;
	}

	public void set_cuadrante(int cuad) {
		cuadrante = cuad;
	}

	public int get_cuadrante() {
		return cuadrante;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		if (valor != null && valor < this.getCantElementos()) {
			this.valor = valor;
			entidad.actualizar(this.valor);
		} else {
			this.valor = null;
		}
	}

	public Entidad_grafica getEntidad_grafica() {
		return this.entidad;
	}

	public void setGrafica(Entidad_grafica g) {
		this.entidad = g;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public boolean esta_habilitada() {
		return habilitada;
	}

	public void set_habilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}
}
