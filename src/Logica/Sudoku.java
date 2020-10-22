package Logica;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

/**
 * Clase que modela la parte logica del juego Sudoku. El juego consiste en
 * completar una serie de 9 paneles que a su vez se encuentran divididos en 9
 * celdas con números del 1 al 9.
 * 
 * @author Quimey Rodi
 *
 */
public class Sudoku {
	private Celda[][] tablero;
	private Opcion[] opciones;
	private int[][] cont_archivo;
	private Celda celda_activada;
	private boolean hay_error;
	private int filas;
	private LinkedList<Celda> errores;

	/**
	 * Inicializa el tablero del juego
	 * 
	 * @param ruta
	 * @throws FileException
	 */
	public Sudoku(String ruta) throws FileException {
		filas = 9;
		tablero = new Celda[filas][filas];
		cont_archivo = new int[filas][filas];
		celda_activada = null;
		hay_error = false;
		errores = new LinkedList<Celda>();
		boolean archivo_valido = controlar_archivo(ruta);
		if (archivo_valido) {
			for (int i = 0; i < filas; i++) {
				for (int j = 0; j < filas; j++) {
					Celda celda = new Celda();
					tablero[i][j] = celda;
					Random random = new Random();
					// int valor = random.nextInt(2);
					int valor = random.nextInt(8);
					if (valor != 0) {
						celda.actualizar(cont_archivo[i][j]);
						celda.set_habilitada(false);
					} else
						celda.setValor(0);
					celda.setColumna(j);
					celda.setFila(i);
				}
			}

			cargar_cuadrantes();

			opciones = new Opcion[10];
			for (int f = 0; f < 10; f++) {
				opciones[f] = new Opcion();
				opciones[f].setValor(f);
			}
		} else
			throw new FileException("Formato invalido del archivo");

	}

	/**
	 * Retorna la cantidad de filas del tablero
	 * 
	 * @return cantidad de filas
	 */
	public int get_filas() {
		return filas;
	}

	/**
	 * 
	 * @return
	 */
	public boolean gane() {
		boolean gane = true;
		if (!hay_error) {
			for (int fila = 0; fila < filas && gane; fila++) {
				for (int col = 0; col < filas && gane; col++) {
					if (tablero[fila][col].getValor() == 0 || tablero[fila][col].tiene_error()) {
						gane = false;
					}
				}
			}
		} else
			gane = false;
		return gane;
	}

	/**
	 * Retorna true si hay error en el tablero, false en caso contrario
	 * 
	 * @return
	 */
	public boolean hay_error() {
		return hay_error;
	}

	/**
	 * 
	 * @param fila
	 * @param columna
	 */
	public void activar_celda(int fila, int columna) {
		celda_activada = tablero[fila][columna];
	}

	/**
	 * Retona la celda activada del tablero
	 * 
	 * @return
	 */
	public Celda get_celda_activada() {
		Celda retorno = null;
		if (celda_activada != null) {
			retorno = celda_activada;
		}
		return retorno;
	}

	/**
	 * Acciona una celda, cambiando su valor por el de la opcion recibida
	 * 
	 * @param c      Celda
	 * @param opcion Opcion
	 */
	public void accionar(Celda c, Opcion opcion) {
		int valor = opcion.getValor();
		c.actualizar(valor);
	}

	/**
	 * Retorna la opcion en la posicion i parametrizada
	 * 
	 * @param i
	 * @return
	 */
	public Opcion getOpcion(int i) {
		return opciones[i];
	}

	/**
	 * Retorna la celda en la posicion i,j parametrizada
	 * 
	 * @param i fila
	 * @param j columna
	 * @return Celda
	 */
	public Celda getCelda(int i, int j) {
		return tablero[i][j];
	}

	/**
	 * 
	 * @param ruta
	 * @return
	 */
	private boolean controlar_archivo(String ruta) {
		boolean es_valido = true;
		BufferedReader puntero = null;
		String arr[];
		int col = 0, fil = 0;
		int digito;
		try {
			// if (archivo.exists()) {

			InputStream in = Sudoku.class.getClassLoader().getResourceAsStream(ruta);
			InputStreamReader inr = new InputStreamReader(in);
			puntero = new BufferedReader(inr);

			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).

			// Lectura del fichero
			String linea;
			while ((linea = puntero.readLine()) != null && fil < cont_archivo.length) {
				// System.out.println(linea);
				arr = linea.split(" ");
				if (arr.length == filas) {
					for (col = 0; col < arr.length; col++) {
						if (es_numero(arr[col])) {
							digito = Integer.parseInt(arr[col]);
							if (digito > 0 && digito <= 9) {
								cont_archivo[fil][col] = digito;
							} else {
								es_valido = false;
							}
						} else {
							es_valido = false;
						}
					}
					fil++;
				} else {
					es_valido = false;
				}

			}
			recorrer_matriz();
			if (fil >= cont_archivo.length && linea != null) // <-- tiene filas de mas
				es_valido = false;
			else {
				System.out.println("-----------contenido aca----------");
				recorrer_matriz();
				for (int i = 0; i < filas && es_valido; i++) {
					if (es_valido)
						es_valido = controlar_filas_arch(i);
					if (!es_valido)
						System.out.println("-----------se rompe fila----------");
				}
				for (int u = 0; u < filas && es_valido; u++) {
					if (es_valido)
						es_valido = controlar_columnas_arch(u);
					if (!es_valido)
						System.out.println("-----------se rompe col----------");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
		System.out.println("-----------" + es_valido + "---------");
		return es_valido;

	}

	private void recorrer_matriz() {// <---- borrar
		for (int f = 0; f < filas; f++) {
			for (int c = 0; c < filas; c++) {
				System.out.print(cont_archivo[f][c] + " ");
			}
			System.out.println();
		}
	}

	private void recorrer_matriz_aux() {// <---- borrar
		for (int f = 0; f < filas; f++) {
			for (int c = 0; c < filas; c++) {
				System.out.print(tablero[f][c].get_cuadrante() + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Marca a la celda parametrizada estableciendo si tiene o no error. Dependiendo
	 * del valor booleano recibido
	 * 
	 * @param celda
	 * @param estado
	 */
	private void marcar_celda(Celda celda, boolean estado) {
		celda.set_error(estado);
	}

	/**
	 * Elimina los errores del tablero
	 * 
	 * @param index_actual
	 */
	private void eliminar_errores_anteriores(int index_actual) {
		Celda celda_actual;
		if (index_actual < errores.size()) {
			celda_actual = errores.get(index_actual);
			celda_actual.set_error(false);
			eliminar_errores_anteriores(index_actual + 1);
			errores.remove(index_actual);
		}

	}

	/**
	 * 
	 * @param op
	 * @param cel
	 */
	public void controlar_tablero(Opcion op, Celda cel) {
		int comp = op.getValor();
		System.out.println("cuadrante " + cel.get_cuadrante());
		Celda control_cuad = controlar_cuadrante_tab(cel, comp);
		Celda control_fila = controlar_fila_tab(cel, comp);
		Celda control_col = controlar_columna_tab(cel, comp);
		hay_error = !(control_cuad == null && control_fila == null && control_col == null);
		System.out.println("valor de hay_error-> " + hay_error);
		System.out.println("valor de columna-> " + (control_col != null));
		System.out.println("valor de fila-> " + (control_fila != null));
		System.out.println("valor de cuadrante-> " + (control_cuad != null));
		if (!errores.isEmpty()) {
			eliminar_errores_anteriores(0);
		}
		if (hay_error) {
			if (control_cuad != null) {
				marcar_celda(control_cuad, true);
				errores.add(control_cuad);
			}
			if (control_fila != null) {
				marcar_celda(control_fila, true);
				errores.add(control_fila);
			}
			if (control_col != null) {
				marcar_celda(control_col, true);
				errores.add(control_col);
			}
			marcar_celda(cel, true);
			// errores.add(cel);
		} else {
			eliminar_errores_anteriores(0);
			marcar_celda(cel, false);
		}
	}

	/**
	 * 
	 * @param cel
	 * @param componente
	 * @return
	 */
	private Celda controlar_fila_tab(Celda cel, int componente) {
		Celda celda_error = null;
		int fila = cel.getFila();
		Celda actual;
		if (componente != 0) {
			for (int col = 0; col < tablero.length && celda_error == null; col++) {
				actual = tablero[fila][col];
				if (actual != cel && actual.getValor() == componente)
					celda_error = actual;
			}
		}
		if (celda_error != null) {
			System.out.println("----->error en fila " + fila);
			System.out.println("fila  " + celda_error.getFila());
			System.out.println("col  " + celda_error.getColumna());

		} else
			System.out.println("----->no hay error en fila " + fila);

		return celda_error;
	}

	private Celda controlar_columna_tab(Celda cel, int componente) {
		Celda celda_error = null;
		Celda actual;
		int columna = cel.getColumna();
		if (componente != 0) {
			for (int fila = 0; fila < tablero.length && celda_error == null; fila++) {
				actual = tablero[fila][columna];
				if (actual != cel && actual.getValor() == componente) {
					celda_error = actual;
				}
			}
		}

		if (celda_error != null) {
			System.out.println("----->error en columna " + columna);
			System.out.println("fila  " + celda_error.getFila());
			System.out.println("col  " + celda_error.getColumna());
		} else
			System.out.println("----->no hay error en columna " + columna);
		return celda_error;
	}

	private Celda controlar_cuadrante_tab(Celda cel, int componente) {
		int cuadrante = cel.get_cuadrante();
		Celda celda_error = null;
		Celda actual;
		int fila = (cuadrante / 3) * 3;
		int columna = (cuadrante % 3) * 3;
		if (componente != 0) {
			for (int i = fila; i < fila + 3 && celda_error == null; i++) {
				for (int j = columna; j < columna + 3 && celda_error == null; j++) {
					actual = tablero[i][j];
					if (actual != cel && actual.getValor() == componente)
						celda_error = actual;

				}
			}
		}

		if (celda_error != null) {
			System.out.println("----->error en cuadrante " + cuadrante);
			System.out.println("fila  " + celda_error.getFila());
			System.out.println("col  " + celda_error.getColumna());

		} else
			System.out.println("----->no hay error en cuadraznte " + cuadrante);

		return celda_error;
	}

	/**
	 * Asigna a cada celda su cuadrante correspondiente
	 */
	private void cargar_cuadrantes() {
		// 0 1 2
		// 2 4 5
		// 6 7 8
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cargar_cuadrantes_aux(i * 3 + j, i, j);
			}
		}
		recorrer_matriz_aux();
	}

	/**
	 * Asigna a cada celda del tablero, el cuadrante al que corresponde
	 * 
	 * @param cuadrante
	 * @param fila
	 * @param columna
	 */
	private void cargar_cuadrantes_aux(int cuadrante, int fila, int columna) {
		for (int f = fila * 3; f < fila * 3 + 3; f++) {
			for (int c = columna * 3; c < columna * 3 + 3; c++) {
				tablero[f][c].set_cuadrante(cuadrante);
			}
		}
	}

	/**
	 * 
	 * @param cadena
	 * @return
	 */
	private static boolean es_numero(String cadena) {

		boolean resultado;

		try {
			Integer.parseInt(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	/**
	 * 
	 * @param fila
	 * @return
	 */
	private boolean controlar_filas_arch(int fila) {
		boolean cumple_filas = true;
		int numero = 1;
		boolean encontre = false;
		while (cumple_filas && numero < 10) {
			for (int col = 0; col < 9 && cumple_filas && !encontre; col++) {
				if (cont_archivo[fila][col] == numero) {
					encontre = true;
					numero++;
				} else if (col == 8)
					cumple_filas = false;
			}
			encontre = false;
		}
		return cumple_filas;

	}

	/**
	 * 
	 * @param col
	 * @return
	 */
	private boolean controlar_columnas_arch(int col) {
		boolean cumple_columnas = true;
		int numero = 1;
		boolean encontre = false;
		while (cumple_columnas && numero < 10) {
			for (int fil = 0; fil < 9 && cumple_columnas && !encontre; fil++) {
				if (cont_archivo[fil][col] == numero) {
					encontre = true;
					numero++;
				} else if (fil == 8)
					cumple_columnas = false;
			}
			encontre = false;
		}
		return cumple_columnas;
	}
}
