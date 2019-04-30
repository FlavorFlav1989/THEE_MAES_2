package model;

public class Poisson {
	
	public static double next_random(double alpha){
		  int n = 0; 
		  double limit; 
		  double x; 
		  limit = Math.exp(-alpha);
		  x = Math.random(); 
		  while (x > limit) {
		    n++;
		    x *= Math.random();
		  }
		  return n;
	}
		
	public static double next_th(double alpha, double max, double min){
		double max_val = 0;
		double min_val = 0;		
		double res = 0.0;
		if(max < min){
			double tmp = max;
			max = min;
			min = tmp;
		}
		for(double i = min; i < max; i++){
			double up_1 = Math.pow(alpha, i);
			double up_2 = Math.exp(-alpha);
			double down = fact(i);
			res += (up_1 * up_2 / down);
		}
		System.out.println("RES " + res);
		return max_val - min_val;
	}
	
	public static int fact(double max){
		int res = 1;
		for(int i = 1; i <= max; i++){
			res *= i;
		}
		return res;
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
