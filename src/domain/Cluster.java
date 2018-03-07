package domain;

import javafx.scene.image.Image;

public class Cluster {
	private Image greyImage;
	private int first, last;
	private long amount;
	private float imagePercent;
	private long insidePixels;


	public int getFirst() {
		return first;
	}
	public int getLast() {
		return last;
	}
	public long getAmount() {
		return amount;
	}
	public float getImagePercent() {
		return imagePercent;
	}
	public long getInsidePixels() {
		return insidePixels;
	}
	public float getInsidePixelsPercent() {
		try{
			return (float)insidePixels/amount;
		}catch(ArithmeticException e){
			System.out.println("Ar Error: " + first + "-" + last);
			return 0;
		}
	}
	public Image getGreyImage() {
		return greyImage;
	}

	public Cluster(Image greyImage, int first, int last) {
		this.greyImage = greyImage;
		this.first = first;
		this.last = last;

		compute();
	}


	private void compute() {
		computeAmount();
		computeImagePercent();
		computeInsidePixels();
	}


	private void computeAmount() {
		int w = (int) greyImage.getWidth();
		int h = (int) greyImage.getHeight();

		int amount = 0;

		for(int x=0; x<w; x++)
			for(int y=0; y<h; y++)
				if (isPixelInCluster(x, y)) amount++;

		this.amount = amount;
	}


	private void computeImagePercent() {
		int w = (int) greyImage.getWidth();
		int h = (int) greyImage.getHeight();

		this.imagePercent = amount/(float)(w*h);
	}


	private void computeInsidePixels() {
		int w = (int) greyImage.getWidth();
		int h = (int) greyImage.getHeight();

		int insidePixels = 0;

		for(int x=0; x<w; x++)
			for(int y=0; y<h; y++){
				if(isPixelInCluster(x, y))
					if (isPixelInCluster(x+1, y) &&
						isPixelInCluster(x-1, y) &&
						isPixelInCluster(x, y+1) &&
						isPixelInCluster(x, y-1))
						insidePixels++;
			}

		this.insidePixels = insidePixels;
	}

	private int getPixelValue(int x, int y){
		if (x<0 || y<0 || x>=greyImage.getWidth() || y>=greyImage.getHeight())
			return 0;
		int pixel = greyImage.getPixelReader().getArgb(x, y);

		return pixel & 0xff;
	}


	public boolean isPixelInCluster(int x, int y){
		int value = getPixelValue(x, y);
		return (value>=first && value<=last);
	}

	public static Cluster merge(Cluster c1, Cluster c2) {
		if (c1.getGreyImage() != c2.getGreyImage()) return null;

		int first = Math.min(c1.getFirst(), c2.getFirst());
		int last = Math.max(c1.getLast(), c2.getLast());



		return new Cluster(c1.getGreyImage(), first, last);
	}
}
