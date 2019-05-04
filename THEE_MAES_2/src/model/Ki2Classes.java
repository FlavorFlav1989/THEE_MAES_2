package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Ki2Classes {
	private List<Double> borne_inf;
	private List<Double> borne_supp;
	private List<Double> valeurs_reel;
	private List<Double> valeurs_th;
	private List<Double> ki2_vals;
	private int taille;
	
	public Ki2Classes(Double[] borne_inf, Double[] borne_supp, Double[] valeurs_reel, Double[] valeurs_th){
		this.borne_inf = new LinkedList<Double>(Arrays.asList(borne_inf));
		this.borne_supp = new LinkedList<Double>(Arrays.asList(borne_supp));
		this.valeurs_reel = new LinkedList<Double>(Arrays.asList(valeurs_reel));
		this.valeurs_th = new LinkedList<Double>(Arrays.asList(valeurs_th));
		this.taille = this.borne_inf.size();
		reduce();
		ki2();
	}
	
	public void reduce(){
		for(int i = taille-1; i > 0; i--){
			if(valeurs_reel.get(i) < 5){
				//borne_inf.set(i-1, (borne_inf.get(i) +  borne_inf.get(i-1)));
				borne_supp.set(i-1, (borne_supp.get(i)));
				valeurs_reel.set(i-1, (valeurs_reel.get(i) +  valeurs_reel.get(i-1)));
				valeurs_th.set(i-1, (valeurs_th.get(i) +  valeurs_th.get(i-1)));
				
				borne_inf.remove(i);
				borne_supp.remove(i);
				valeurs_reel.remove(i);
				valeurs_th.remove(i);
				
				taille--;
			}
		}
		if(valeurs_reel.get(0) < 5){
			borne_inf.set(1, (borne_inf.get(0) +  borne_inf.get(1)));
			borne_supp.set(1, (borne_supp.get(0) +  borne_supp.get(1)));
			valeurs_reel.set(1, (valeurs_reel.get(0) +  valeurs_reel.get(1)));
			valeurs_reel.set(1, (valeurs_th.get(0) +  valeurs_th.get(1)));
			
			borne_inf.remove(0);
			borne_supp.remove(0);
			valeurs_reel.remove(0);
			valeurs_th.remove(0);
			
			taille--;
		}
		ki2_vals = new ArrayList<Double>();
	}
	
	public double ki2(){
		double classes_theorique;
	    double classes_reel;
	    double q = 0;
	    for(int i = 0; i < taille; i++){
	    	classes_reel = valeurs_reel.get(i);
	    	classes_theorique = valeurs_th.get(i);
	    	//if(classes_reel != 0.0){
	    	
	    		ki2_vals.add((((classes_reel - classes_theorique) * (classes_reel - classes_theorique)) / classes_theorique));
	    		q += (((classes_reel - classes_theorique) * (classes_reel - classes_theorique)) / classes_theorique);
	    		System.out.println(ki2_vals.get(i));
	    	//}	
	    }
	    return q;
	}
	
	 public void affiche_classes(){
		 for(int i = 0; i < taille; i++){
	    		System.out.println("Class : " + i + " Borne inf : " + borne_inf.get(i) + " Borne supp : " + borne_supp.get(i) + " Valeur : " + valeurs_reel.get(i) + " Theorique" + valeurs_th.get(i));
	    	}
    }

	/**
	 * @return the borne_inf
	 */
	public List<Double> getBorne_inf() {
		return borne_inf;
	}

	/**
	 * @param borne_inf the borne_inf to set
	 */
	public void setBorne_inf(List<Double> borne_inf) {
		this.borne_inf = borne_inf;
	}

	/**
	 * @return the borne_supp
	 */
	public List<Double> getBorne_supp() {
		return borne_supp;
	}

	/**
	 * @param borne_supp the borne_supp to set
	 */
	public void setBorne_supp(List<Double> borne_supp) {
		this.borne_supp = borne_supp;
	}

	/**
	 * @return the valeurs_reel
	 */
	public List<Double> getValeurs_reel() {
		return valeurs_reel;
	}

	/**
	 * @param valeurs_reel the valeurs_reel to set
	 */
	public void setValeurs_reel(List<Double> valeurs_reel) {
		this.valeurs_reel = valeurs_reel;
	}

	/**
	 * @return the valeurs_th
	 */
	public List<Double> getValeurs_th() {
		return valeurs_th;
	}

	/**
	 * @param valeurs_th the valeurs_th to set
	 */
	public void setValeurs_th(List<Double> valeurs_th) {
		this.valeurs_th = valeurs_th;
	}

	/**
	 * @return the taille
	 */
	public int getTaille() {
		return taille;
	}

	/**
	 * @param taille the taille to set
	 */
	public void setTaille(int taille) {
		this.taille = taille;
	}

	/**
	 * @return the ki2_vals
	 */
	public List<Double> getKi2_vals() {
		return ki2_vals;
	}

	/**
	 * @param ki2_vals the ki2_vals to set
	 */
	public void setKi2_vals(List<Double> ki2_vals) {
		this.ki2_vals = ki2_vals;
	}
}
