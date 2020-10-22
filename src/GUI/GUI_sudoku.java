package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import Logica.Celda;
import Logica.Entidad_grafica;
import Logica.FileException;
import Logica.Opcion;
import Logica.Sudoku;
import Logica.Temporizador;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Clase que modela la interfaz grafica del juego Sudoku y un temporizador
 * 
 * @author Quimey Rodi
 *
 */
public class GUI_sudoku extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1553445123289675595L;
	private JPanel contentPane;
	private Sudoku mi_sudoku;
	private JLabel tablero[][];
	private JLabel hora[];
	private JButton opciones[];
	private JLabel ultimo_label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_sudoku frame = new GUI_sudoku();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_sudoku() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GUI_sudoku.class.getResource("/img/bola8.png")));

		setTitle("Sudoku");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 618, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		try {

			mi_sudoku = new Sudoku("archivo/solucion.txt");
			tablero = new JLabel[mi_sudoku.get_filas()][mi_sudoku.get_filas()];
			opciones = new JButton[12];
			// --------------------------------------------------
			JLabel mensajes = new JLabel("");
			mensajes.setBounds(10, 38, 350, 20);
			contentPane.add(mensajes);
			// --------------------------------------------------
			Temporizador temp = new Temporizador();
			crear_cronometro(temp);
			crear_panel_opciones(temp);
			crear_tablero_sudoku(mensajes);
			pintar_cuadrantes();

			// --------------------------------------------------
			JButton reiniciar = new JButton("Reiniciar");
			reiniciar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					GUI_sudoku nuevo = new GUI_sudoku();
					nuevo.setVisible(true);
					dispose();
				}
			});

			reiniciar.setBounds(443, 354, 89, 23);
			contentPane.add(reiniciar);

		} catch (FileException e) {
			JOptionPane.showMessageDialog(contentPane, "ARCHIVO INVALIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(0);;		
		}
	}

	private void crear_panel_opciones(Temporizador temp) {
		// --------------------opciones
		JPanel panel_opciones = new JPanel();
		panel_opciones.setBounds(392, 115, 190, 205);
		panel_opciones.setLayout(new GridLayout(0, 3, 0, 0));
		contentPane.add(panel_opciones);

		for (int f = 0; f < 12; f++) {
			JButton boton_num = new JButton();
			ImageIcon imagen;
			Opcion opcion;
			panel_opciones.add(boton_num);
			if (f == 9 || f == 11) {
				opcion = null;
				imagen = null;
				boton_num.setEnabled(false);
			} else {
				if (f == 10) {
					opcion = mi_sudoku.getOpcion(0);
					imagen = opcion.getEntidad_grafica().getGrafico();
				} else {
					opcion = mi_sudoku.getOpcion(f + 1);
					imagen = opcion.getEntidad_grafica().getGrafico();
				}
			}
			opciones[f] = boton_num;
			boton_num.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					if (imagen != null) {
						reDimensionar(imagen, boton_num.getHeight(), boton_num.getWidth());
						boton_num.repaint();
						boton_num.setIcon(imagen);
					}
				}
			});

			boton_num.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (boton_num.isEnabled() && opcion != null) {
						Celda celda_act = mi_sudoku.get_celda_activada();
						if (celda_act != null || mi_sudoku.hay_error()) {
							mi_sudoku.accionar(celda_act, opcion);
							reDimensionar(celda_act.getEntidad_grafica().getGrafico(), ultimo_label.getHeight(),
									ultimo_label.getWidth());
							ultimo_label.repaint();
							ultimo_label.setBackground(null);

							mi_sudoku.controlar_tablero(opcion, celda_act);

							mostrar_errores();

							if (mi_sudoku.gane()) {
								temp.stop();
								JOptionPane.showMessageDialog(contentPane, "GANASTE", "completaste el juego",
										JOptionPane.INFORMATION_MESSAGE);
								habilitar_opciones(false);
								hablitar_tablero(false);
							}
						}
					}
				}
			});

		}
	}

	private void crear_tablero_sudoku(JLabel mensajes) {
		// --------------------tablero
		JPanel panel_tablero = new JPanel();
		panel_tablero.setBounds(10, 81, 350, 343);
		contentPane.setLayout(null);

		contentPane.add(panel_tablero);

		panel_tablero.setLayout(new GridLayout(9, 0, 0, 0));

		for (int f = 0; f < mi_sudoku.get_filas(); f++) {
			for (int c = 0; c < mi_sudoku.get_filas(); c++) {
				Celda celda = mi_sudoku.getCelda(f, c);
				ImageIcon grafico = celda.getEntidad_grafica().getGrafico();
				JLabel label_celda = new JLabel();

				if((f%3)==0 && (c%3)==0) {
		            label_celda.setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, Color.DARK_GRAY));
		        }else {
		            if((f%3)==0) {
		                label_celda.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.DARK_GRAY));
		            }else {
		                if((c%3)==0) {
		                    label_celda.setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, Color.DARK_GRAY));
		                }else {
		                    label_celda.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		                }
		            }
		        }
				
				
				panel_tablero.add(label_celda);
				tablero[f][c] = label_celda;
				label_celda.setOpaque(true);

				if (!celda.esta_habilitada()) {
					label_celda.setBackground(Color.LIGHT_GRAY);
				}

				label_celda.addComponentListener(new ComponentAdapter() {

					@Override
					public void componentResized(ComponentEvent e) {
						label_celda.setIcon(grafico);
						reDimensionar(grafico, label_celda.getHeight(), label_celda.getWidth());

						label_celda.repaint();
					}
				});
				// TABLERO
				label_celda.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (celda.esta_habilitada()) {
							if (mi_sudoku.hay_error()) {
								if (label_celda == ultimo_label) {
									habilitar_opciones(true);
									mensajes.setText(" ");
									// ---
									label_celda.setBackground(Color.GRAY);
									ultimo_label.setBackground(null);
									// ---
								} else {
									habilitar_opciones(false);
									mensajes.setText("Debe corregir el error!");
									ultimo_label.setBackground(Color.cyan);
								}

							} else {
								habilitar_opciones(true);
								mensajes.setText(" ");
								if (mi_sudoku.get_celda_activada() == null) {
									ultimo_label = label_celda;
									label_celda.setBackground(Color.GRAY);
									System.out.println("celda: (" + celda.getFila() + "," + celda.getColumna()
											+ ") cuadrante-> " + celda.get_cuadrante());
									mi_sudoku.activar_celda(celda.getFila(), celda.getColumna());
								} else {
									label_celda.setBackground(Color.GRAY);
									ultimo_label.setBackground(null);
									ultimo_label = label_celda;
									mi_sudoku.activar_celda(celda.getFila(), celda.getColumna());
								}
							}
						} else {
							mensajes.setText("Esta celda no se puede modificar");
							habilitar_opciones(false);
						}
					}
				});
				
			}
		} // -----------------------FIN tablero---------------------------------
	}

	private void crear_cronometro(Temporizador temp) {
		// ---------------temporizador
		hora = new JLabel[6];
		JPanel panel_hora = new JPanel();
		panel_hora.setBounds(407, 81, 160, 20);
		contentPane.add(panel_hora);
		panel_hora.setLayout(new GridLayout(0, 8, 0, 0));
		int pos = 0;
		for (int i = 0; i < 8; i++) {
			JLabel label_hora = new JLabel();
			panel_hora.add(label_hora);

			if (i != 2 && i != 5) {
				hora[pos++] = label_hora;
			}

		}

		TimerTask task = new TimerTask() {

			int sec = 0;
			Entidad_grafica entidad_hora1 = new Entidad_reloj();
			Entidad_grafica entidad_hora2 = new Entidad_reloj();
			Entidad_grafica entidad_min1 = new Entidad_reloj();
			Entidad_grafica entidad_min2 = new Entidad_reloj();
			Entidad_grafica entidad_sec1 = new Entidad_reloj();
			Entidad_grafica entidad_sec2 = new Entidad_reloj();

			public void run() {
				temp.getTime(sec);

				int horas = temp.get_hora();
				int minutos = temp.get_min();
				int segundos = temp.get_sec();

				entidad_hora2.actualizar(horas % 10);
				hora[1].setIcon(entidad_hora2.getGrafico());
				reDimensionar(entidad_hora2.getGrafico(), 20, 20);

				hora[1].repaint();
				horas = horas / 10;
				entidad_hora1.actualizar(horas);
				hora[0].setIcon(entidad_hora1.getGrafico());
				reDimensionar(entidad_hora1.getGrafico(), 20, 20);
				hora[0].repaint();

				entidad_min2.actualizar(minutos % 10);
				hora[3].setIcon(entidad_min2.getGrafico());
				reDimensionar(entidad_min2.getGrafico(), 20, 20);
				hora[3].repaint();
				minutos = minutos / 10;
				entidad_min1.actualizar(minutos);
				hora[2].setIcon(entidad_min1.getGrafico());
				reDimensionar(entidad_min1.getGrafico(), 20, 20);
				hora[2].repaint();

				entidad_sec2.actualizar(segundos % 10);
				hora[5].setIcon(entidad_sec2.getGrafico());
				reDimensionar(entidad_sec2.getGrafico(), 20, 20);
				hora[5].repaint();
				segundos = segundos / 10;
				entidad_sec1.actualizar(segundos);
				hora[4].setIcon(entidad_sec1.getGrafico());
				reDimensionar(entidad_sec1.getGrafico(), 20, 20);
				hora[4].repaint();

				sec++;
			}
		};
		temp.runTimer(task);
		// ---------------------------------------FINtemp
	}

	// ---------------------------------------------------------------
	private void mostrar_errores() {
		Celda actual;
		JLabel label;
		for (int f = 0; f < mi_sudoku.get_filas(); f++) {
			for (int c = 0; c < mi_sudoku.get_filas(); c++) {
				actual = mi_sudoku.getCelda(f, c);
				label = tablero[f][c];
				if (actual.tiene_error())
					if (label == ultimo_label)
						label.setBackground(Color.CYAN);
					else
						label.setBackground(Color.RED);
				else if (actual.esta_habilitada())
					label.setBackground(null);
				else
					label.setBackground(Color.LIGHT_GRAY);

			}
		}
	}

	private void habilitar_opciones(boolean estado) {
		for (int i = 0; i < mi_sudoku.get_filas(); i++) {
			opciones[i].setEnabled(estado);
		}
	}

	private void reDimensionar(ImageIcon grafico, int h, int a) {
		Image image = grafico.getImage();
		if (image != null) {
			Image newimg = image.getScaledInstance(a, h, java.awt.Image.SCALE_SMOOTH);
			grafico.setImage(newimg);

		}
	}

	private void hablitar_tablero(boolean estado) {
		for (int f = 0; f < 9; f++)
			for (int c = 0; c < 9; c++)
				tablero[f][c].setEnabled(estado);
	}

	private void pintar_cuadrantes() {
//		for (int f = 0; f < 3; f++)
//			for (int c = 0; c < 3; c++)
//				tablero[f][c].setBorder(new LineBorder(Color.DARK_GRAY, 2));
//
//		for (int f = 0; f < 3; f++)
//			for (int c = 6; c < 9; c++)
//				tablero[f][c].setBorder(new LineBorder(Color.DARK_GRAY, 2));
//
//		for (int f = 3; f < 6; f++)
//			for (int c = 3; c < 6; c++)
//				tablero[f][c].setBorder(new LineBorder(Color.DARK_GRAY, 2));
//
//		for (int f = 6; f < 9; f++)
//			for (int c = 0; c < 3; c++)
//				tablero[f][c].setBorder(new LineBorder(Color.DARK_GRAY, 2));
//
//		for (int f = 6; f < 9; f++)
//			for (int c = 6; c < 9; c++)
//				tablero[f][c].setBorder(new LineBorder(Color.DARK_GRAY, 2));
//	
		
		}
		
}
