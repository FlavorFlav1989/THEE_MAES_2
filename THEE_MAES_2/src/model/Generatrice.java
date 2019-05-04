package model;

public class Generatrice {
    private double echantillon[];
    private double classes[][];
    private double rep_th[];
    private Double borne_inf[];
    private Double borne_supp[];
    private Double ki2_classes_reel[];
    private Double ki2_classes_th[];
    private int nb_value;
    private int nb_class;
    private TypeDistribution distrib;
    private double param1;
    private double param2;
    private double val_max;
    private double val_min;
    private double interval;
    
    public Generatrice(int n, int nb_class, TypeDistribution distrib){
        this.echantillon = new double[n];
        this.rep_th = new double[nb_class];
        this.borne_inf = new Double[nb_class];
        this.borne_supp = new Double[nb_class];
        this.ki2_classes_reel = new Double[nb_class];
        this.ki2_classes_th = new Double[nb_class];
        this.classes = new double[nb_class][n];
        nb_value = n;
        this.nb_class = nb_class;
        this.distrib = distrib;
    }
    
    public Generatrice(int nb_class, TypeDistribution distrib, double[] echantillon, int param){
        this.echantillon = echantillon;
        this.param1 = param;
        this.borne_inf = new Double[nb_class];
        this.borne_supp = new Double[nb_class];
        this.ki2_classes_reel = new Double[nb_class];
        this.ki2_classes_th = new Double[nb_class];
        this.rep_th = new double[nb_class];
        this.classes = new double[nb_class][echantillon.length];
        this.nb_value = echantillon.length;
        this.nb_class = nb_class;
        this.distrib = distrib;
        tri();
        repartir_class();
    }

    public double[] getDistribution(Double alpha, Double beta){
    	switch(distrib){
	    	case UNIFORME : return uniforme();
	    	case POISSON : return poisson(alpha);
	    	case EXPONENTIELLE : return exponentielle(alpha);
	    	case NORMALE : return normale(alpha, beta);
	    	default : return null;
    	}
    }
    
    private double[] uniforme(){
        for(int i = 0; i < nb_value; i++){
            echantillon[i] = Uniforme.next_random();
        }
        tri();
        return echantillon;
    }
    
    private double[] poisson(double alpha){
    	this.param1 = alpha;
        for(int i = 0; i < nb_value; i++){
            echantillon[i] = Poisson.next_random(alpha);
        }
        tri();
        return echantillon;
    }
    
    private double[] exponentielle(double alpha){
    	this.param1 = alpha;
        for(int i = 0; i < nb_value; i++){
            echantillon[i] = Exponentielle.next_random(alpha);
        }
        tri();
        return echantillon;
    }
    
    private double[] normale(double alpha, double beta){
    	this.param1 = alpha;
    	this.param2 = beta;
    	for(int i = 0; i < nb_value; i++){
            echantillon[i] = Normale.next_random(alpha, beta);
        }
        tri();
        return echantillon;
    }

    public double[][] repartir_class(){
    	int k = 0;
        for(int i = 1; i <= nb_class; i++){
        	/*Borne supérieur de la classe*/
        	double max_class = val_min + (i * ((val_max + Math.abs(val_min))/(double)nb_class));
        	
        	/*Borne inféireur de la classe*/
        	double min_class = (i == 1 ? val_min : (val_min + ((i-1) * ((val_max + Math.abs(val_min))/(double)nb_class))));
        	borne_inf[i-1] = min_class;
        	borne_supp[i-1] = max_class;
        	switch(distrib){
        	case UNIFORME :	rep_th[i-1] = nb_value/nb_class;
        					break; 
        	case EXPONENTIELLE : rep_th[i-1] = (Exponentielle.next_th(this.param1, max_class, min_class))*nb_value;
        						break; 
        	case POISSON : rep_th[i-1] = (Poisson.next_th(this.param1, max_class, min_class))*nb_value;
        					break; 
        	case NORMALE : rep_th[i-1] = (Normale.next_th(this.param1, this.param2,max_class, min_class))*nb_value;
        					break; 
        	}
        	for(int j = 0; j < nb_value; j++){
        		if(echantillon[j] <= max_class && echantillon[j] > min_class){
        			classes[i-1][k] = echantillon[j];
        			k++;
        		}
            }
        	k = 0;
        }
        reduce_class();
        return classes;
    }
    
    private void reduce_class(){
    	for(int i = classes.length-1 ; i > 0 ; i--){
    		if(compte_valeur(classes[i]) < 5){
    			migrer_valeur(i, i--, compte_valeur(classes[i]));
    		}
    	}
    }
    
    private void migrer_valeur(int index_out, int index_in, int nb_value){
    	int index_to_insert = compte_valeur(classes[index_in]);
    	for(int i = 0; i < nb_value; i++){
    		classes[index_in][index_to_insert] = classes[index_out][i];
    		classes[index_out][i] = 0;
    		index_to_insert++;
    	}
    }

    public void tri(){
        for(int i = 0; i < nb_value; i++){
        	for(int j = i; j < nb_value; j++){
        		if(echantillon[i] > echantillon[j]){
        			echantillon[j] = echantillon[i] + echantillon[j];
        			echantillon[i] = echantillon[j] - echantillon[i];
        			echantillon[j] = echantillon[j] - echantillon[i];
        		}
        	}
        }
        if(nb_value != 0){
        	 val_max = echantillon[nb_value-1];
             val_min = echantillon[0];
             interval = (val_max + Math.abs(val_min)) / nb_class;
        }
    }

    public void affiche_echantilon(){
    	for(int i = 0; i < nb_value; i++){
    		System.out.println("Valeur : " + echantillon[i]);
    	}
    }

    public void affiche_classes(){
    	for(int i = 0; i < nb_class; i++){
    		for(int j = 0; j < nb_value; j++){
    			if(classes[i][j] != 0)
    				System.out.println("Class : " + i + " Valeur " + j + " : " + classes[i][j]);
    			else
    				break;
        	}
    	}
    }
    
    public void affiche_classes_2(){
    	for(int i = 0; i < nb_class; i++){
    		System.out.println("Class : " + i + " Borne inf : " + borne_inf[i] + " Borne supp : " + borne_supp[i] + " Valeur : " + ki2_classes_reel[i] + " Theorique" + ki2_classes_th[i]);
    	}

    }

    public double moyenne(){
    	switch(distrib){
    	case UNIFORME : return Uniforme.moyenne(echantillon);
    	case POISSON : return Poisson.moyenne(echantillon);
    	case EXPONENTIELLE : return Exponentielle.moyenne(echantillon);
    	case NORMALE : return Normale.moyenne(echantillon);
    	default : return 0;
    	}
    }

    public double variance(){
    	switch(distrib){
    	case UNIFORME : return Uniforme.variance(echantillon);
    	case POISSON : return Poisson.variance(echantillon);
    	case EXPONENTIELLE : return Exponentielle.variance(echantillon);
    	case NORMALE : return Normale.variance(echantillon);
    	default : return 0;
    	}
    }
    
    public double test_ki2(){
    	switch(distrib){
    	case UNIFORME : ;
    	case POISSON : ;
    	case EXPONENTIELLE : ;
    	case NORMALE : ;
    	}
    	double classes_theorique;
    	int classes_reel;
    	double q = 0;
    	for(int i = 0; i < nb_class; i++){
    		classes_reel = compte_valeur(classes[i]);
    		ki2_classes_reel[i] = Double.valueOf(classes_reel);
    		classes_theorique = rep_th[i];
    		ki2_classes_th[i] = classes_theorique ;
//    		double classes_reel_conv = (double) classes_reel;
//    		double classes_theorique_conv = (double) classes_theorique;
//    		if(classes_reel_conv != 0.0 && classes_reel >= 5)
//    			q += (((classes_reel_conv - classes_theorique_conv) * (classes_reel_conv - classes_theorique_conv)) / classes_theorique_conv);
    	}
    	
    	affiche_classes_2();
    	Ki2Classes ki2 = new Ki2Classes(this.borne_inf, this.borne_supp, this.ki2_classes_reel, this.ki2_classes_th);
    	ki2.affiche_classes();
    	return ki2.ki2();
    }
    
    public Ki2Classes getObjectKi2(){
    	double classes_theorique;
    	int classes_reel;
    	double q = 0;
    	for(int i = 0; i < nb_class; i++){
    		classes_reel = compte_valeur(classes[i]);
    		ki2_classes_reel[i] = Double.valueOf(classes_reel);
    		classes_theorique = rep_th[i];
    		ki2_classes_th[i] = classes_theorique ;
    	}
    	Ki2Classes ki2 = new Ki2Classes(this.borne_inf, this.borne_supp, this.ki2_classes_reel, this.ki2_classes_th);
    	return ki2;
    }
    
    public int compte_valeur(double tab[]){
    	int i = 0;
    	for(i = 0; i < tab.length; i++){
    		if(tab[i] == 0)break;
    	}
    	return i;
    }

	/**
	 * @return the nb_value
	 */
	public int getNb_value() {
		return nb_value;
	}

	/**
	 * @param nb_value the nb_value to set
	 */
	public void setNb_value(int nb_value) {
		this.nb_value = nb_value;
	}

	/**
	 * @return the nb_class
	 */
	public int getNb_class() {
		return nb_class;
	}

	/**
	 * @param nb_class the nb_class to set
	 */
	public void setNb_class(int nb_class) {
		this.nb_class = nb_class;
	}

	/**
	 * @return the interval
	 */
	public double getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}

	/**
	 * @return the val_max
	 */
	public double getVal_max() {
		return val_max;
	}

	/**
	 * @param val_max the val_max to set
	 */
	public void setVal_max(double val_max) {
		this.val_max = val_max;
	}

	/**
	 * @return the val_min
	 */
	public double getVal_min() {
		return val_min;
	}

	/**
	 * @param val_min the val_min to set
	 */
	public void setVal_min(double val_min) {
		this.val_min = val_min;
	}

}
