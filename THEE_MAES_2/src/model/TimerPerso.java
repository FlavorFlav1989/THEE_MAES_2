package model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import application.SampleController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public final class TimerPerso extends Thread{
	private static int arret_timer = 0;
	private int time;
	private int timer = 0;
	private Callable<Integer> callable;
	
	public TimerPerso(int time, Callable<Integer> callable){
		this.time = time;
		this.callable = callable;
	}
	
	public void run(){
		if(time != 0)lancer_timer(time, this.callable);
		else lancer_timer(1, this.callable);
	}
	
	/**
	 * Lancer une boucle infini avec le temps en seconde qui déclanche la fonction
	 * @param time
	 */
	public void lancer_timer(long time, Callable<Integer> callable){
		long timeMillis = System.currentTimeMillis();
		while(true){
			if(arret_timer == 1) break;
			long time_passed = System.currentTimeMillis() - timeMillis;
			//System.out.println("Seconds in current minute = " + (actual_time) + " " + second_now);
			if(time_passed >= time){
				timeMillis = System.currentTimeMillis();
				timer+=time;
				CalculerThread c_thread = new CalculerThread(callable);
				c_thread.start();
			}	
		}
		arret_timer = 0;
	}
	
	
	public final class CalculerThread extends Thread{
		private Callable<Integer> callable;
		public CalculerThread(Callable<Integer> callable){
			this.callable = callable;
		}
		
		public void run(){
			try {
				this.callable.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Arret du timer
	 */
	public void arreter_timer(){
		arret_timer = 1;
	}
}
