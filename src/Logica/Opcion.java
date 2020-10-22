package Logica;

public class Opcion {
	private Integer valor;
	private Entidad_grafica entidad;
	
	public Opcion() {
		this.valor = null;
		this.entidad = new Entidad_grafica();
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
	
	public int getCantElementos() {
		return this.entidad.getImagenes().length;
	}
	
}
