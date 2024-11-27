import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class UsoThreads {

	public static void main(String[] args) {
		JFrame marco = new MarcoRebote();
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marco.setVisible(true);
	}
}

class Pelota {
	public void mueve_pelota(Rectangle2D limites) {
		x += dx;
		y += dy;

		if (x < limites.getMinX()) {
			x = limites.getMinX();
			dx = -dx;
		}

		if (x + TAMX >= limites.getMaxX()) {
			x = limites.getMaxX() - TAMX;
			dx = -dx;
		}

		if (y < limites.getMinY()) {
			y = limites.getMinY();
			dy = -dy;
		}

		if (y + TAMY >= limites.getMaxY()) {
			y = limites.getMaxY() - TAMY;
			dy = -dy;
		}
	}

	public Ellipse2D getShape() {
		return new Ellipse2D.Double(x, y, TAMX, TAMY);
	}

	private static final int TAMX = 15;
	private static final int TAMY = 15;
	private double x = 0;
	private double y = 0;
	private double dx = 1;
	private double dy = 1;
}

class LaminaPelota extends JPanel {
	public void add(Pelota b) {
		pelotas.add(b);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (Pelota b : pelotas) {
			g2.fill(b.getShape());
		}
	}

	private ArrayList<Pelota> pelotas = new ArrayList<>();
}

class MarcoRebote extends JFrame {
	public MarcoRebote() {
		setBounds(600, 300, 600, 400);
		setTitle("Rebotes");
		lamina = new LaminaPelota();
		add(lamina, BorderLayout.CENTER);

		JPanel laminaBotones = new JPanel();

		// Botón para iniciar el movimiento de la primera pelota
		ponerBoton(laminaBotones, "Iniciar Pelota 1", e -> comienza_el_juego(1));

		// Botón para iniciar el movimiento de la segunda pelota
		ponerBoton(laminaBotones, "Iniciar Pelota 2", e -> comienza_el_juego(2));

		// Botón para detener el hilo de la primera pelota
		ponerBoton(laminaBotones, "Parar Pelota 1", e -> pararPelota(1));

		// Botón para suspender el hilo de la segunda pelota (estado WAITING)
		ponerBoton(laminaBotones, "Dormir Pelota 2", e -> suspenderPelota(2));

		// Botón para reanudar el hilo de la segunda pelota
		ponerBoton(laminaBotones, "Despertar Pelota 2", e -> reanudarPelota(2));

		// Botón para salir de la aplicación
		ponerBoton(laminaBotones, "Salir", e -> System.exit(0));

		add(laminaBotones, BorderLayout.SOUTH);
	}

	public void ponerBoton(Container c, String titulo, ActionListener oyente) {
		JButton boton = new JButton(titulo);
		c.add(boton);
		boton.addActionListener(oyente);
	}

	// Inicia el movimiento de una pelota y asocia un hilo a ella
	public void comienza_el_juego(int pelotaId) {
		Pelota pelota = new Pelota();
		lamina.add(pelota);

		PelotaThread hilo = new PelotaThread(pelota, lamina);
		Thread t = new Thread(hilo);
		if (pelotaId == 1) {
			hiloPelota1 = hilo;
			hilo1 = t;
		} else if (pelotaId == 2) {
			hiloPelota2 = hilo;
			hilo2 = t;
		}
		t.start();
	}

	// Detiene el hilo de la primera pelota
	public void pararPelota(int pelotaId) {
		if (pelotaId == 1 && hilo1 != null) {
			hiloPelota1.detener();
		}
	}

	// Suspende el hilo de la segunda pelota
	public void suspenderPelota(int pelotaId) {
		if (pelotaId == 2 && hilo2 != null) {
			hiloPelota2.suspender();
		}
	}

	// Reanuda el hilo de la segunda pelota
	public void reanudarPelota(int pelotaId) {
		if (pelotaId == 2 && hilo2 != null) {
			hiloPelota2.reanudar();
		}
	}

	private LaminaPelota lamina;
	private PelotaThread hiloPelota1;
	private PelotaThread hiloPelota2;
	private Thread hilo1;
	private Thread hilo2;

	// Clase que implementa el hilo asociado a cada pelota
	class PelotaThread implements Runnable {
		private Pelota pelota;
		private LaminaPelota lamina;
		private volatile boolean running = true; // Controla si el hilo está en ejecución
		private volatile boolean suspended = false; // Controla si el hilo está suspendido

		PelotaThread(Pelota p, LaminaPelota l) {
			pelota = p;
			lamina = l;
		}

		// Método para detener el hilo
		public void detener() {
			running = false;
		}

		// Método para suspender el hilo
		public void suspender() {
			suspended = true;
		}

		// Método para reanudar el hilo
		public void reanudar() {
			synchronized (this) {
				suspended = false;
				notify(); // Notifica al hilo que puede continuar
			}
		}

		@Override
		public void run() {
			while (running) {
				try {
					synchronized (this) {
						while (suspended) {
							System.out.println(Thread.currentThread().getName() + " - Estado: WAITING");
							wait(); // Suspende el hilo hasta recibir una notificación
						}
					}
					System.out.println(Thread.currentThread().getName() + " - Estado: RUNNABLE");
					pelota.mueve_pelota(lamina.getBounds());
					lamina.paint(lamina.getGraphics());
					Thread.sleep(4); // Coloca el hilo en estado TIMED_WAITING
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println(Thread.currentThread().getName() + " - Estado: INTERRUPTED");
				}
			}
			System.out.println(Thread.currentThread().getName() + " - Estado: TERMINATED");
		}
	}
}
