package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domain.Cluster;
import domain.VainPoints;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import protocol.Message;
import protocol.RemoteFunctionsManager.Name;
import server.Server;
import tools.Tools;

public class ServerWindowController implements Initializable{

	@FXML private ImageView imagePane;
	@FXML private TextField widthImage;
	@FXML private ImageView greyImagePane;
	@FXML private AnchorPane histogramPane;

	@FXML private Label groupNumber;
	@FXML private Label groupRange;
	@FXML private ImageView groupImage;

	@FXML private VBox clusterButtonsVBox;

	@FXML private ImageView lineImage;
	@FXML private AnchorPane lineChartPane;
	@FXML private ImageView vainPointsImage;

	private Button[] clustersButtons;

	private Image image;
	private Image greyImage;

	private int curCluster = -1;
	private List<Cluster> clusters = new ArrayList<>();

	Server server;

	public void startServer(int portNumber){

		server = new Server();
		server.getClientManager().setNewNumberOfClientsEvent((n)->System.out.println("Current number of clients: " + n));
		server.startServer(portNumber);
	}
	@FXML
	private void showCluster(ActionEvent event){
		curCluster++;
		if (curCluster >= clusters.size()) curCluster = 0;

		showCluster(curCluster);
	}

	private void showCluster(int index){
		Cluster c = clusters.get(index);
		Image clusterImage = createClusterImage(c);
		groupImage.setImage(clusterImage);
		groupNumber.setText("Cluster " + index + "/" + clusters.size());
		groupRange.setText("(" + c.getFirst() + "-" + c.getLast() + ")");
	}

	private Image createClusterImage(Cluster cluster) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();


		WritableImage clusterImage = new WritableImage(width, height);

		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++){

				if(cluster.isPixelInCluster(x, y))
					clusterImage.getPixelWriter().setArgb(x, y, (0xff<<24)+(0xff<<16)+(0xff<<8));
				else
					clusterImage.getPixelWriter().setArgb(x, y, 0xff<<24);
			}
		return clusterImage;
	}

	@FXML
	private void showImage(ActionEvent event){
		try {
			FileInputStream input = new FileInputStream("data\\0002.jpg");
			int width = -1;
			try {
				width = Integer.parseInt(widthImage.getText());
				image = new Image(input,width,0,true,false);
			} catch(NumberFormatException e){
				image = new Image(input);
			}
			imagePane.setImage(image);

		makeGreyImage();
			greyImagePane.setImage(greyImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void histogram(ActionEvent event){
		long[] histogram = new long[256];
		List<Integer> maxima = new ArrayList<>();
		List<Integer> minima = new ArrayList<>();

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++){
				int pixel = image.getPixelReader().getArgb(x, y);

				int value = (pixel & 0xff);

				histogram[value]++;
			}

		for(int i=0;i<histogram.length; i++)
			System.out.println(i + ":\t" + histogram[i]);

		if (histogram[1] < histogram[0])
			maxima.add(0);
		else
			minima.add(0);

		//TODO kiedy extremum jest na kilku wartoœciach nie jest wykrywane
		//seeking maxima
		for(int i=1;i<histogram.length-1; i++)
			if(histogram[i-1] < histogram[i] && histogram[i+1] < histogram[i])
				maxima.add(i);
		//seeking minima
		for(int i=1;i<histogram.length-1; i++)
			if(histogram[i-1] > histogram[i] && histogram[i+1] > histogram[i])
				minima.add(i);

		if (histogram[histogram.length-1] > histogram[histogram.length-2])
			maxima.add(histogram.length);
		else
			minima.add(histogram.length);




		System.out.println("Maxima(" + maxima.size() + "):");
		for(int i : maxima)
			System.out.println(" - " + i);

		System.out.println("Minima(" + minima.size() + "):");
		for(int i : minima)
			System.out.println(" - " + i);

		for(int i=0;i<256;i++){
			if (minima.contains(i))
				System.out.println(i + ":\tI");
			if (maxima.contains(i))
				System.out.println(i + ":\t  O");
		}

		//seeking clusters
		List<Cluster> clusters = new ArrayList<>();

		if(minima.get(0) != 0)
			clusters.add(new Cluster(image, 0, minima.get(0)-1));

		for(int i=0; i<minima.size()-1; i++)
			clusters.add(new Cluster(image, minima.get(i), minima.get(i+1)-1));

		if(minima.get(minima.size()-1) < 255)
			clusters.add(new Cluster(image, minima.get(minima.size()-1), 255));

		this.clusters = clusters;

		//showing clusters
		System.out.println("Clusters(" + clusters.size() + "):");
		for(int i=0;i<clusters.size(); i++)
			System.out.println(i + ":\t" + clusters.get(i).getFirst() + " - " + clusters.get(i).getLast());

		mergeCluster(0.1f);
		showClastersButton();

		showHistogram(histogram);
	}

	private void mergeCluster(float minInsidePixelsPercentage){
		Cluster c, cPrev, cNext;
		Cluster newCPrev, newCNext;
		boolean change;

//		do{
//			change=false;

			c = clusters.get(clusters.size()-1);
			if(c.getInsidePixelsPercent() < minInsidePixelsPercentage){
				cPrev = clusters.get(clusters.size()-2);
				clusters.add(Cluster.merge(c, cPrev));
				clusters.remove(c);
				clusters.remove(cPrev);
				mergeMessage(c, cPrev);
//				change = true;
//				continue;
			}

			for(int i=clusters.size()-2; i>0; i--){
				c = clusters.get(i);
				if(c.getInsidePixelsPercent() < minInsidePixelsPercentage){
					cPrev = clusters.get(i-1);
					cNext = clusters.get(i+1);
					newCPrev = Cluster.merge(c,cPrev);
					newCNext = Cluster.merge(c,cNext);

					if(newCPrev.getInsidePixelsPercent() > newCNext.getInsidePixelsPercent()){
						clusters.add(i,newCPrev);
						clusters.remove(c);
						clusters.remove(cPrev);
						mergeMessage(c, cPrev);
					} else {
						clusters.add(i,newCNext);
						clusters.remove(c);
						clusters.remove(cNext);
						mergeMessage(c, cNext);
					}
//					change = true;
//					break;
				}
			}
//			if(change) continue;

			c = clusters.get(0);
			if(c.getInsidePixelsPercent() < minInsidePixelsPercentage){
				cNext = clusters.get(1);
				clusters.add(0,Cluster.merge(c, cNext));
				clusters.remove(c);
				clusters.remove(cNext);
				mergeMessage(c, cNext);
//				change = true;
			}
//		}while(change);
	}

	private void mergeMessage(Cluster c1, Cluster c2){
		//-----
		float p1_1 = (int)(c1.getImagePercent()*10000)/100f;
		float p2_1 = (int)(c1.getInsidePixelsPercent()*10000)/100f;
		float p1_2 = (int)(c2.getImagePercent()*10000)/100f;
		float p2_2 = (int)(c2.getInsidePixelsPercent()*10000)/100f;
		System.out.println("Merge clusters: [(" + c1.getFirst() + "-" + c1.getLast() + ") " + p1_1 + "% " + p2_1 + "%] and [(" + c2.getFirst() + "-" + c2.getLast() + ") " + p1_2 + "% " + p2_2 + "%]");
		//-----
	}

	private void showClastersButton(){
		clusterButtonsVBox.getChildren().clear();
		clustersButtons = new Button[clusters.size()];
		for(int i=0; i<clusters.size(); i++){
			Cluster c =  clusters.get(i);
			float p1 = (int)(c.getImagePercent()*10000)/100f;
			float p2 = (int)(c.getInsidePixelsPercent()*10000)/100f;
			clustersButtons[i] = new Button("C" + i + " " + p1 + "% " + p2 + "%");

			clusterButtonsVBox.getChildren().add(clustersButtons[i]);
			int index = i;
			clustersButtons[i].setOnAction((e)->showCluster(index));
		}
	}

	private void showHistogram(long[] histogram){
//        stage.setTitle("Bar Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
            new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Histogram");
        xAxis.setLabel("Value");
        yAxis.setLabel("Amout");

        XYChart.Series<String,Number> series1 = new XYChart.Series<>();
//        series1.setName("2003");
        for(int i=0; i<histogram.length; i++)
        	series1.getData().add(new XYChart.Data<>(i+"", histogram[i]));
//        series1.getData().add(new XYChart.Data(brazil, 20148.82));
//        series1.getData().add(new XYChart.Data(france, 10000));
//        series1.getData().add(new XYChart.Data(italy, 35407.15));
//        series1.getData().add(new XYChart.Data(usa, 12000));

//        XYChart.Series series2 = new XYChart.Series();
//        series2.setName("2004");
//        series2.getData().add(new XYChart.Data(austria, 57401.85));
//        series2.getData().add(new XYChart.Data(brazil, 41941.19));
//        series2.getData().add(new XYChart.Data(france, 45263.37));
//        series2.getData().add(new XYChart.Data(italy, 117320.16));
//        series2.getData().add(new XYChart.Data(usa, 14845.27));
//
//        XYChart.Series series3 = new XYChart.Series();
//        series3.setName("2005");
//        series3.getData().add(new XYChart.Data(austria, 45000.65));
//        series3.getData().add(new XYChart.Data(brazil, 44835.76));
//        series3.getData().add(new XYChart.Data(france, 18722.18));
//        series3.getData().add(new XYChart.Data(italy, 17557.31));
//        series3.getData().add(new XYChart.Data(usa, 92633.68));

//        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        histogramPane.getChildren().add(bc);
//        stage.setScene(scene);
//        stage.show();
	}

	private void makeGreyImage(){
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		System.out.println("width:  " + width);
		System.out.println("height: " + height);

		WritableImage greyImage = new WritableImage(width, height);

		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++){
				int pixel = image.getPixelReader().getArgb(x, y);

				int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff);
				int blue = (pixel & 0xff);

				int grayLevel = (int) ((red + green + blue) / 3);
				grayLevel = 255 - grayLevel;
				int grey = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

//				System.out.println(grey);

				greyImage.getPixelWriter().setArgb(x, y, -grey);
			}
		this.greyImage = greyImage;

//		greyImagePane.setImage(greyImage);
	}


	@FXML
	private void startLineImage(ActionEvent event){
		VainPoints vp = new VainPoints(greyImage);
		int selX = (int) (greyImage.getWidth()/2);
		Image imageWithLine = generateImageWithVLine(image, selX);

		lineImage.setImage(imageWithLine);
		List<Integer> line = vp.getPixelVLine(selX);

//		System.out.println("Line begin");
//		for(int l : line)
//			System.out.println(l);
//		System.out.println("Line end");

//		VainPoints vp = new VainPoints(greyImage);
		for(int i=0; i<100; i++)
			vp.compeYPoints((int) (greyImage.getWidth()/100*i));

		vainPointsImage.setImage(vp.getImageWithPoints());

		showLineChart(line, selX);

		server.sendMessage(0,Message.createAnswer(Name.ClientName, "vains"));
//		server.sendMessage(0,"vains");
	}

	private void showLineChart(List<Integer> line, int selX) {
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.prefWidth(450);

		Series<Number, Number> series = new Series<>();
		Series<Number, Number> series2 = new Series<>();

		List<Integer> deri = Tools.derivative(line);


		for(int i=0; i<line.size(); i++){
			series.getData().add(new Data<Number, Number>(i, line.get(i)));
			if (i>0)
				series2.getData().add(new Data<Number, Number>(i, deri.get(i-1)));
		}
		chart.getData().add(series);
		chart.getData().add(series2);

		lineChartPane.getChildren().clear();
		lineChartPane.getChildren().add(chart);

	}

	private Image generateImageWithVLine(Image image, int selX) {
		int w = (int) image.getWidth();
		int h = (int) image.getHeight();
		WritableImage imageWithLine = new WritableImage(image.getPixelReader(), 0, 0, w, h);

		for(int y=0; y<h; y++)
			imageWithLine.getPixelWriter().setColor(selX, y, Color.DARKORANGE);
//			line.add(image.getPixelReader().getArgb(selX, y) & 0xff);


		return imageWithLine;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}


}
