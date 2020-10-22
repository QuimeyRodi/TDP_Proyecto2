package Logica;

import java.util.Timer;
//import java.util.TimerTask;
import java.util.TimerTask;

/**
 * Clase que modela un cronometro
 * @author Quimey Rodi
 *
 */
public class Temporizador {
	private Timer timer;
	private int hours;
	private int minutes;
	private int seconds;

	public Temporizador() {
		timer = new Timer();
		hours = minutes = seconds = 0;
	}

	public void getTime(int sec) {
		// if we have hours minutes and seconds
		int remainderOfHours = 0;
		if (sec >= 3600) // if we have an hour or more
		{
			hours = sec / 3600;
			remainderOfHours = sec % 3600; // could be more or less than a min

			if (remainderOfHours >= 60) // check if remainder is more or equal to a min
			{
				minutes = remainderOfHours / 60;
				seconds = remainderOfHours % 60;
			} else { // if it's less than a min
				seconds = remainderOfHours;
			}
		}
		// if we have a min or more
		else if (sec >= 60) {
			hours = 0; // 62
			minutes = sec / 60;
			seconds = sec % 60;
		}
		// if we have just seconds
		else if (sec < 60) {
			hours = 0;
			minutes = 0;
			seconds = sec;
		}

	}

	public void reiniciar() {
		hours = minutes = seconds = 0;
	}
	public int get_hora() {
		return hours;
	}

	public int get_min() {
		return minutes;
	}

	public int get_sec() {
		return seconds;
	}

	public void runTimer(TimerTask task) {
		timer.schedule(task, 0, 1000);
	}
	
	public void stop() {
		timer.cancel();
	}
}
