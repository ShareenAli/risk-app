package map;

public class MapMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MapfileWriter wt = new MapfileWriter();
		
		wt.writeToFile("output.txt");
		System.out.println("Its going!");
	}

}
