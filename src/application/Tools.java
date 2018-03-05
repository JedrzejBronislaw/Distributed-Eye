package application;

import java.util.ArrayList;
import java.util.List;

public class Tools {

	public static List<Integer> derivative(List<Integer> line){
		List<Integer> deri = new ArrayList<>();

		deri.add(0);
		for(int i=1; i<line.size(); i++)
			deri.add(line.get(i)-line.get(i-1));
		return deri;
	}
}