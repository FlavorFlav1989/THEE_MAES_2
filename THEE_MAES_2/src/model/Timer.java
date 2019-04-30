package model;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import application.SampleController;

public final class Timer extends Thread{
	private static int arret_timer = 0;
	private int time;
	private int timer = 0;
	
	public Timer(int time){
		this.time = time;
	}
	
	public void run(){
		if(time != 0)lancer_timer(time);
		else lancer_timer(1);
	}
	
	/**
	 * Lancer une boucle infini avec le temps en seconde qui déclanche la fonction
	 * @param time
	 */
	public void lancer_timer(int time){
		long timeMillis = System.currentTimeMillis();
		long second_now = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
		while(true){
			if(arret_timer == 1) break;
			long actual_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			//System.out.println("Seconds in current minute = " + (actual_time) + " " + second_now);
			if(actual_time - second_now >= time){
				timer+=time;
				second_now = actual_time;
				System.out.println("Lancer fonction");		
			}	
		}
		arret_timer = 0;
	}
	
	/**
	 * Arret du timer
	 */
	public void arreter_timer(){
		arret_timer = 1;
	}
}
