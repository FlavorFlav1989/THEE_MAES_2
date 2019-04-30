package model;

public class Uniforme {

	public static double next_random(){
		return Math.random();
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
