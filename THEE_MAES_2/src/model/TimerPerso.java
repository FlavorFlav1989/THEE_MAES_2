package model;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
	private ProcessusPoisson proc_poiss;
	private Set<Data<Number, Number>> serie1;
	private Set<Data<Number, Number>>  serie2;
	
	public TimerPerso(int time, ProcessusPoisson proc_poiss, Set<Data<Number, Number>>  serie1, Set<Data<Number, Number>> serie2){
		this.time = time;
		this.proc_poiss = proc_poiss;
		this.serie1 = serie1;
		this.serie2 = serie2;
	}
	
	public void run(){
		if(time != 0)lancer_timer(time, this.proc_poiss, this.serie1, this.serie2);
		else lancer_timer(1, this.proc_poiss, this.serie1, this.serie2);
	}
	
	/**
	 * Lancer une boucle infini avec le temps en seconde qui déclanche la fonction
	 * @param time
	 */
	public void lancer_timer(long time, ProcessusPoisson proc_poiss,Set<Data<Number, Number>>  serie1, Set<Data<Number, Number>>  serie2){
		long timeMillis = System.currentTimeMillis();
		while(true){
			if(arret_timer == 1) break;
			long time_passed = System.currentTimeMillis() - timeMillis;
			//System.out.println("Seconds in current minute = " + (actual_time) + " " + second_now);
			if(time_passed >= time){
				timeMillis = System.currentTimeMillis();
				timer+=time;	
				proc_poiss.ajout_elem();
				try{
					serie1.add(new XYChart.Data(proc_poiss.getListElemEcart().size()-1, proc_poiss.moyenne()));
					serie2.add(new XYChart.Data(proc_poiss.getListElemEcart().size()-1, proc_poiss.lambda_reel()));
				}
				catch(ConcurrentModificationException e){
					
				}
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
