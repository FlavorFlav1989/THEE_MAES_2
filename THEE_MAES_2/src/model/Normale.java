package model;

public class Normale {
	public static double next_random(double m, double s){
		 double r = Math.sqrt(-2 * Math.log(Math.random())) * s;
         double x = 2 * Math.PI * Math.random();
         return m + r * Math.sin(x);
	}
	
	public static double next_th(double alpha, double beta, double max, double min){
//		double up = max - alpha;
//		beta = Math.sqrt(beta);
//		double down = beta * Math.sqrt(2);
//		double max_val = ((1 + Erf.erf(-(up / down))) / 2) + 0.5;
//		up = min - alpha;
//		double min_val = ((1 + Erf.erf(-(up / down))) / 2) + 0.5;
//		return (Math.max(max_val, min_val) - Math.min(max_val, min_val));
		
        return 0.0;
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
