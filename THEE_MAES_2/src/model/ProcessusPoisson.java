package model;

import java.util.ArrayList;
import java.util.List;

public class ProcessusPoisson {
	int lambda;
	List<List<Double>> espacement_evnt = new ArrayList<List<Double>>();
	List<List<Double>> evnt_time = new ArrayList<List<Double>>();
	List<Integer> list_poisson = new ArrayList<Integer>();
	
	List<Double> list_ecart = new ArrayList<Double>();
	List<Double> list_pointe = new ArrayList<Double>();
	int max_time;
	
	public ProcessusPoisson(int lambda){
		this.lambda = lambda;
	}
	
	public double ki2(){
		double[] arr = new double[this.list_ecart.size()];
		List<Double> list = this.list_ecart;
		//Collections.sort(list);
		for(int i = 0; i < list.size(); i++){
			arr[i] = list.get(i);
		}
		Generatrice gen = new Generatrice(10, TypeDistribution.EXPONENTIELLE, arr, lambda);
		return gen.test_ki2();
	}
	/**
	 * Calculer le temps séparant deux évenemens
	 * @return le temps calculé
	 */
	public double calculer_temps_separe_evnmt(){
		return Exponentielle.next_random(lambda);
	}
	
	public void ajout_elem(){
		double temps = calculer_temps_separe_evnmt();
		int index = list_pointe.size();
		list_ecart.add(temps);
		if(index == 0)
			list_pointe.add(temps);
		else
			list_pointe.add(list_pointe.get(index-1) + temps);
		max_time = (int) Math.ceil(list_pointe.get(list_pointe.size() - 1));
	}
	
//	public void reduce_list(List<Double> list_in_1, List<Double> list_in_2){
//		for(int i = 0; i < list_in_1.size(); i++){
//			list_in_1.set(i, list_in_1)
//		}
//	}
	
	public int index_max(List<Double> list_in){
		int index = 0;
		for(int i = 1; i < list_in.size(); i++){
			if(list_in.get(index) < list_in.get(i))
				index = i;
		}
		return index;
	}
	
	
	public void affiche_evnt(){
		System.out.println("List espacement : " + espacement_evnt.toString());
	}
	
	public double getElemEcart(int index){
		return list_ecart.get(index);
	}
	
	public double getElemPointe(int index){
		return list_pointe.get(index);
	}
	
	public List<Double> getListElemPointe(){
		return list_pointe;
	}
	
	public List<Double> getListElemEcart(){
		return list_ecart;
	}

	public void affiche_ecart(){
		System.out.println("List ecart : " + list_ecart.toString());
	}
	
	public void affiche_pointe(){
		System.out.println("List pointe : " + list_pointe.toString());
	}
	
	public double moyenne(){
		double moy = 0;
		for(int i = 0; i < list_ecart.size(); i++){
			moy+= list_ecart.get(i);
		}
		moy/=list_ecart.size();
		return moy;
	}
	
	public int max_time(){
		return this.max_time;
	}
	
	public int compte_valeur_inter(double max){
		int res = 0;
		for(int i = 0; i < max; i++){
			if(Double.compare(list_pointe.get(i), max) < 0)
				res++;
			else
				break;
		}
		return res;
	}
	
	public double lambda_reel(){
		int res = 0;
		int max = this.max_time;
		for(int i = 1; i < max; i++){
			res += compte_in_max(i);
		}
		return ((double)res /(double) max);
	}
	
	private int compte_in_max(int max){
		int res = 0;
		for(int i = 0; i < list_pointe.size(); i++){
			if(list_pointe.get(i) > (max-1) && list_pointe.get(i) <= max){
				res++;
			}
			if(list_pointe.get(i) > max)
				break;
		}
		return res;
	}
}
