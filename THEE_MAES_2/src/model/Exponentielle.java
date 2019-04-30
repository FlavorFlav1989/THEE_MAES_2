package model;

public class Exponentielle {
	
	public static double next_random(double alpha){
		double val = -(Math.log(Math.random())/(alpha));
//		while(val > 1){
//			val = -(Math.log(Math.random())/(alpha));
//		}
		return val;
	}
	
	public static double next_th(double alpha, double max, double min){
		double max_val = (1 - Math.exp(-alpha * max));
		double min_val = (1 - Math.exp(-alpha * min));
		return max_val - min_val;
	}
	
	public static double moyenne(double[] tab){
		if(tab == null) return 0;
    	double tot = 0;
    	int nb_value = tab.length;
    	int i = 0;
    	for(i = 0; i < nb_value; i++){
    		//if(tab[i] == 0) break;
    		tot += tab[i];
    	}
    	if(i == 0) return 0;
    	return tot/i;
	}
	
	public static double variance(double[] tab){
		if(tab == null) return 0;
		double res = 0;
    	double moy = moyenne(tab);
    	if(moy == 0) return 0;
    	int nb_value = tab.length;
       	int i = 0;
    	for(i = 0; i < nb_value; i++){
    		//if(tab[i] == 0)break;
    		res += ((tab[i] - moy) * (tab[i] - moy));
    	}
    	if(i == 0) return 0;
    	return res/i;
	}
}
