package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Ki2Classes;
import model.ProcessusPoisson;
import model.TimerPerso;

public class SampleController {
	Alert alert = new Alert(AlertType.WARNING);
	ProcessusPoisson proc_Poiss;
	float current_i = 1;
	@FXML
	Button test_button;
	
	@FXML
	Button pause_btn;
	
	@FXML
	TextField nb_valeur;
	
	@FXML
	TextField moyenne_reel;
	
	@FXML
	TextField moyenne_poiss_reel;
	
	@FXML
	TextField moyenne_th;
	
	@FXML
	TextField moyenne_poiss_th;
	
	@FXML
	TextField lambda;

	@FXML
	TextField ki2;
	
	@FXML
	BarChart<String, Number> bc;
	
	@FXML
	NumberAxis xLineAxis = new NumberAxis();
	
	@FXML
	NumberAxis yLineAxis = new NumberAxis();
	
	
	@FXML
	NumberAxis xLineMoyAxis = new NumberAxis();;
	
	@FXML
	NumberAxis yLineMoyAxis = new NumberAxis();;
	
	LineChart<Number, Number> lc_moy = new LineChart<Number, Number>(xLineMoyAxis, yLineMoyAxis);
	
	@FXML
	NumberAxis xLinePoissAxis = new NumberAxis();;
	
	@FXML
	NumberAxis yLinePoissAxis = new NumberAxis();;
	
	LineChart<Number, Number> lc_poiss = new LineChart<Number, Number>(xLinePoissAxis, yLinePoissAxis);
	
	@FXML
	LineChartWithMarkers<Number, Number> lc;
	
	Button ki2_button;
	
	Timer timer;
	
	@FXML
	VBox vb_princ;
	
	Timeline timeline;
	
	TimerPerso timer_perso;
	
	int index_tot = 0;
	int lambda_glob = 5;
	int nb_valeur_glob = 150;
	XYChart.Series<Number, Number> series1_expo;
	XYChart.Series<Number, Number> series2_expo;
	
	XYChart.Series<Number, Number> series1_poiss;
	XYChart.Series<Number, Number> series2_poiss;
	
	Set<Data<Number, Number>> series1_expo_tmp;
	Set<Data<Number, Number>> series1_poiss_tmp;
	Label[][] ki2_tab;
	GridPane grid = new GridPane();
	Stage stage;
	
	Ki2Classes ki2_classes;
	
	public void click_ki2(){
		stage.show();
		
	}
	
	public void init_ki2(){
		stage = new Stage();
		stage.setWidth(1000);
		stage.setHeight(300);
		stage.setScene(new Scene(grid));
		
	}
	
	@FXML
	public void initialize(){
		init_ki2();
		pause_btn.setDisable(true);
		initialize_chart_lc();
		vb_princ.getChildren().add(lc_moy);
		vb_princ.getChildren().add(lc_poiss);
	    lc_moy.setCreateSymbols(false);
	    lc_poiss.setCreateSymbols(false);
	    lc_moy.setTitle("Evolution de la moyenne suivant la loi exponentielle");
	    lc_poiss.setTitle("Evolution de la moyenne suivant la loi de poisson");
	    //yLineAxis.setTickUnit(0.1);
	}
	 
	public void initialize_chart_lc(){
		try{
			vb_princ.getChildren().remove(1);
		}
		catch(Exception e){
			
		}
		
		xLineMoyAxis.setAutoRanging(false);
		xLineMoyAxis.setLowerBound(0);
		xLineMoyAxis.setUpperBound(10);
		xLineMoyAxis.setLabel("Occurences");
		yLineMoyAxis.setLabel("Moyenne");
		
		xLinePoissAxis.setAutoRanging(false);
		xLinePoissAxis.setLowerBound(0);
		xLinePoissAxis.setUpperBound(10);
		xLinePoissAxis.setLabel("Occurences");
		yLinePoissAxis.setLabel("Moyenne");
		
		xLineAxis.setAutoRanging(false);
		xLineAxis.setLowerBound(0);
		xLineAxis.setUpperBound(10);
		//xLineAxis.setTickUnit(3);

		yLineAxis.setAutoRanging(false);
		yLineAxis.setLowerBound(0);
		yLineAxis.setUpperBound(1);
		lc = new LineChartWithMarkers<Number, Number>(xLineAxis, yLineAxis);
		lc.setTitle("Processus de poisson");
		xLineAxis.setLabel("Temps");
		yLineAxis.setLabel("Etat");
		
		vb_princ.getChildren().add(1, lc);
	}
	
	public void start_timer(ActionEvent evnt){
		 xLineAxis.setLowerBound(0);
		    xLineAxis.setUpperBound(10);
	}
	
	public void arreter_timer(ActionEvent evnt){
		 xLineAxis.setLowerBound(1);
		    xLineAxis.setUpperBound(11);
	}
		
	@FXML
	public void test() throws InterruptedException, NoSuchMethodException, SecurityException{
		clear_chart();
		initialize_chart_lc();
		pause_btn.setText("Pause");
		ki2.setText("");
		index_tot = 0;
		
		if(this.nb_valeur.getText() != null && !this.nb_valeur.getText().equals("")){
			try{
				nb_valeur_glob = Integer.parseInt(this.nb_valeur.getText());
			}
			catch(Exception e){
				this.nb_valeur.setText("150");
				nb_valeur_glob = 150;
			}
		}
		else{
			this.nb_valeur.setText("150");
			nb_valeur_glob = 150;
		}
		
		if(this.lambda.getText() != null && !this.lambda.getText().equals("")){
			try{
				lambda_glob = Integer.parseInt(this.lambda.getText());
			}
			catch(Exception e){
				this.lambda.setText("5");
				lambda_glob = 5;
			}
		}
		else{
			this.lambda.setText("5");
			lambda_glob = 5;
		}
		moyenne_th.setText("" + (1.0/((double)this.lambda_glob)));
		moyenne_poiss_th.setText("" + (this.lambda_glob));
		
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(100),
		        ae -> actu_tracer()));
		
		timeline.setCycleCount(Animation.INDEFINITE);
		
		proc_Poiss = new ProcessusPoisson(lambda_glob);
		
		series1_expo = new XYChart.Series<Number, Number>();
		series2_expo = new XYChart.Series<Number, Number>();
		
		series2_expo.getData().add(new XYChart.Data(0, 1.0/(double)lambda_glob));
		series2_expo.getData().add(new XYChart.Data(nb_valeur_glob, 1.0/(double)lambda_glob));
		
		series1_expo.setName("Moyenne r�el");
		series2_expo.setName("Moyenne th�orique");
		
		series1_poiss = new XYChart.Series<Number, Number>();
		series2_poiss = new XYChart.Series<Number, Number>();
		
		series2_poiss.getData().add(new XYChart.Data(0, lambda_glob));
		series2_poiss.getData().add(new XYChart.Data(nb_valeur_glob, lambda_glob));
		
		series1_poiss.setName("Moyenne r�el");
		series2_poiss.setName("Moyenne th�orique");
		
		series1_expo_tmp = new HashSet<Data<Number, Number>>();
		series1_poiss_tmp = new HashSet<Data<Number, Number>>();
		
		lc_moy.getData().add(series1_expo);
		lc_moy.getData().add(series2_expo);
		
		lc_poiss.getData().add(series1_poiss);
		lc_poiss.getData().add(series2_poiss);
		timer_perso = new TimerPerso(1000/this.lambda_glob, new java.util.concurrent.Callable<Integer>(){public Integer call(){return calculer();}});
		timer_perso.start();
		timeline.play();
		
	}
	
	public Integer calculer(){
		proc_Poiss.ajout_elem();
		try{
			series1_expo_tmp.add(new XYChart.Data(proc_Poiss.getListElemEcart().size()-1, proc_Poiss.moyenne()));
			series1_poiss_tmp.add(new XYChart.Data(proc_Poiss.getListElemEcart().size()-1, proc_Poiss.lambda_reel()));
		}
		catch(ConcurrentModificationException e){
			return 0;
		}
		return 1;
	}
	   public void unionArrays(List<Data<Number, Number>> arrays_1, List<Data<Number, Number>> arrays_2)
	    {
		   boolean is_present = false;
	        for(int i = 0; i < arrays_2.size(); i++){
	        	for(int j = 0; j < arrays_1.size(); j++){
		        	if(arrays_1.get(j).getXValue().equals(arrays_2.get(i).getXValue())){
		        		is_present = true;
		        		break;
		        	}
		        		
		        }
	        	if(!is_present){
	        		arrays_1.add(arrays_2.get(i));
	        	}
	        	is_present = false;
	        }
	    }
	    
	public void remplir_tab(){
		for(int i = 0; i < ki2_tab.length; i++){
			for(int j = 0; j < ki2_tab[i].length; j++){
				if(j == 0)
				{
					ki2_tab[i][j].setStyle("-fx-font-weight: bold");
					ki2_tab[i][j].setPrefWidth(125);
					if(i == 0)
						ki2_tab[i][j].setText(" Classes ");
					else if(i == 1)
						ki2_tab[i][j].setText(" Bornes ");
					else if(i == 2)
						ki2_tab[i][j].setText(" Valeurs r�els ");
					else if(i == 3)
						ki2_tab[i][j].setText(" Valeurs th�oriques ");
					else if(i == 4)
						ki2_tab[i][j].setText(" ki2 ");
				}
				else{
					if(i == 0)
						ki2_tab[i][j].setText("" + (j-1));
					else if(i == 1)
						ki2_tab[i][j].setText("[" + String.format("%.2f",ki2_classes.getBorne_inf().get(j-1)) + "; " + String.format("%.2f", ki2_classes.getBorne_supp().get(j-1)) + "]");
					else if(i == 2)
						ki2_tab[i][j].setText("" + ki2_classes.getValeurs_reel().get(j-1));
					else if(i == 3)
						ki2_tab[i][j].setText("" + String.format("%.2f", ki2_classes.getValeurs_th().get(j-1)));
					else if(i == 4)
						ki2_tab[i][j].setText("" + String.format("%.2f", ki2_classes.getKi2_vals().get(j-1)));
				}
			}
		} 
	}
	
	public void creer_grid_label(){
		for(int i = 0; i < ki2_tab.length; i++){
			for(int j = 0; j < ki2_tab[i].length; j++){
				ki2_tab[i][j] = new Label();
				GridPane.setRowIndex(ki2_tab[i][j], i);
				GridPane.setColumnIndex(ki2_tab[i][j], j);
				grid.getChildren().add(ki2_tab[i][j]);
			}
		}
	}
	public void actu_tracer(){	
		//series1_expo = new XYChart.Series<Number, Number>();
		List<Data<Number, Number>> list = new ArrayList<Data<Number, Number>>(series1_expo_tmp);
		unionArrays(series1_expo.getData(), list);
		
		List<Data<Number, Number>> list2 = new ArrayList<Data<Number, Number>>(series1_poiss_tmp);
		unionArrays(series1_poiss.getData(), list2);
		
			if(index_tot == nb_valeur_glob){
				timeline.stop();
				timer_perso.arreter_timer();
				ki2_classes = proc_Poiss.ki2();
				ki2_tab = new Label[5][ki2_classes.getTaille()+1];
				creer_grid_label();
				remplir_tab();
				ki2.setText(""+proc_Poiss.ki2().ki2());
			}
			else{
				index_tot = proc_Poiss.getListElemEcart().size();	
				avance_chart();
				//tracer_line(proc_Poiss.getElemPointe(index_tot), 0);
				tracer_lines();
				moyenne_reel.setText(""+proc_Poiss.moyenne());
				moyenne_poiss_reel.setText(""+proc_Poiss.lambda_reel());		
			}
	}
	
	public void actu_tracer2(){	
		if(index_tot == nb_valeur_glob){
				timeline.stop();
				ki2.setText(""+proc_Poiss.ki2().ki2());
			}
			else{
				
				//clear_chart();				
				proc_Poiss.ajout_elem();
				ki2.setText(""+proc_Poiss.ki2().ki2());
				avance_chart();
				//tracer_line(proc_Poiss.getElemPointe(index_tot), 0);
				tracer_lines();
				series1_expo.getData().add(new XYChart.Data(index_tot, proc_Poiss.moyenne()));
				series1_poiss.getData().add(new XYChart.Data(index_tot, proc_Poiss.lambda_reel()));
				//lc_moy.getData().get(0).getData().add(new XYChart.Data(index_tot, proc_Poiss.moyenne()));
				//lc_moy.getData().set(0, series1);
				moyenne_reel.setText(""+proc_Poiss.moyenne());
				moyenne_poiss_reel.setText(""+proc_Poiss.lambda_reel());
				
				index_tot++;				
			}
	}
	
	public void pause(){
		if(timeline.getStatus().equals(Status.PAUSED)){
			pause_btn.setText("Pause");
			timeline.play();
		}
		else{
			pause_btn.setText("Reprendre");
			timeline.pause();
		}
		
	}
	
	public void tracer_lines(){
//		lc = new LineChartWithMarkers<Number, Number>(xLineAxis, yLineAxis);
//		lc.setTitle("Processus de poisson");
//		xLineAxis.setLabel("Temps");
//		yLineAxis.setLabel("Etat");
//		vb_princ.getChildren().remove(1);
//		vb_princ.getChildren().add(1, lc);
		lc.removeAllVerticalMarker();
		List<Double> list = proc_Poiss.getListElemPointe();
		for(int i = 0; i < list.size(); i++){
			tracer_line(list.get(i), 0);
		}
	}
	public void tracer_line(double x, double y){
		 Data<Number, Number> verticalMarker = new Data<>(x, y);
	        lc.addVerticalValueMarker(verticalMarker);
	        
	        Slider verticalMarkerSlider = new Slider(xLineAxis.getLowerBound(), xLineAxis.getUpperBound(), 0);
	        verticalMarkerSlider.setOrientation(Orientation.HORIZONTAL);
	        verticalMarkerSlider.setShowTickLabels(true);
	        verticalMarkerSlider.valueProperty().bindBidirectional(verticalMarker.XValueProperty());
	        verticalMarkerSlider.minProperty().bind(xLineAxis.lowerBoundProperty());
	        verticalMarkerSlider.maxProperty().bind(xLineAxis.upperBoundProperty());
	}
		
	private void clear_chart(){
		lc.getData().clear();
		lc_moy.getData().clear();
		lc_poiss.getData().clear();
	}
		
	@SuppressWarnings("unused")
	private void display_popup(String text){
		alert.setTitle("Attention");
	    alert.setHeaderText(text);
	    alert.showAndWait(); 
	}
	
	@SuppressWarnings("rawtypes")
	private class LineChartWithMarkers<X,Y> extends LineChart {

        private ObservableList<Data<X, Y>> horizontalMarkers;
        private ObservableList<Data<X, Y>> verticalMarkers;

        @SuppressWarnings("unchecked")
		public LineChartWithMarkers(Axis<X> xAxis, Axis<Y> yAxis) {
            super(xAxis, yAxis);
            horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
            horizontalMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());
            verticalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
            verticalMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());
        }

        public void addHorizontalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (horizontalMarkers.contains(marker)) return;
            Line line = new Line();
            //setColor(line);
            marker.setNode(line );
            getPlotChildren().add(line);
            horizontalMarkers.add(marker);
        }

        public void removeHorizontalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            horizontalMarkers.remove(marker);
        }

        public void addVerticalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (verticalMarkers.contains(marker)) return;
            Line line = new Line();
            //setColor(line);
            marker.setNode(line );
            getPlotChildren().add(line);
            verticalMarkers.add(marker);
        }

        public void removeAllVerticalMarker(){
        	for (int i = verticalMarkers.size()-1; i >=0; i--) {
        		removeVerticalValueMarker(verticalMarkers.get(i));
            }    
        }
        public void removeVerticalValueMarker(Data<X, Y> marker) {
            Objects.requireNonNull(marker, "the marker must not be null");
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            verticalMarkers.remove(marker);
        }


        @Override
        protected void layoutPlotChildren() {
            super.layoutPlotChildren();
            for (Data<X, Y> horizontalMarker : horizontalMarkers) {
                Line line = (Line) horizontalMarker.getNode();
                //setColor(line);
                line.setEndX(getBoundsInLocal().getWidth());
                line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
                line.setEndY(line.getStartY());
                line.toFront();
            }
            for (Data<X, Y> verticalMarker : verticalMarkers) {
                Line line = (Line) verticalMarker.getNode();
                //setColor(line);
                line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);  // 0.5 for crispness
                line.setEndX(line.getStartX());
                line.setStartY(0d);
                line.setEndY(getBoundsInLocal().getHeight());
                line.toFront();
            }      
        }
    }
	
	
	public void setColor(Line line) {
		 // create random object - reuse this as often as possible
        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(0xffffff + 1);

        // format it as hexadecimal string (with hashtag and leading zeros)
        String color = String.format("#%06x", nextInt);
		line.setStyle("-fx-stroke:"+ color);
	}
	
	public void avance_chart(){
		if(proc_Poiss.max_time() > (int)xLineAxis.getUpperBound()){
				double newLower =  xLineAxis.getLowerBound() + (proc_Poiss.max_time() - (int)xLineAxis.getUpperBound());
				double newUpper =  xLineAxis.getUpperBound() + (proc_Poiss.max_time() - (int)xLineAxis.getUpperBound());
				xLineAxis.setLowerBound(newLower);
		 	    xLineAxis.setUpperBound(newUpper);
		}
	 	    
	 	xLineMoyAxis.setUpperBound(index_tot);
	 	xLinePoissAxis.setUpperBound(index_tot);
	}
	
}
