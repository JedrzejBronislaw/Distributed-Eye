package application;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class VainPoints {

	private Image image;
	private List<Point> vainPoints = new ArrayList<>();
	final int threshold = -15;

	public Image getImage() {
		return image;
	}

	public VainPoints(Image image) {
		this.image = image;
	}

	public List<Integer> compeYPoints(int selX){
		List<Integer> line = getPixelVLine(selX);
		List<Integer> deri = Tools.derivative(line);

		//seeking vain
		List<Integer> vainPoints = new ArrayList<>();
		for(int i=0; i<deri.size(); i++)
			if(deri.get(i) < threshold)
				vainPoints.add(i);

		addVainPoints(vainPoints, selX);

		return vainPoints;
	}

	private void addVainPoints(List<Integer> vainPoints, int selX) {
		for(int y : vainPoints)
			this.vainPoints.add(new Point(selX, y));

	}

	public List<Integer> getPixelVLine(int selX) {
		List<Integer> line = new ArrayList<>();
		int h = (int) image.getHeight();

		for(int y=0; y<h; y++)
			line.add(image.getPixelReader().getArgb(selX, y) & 0xff);

		return line;
	}

	public Image getImageWithPoints(){
		//drawing veinPoints
		int w = (int) image.getWidth();
		int h = (int) image.getHeight();
		WritableImage vpImage = new WritableImage(image.getPixelReader(), 0, 0, w, h);
		for(Point p : vainPoints){
			vpImage.getPixelWriter().setColor(p.x, p.y, Color.RED);
		}

		return vpImage;
	}
}
