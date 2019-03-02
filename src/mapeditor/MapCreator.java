package build1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class MapCreator {

	Scanner scan = new Scanner(System.in);
	private static ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	private static ArrayList<Country> TERRITORIES = new ArrayList<Country>();
	private static HashMap<String, Country> COUNTRYMAPPER = new HashMap<String, Country>();
	private static HashMap<String, Continent> CONTINENTMAPPER = new HashMap<String, Continent>();
	int iTrack = 0, jTrack = 0;
	
	public void handler()
	{
		int choice;
		do
		{
			System.out.println("=================================");
			System.out.println("Enter 1 =========== Add Continent");
			System.out.println("Enter 2 =========== Add Countries");
			System.out.println("Enter 3 =========== Exit");
			System.out.println("=================================");
			choice = scan.nextInt();
			scan.nextLine();
			
			switch(choice)
			{
				case 1:
					addContinent();
					break;
				case 2:
					addCountry();
					break;
			}
		}
		while(choice!=3);
		
		
	}

	public void addContinent()
	{
		System.out.println("Enter continent name :: ");
		String continent = scan.nextLine();
		System.out.println("Enter control value :: ");
		int control = scan.nextInt();
		scan.nextLine();
		Continent c = new Continent(continent, control);
		CONTINENTS.add(c);
		CONTINENTMAPPER.put(c.getName(), c);
	}
	
	public static ArrayList<Continent> getCONTINENTS() {
		return CONTINENTS;
	}

	public static void setCONTINENTS(ArrayList<Continent> cONTINENTS) {
		CONTINENTS = cONTINENTS;
	}

	public static ArrayList<Country> getTERRITORIES() {
		return TERRITORIES;
	}

	public static void setTERRITORIES(ArrayList<Country> tERRITORIES) {
		TERRITORIES = tERRITORIES;
	}

	public static HashMap<String, Country> getCOUNTRYMAPPER() {
		return COUNTRYMAPPER;
	}

	public static void setCOUNTRYMAPPER(HashMap<String, Country> cOUNTRYMAPPER) {
		COUNTRYMAPPER = cOUNTRYMAPPER;
	}

	public static HashMap<String, Continent> getCONTINENTMAPPER() {
		return CONTINENTMAPPER;
	}

	public static void setCONTINENTMAPPER(HashMap<String, Continent> cONTINENTMAPPER) {
		CONTINENTMAPPER = cONTINENTMAPPER;
	}

	public void addCountry()
	{
		System.out.println("Enter country name :: ");
		String country = scan.nextLine();
		System.out.println("Enter x value :: ");
		int x = scan.nextInt();
		scan.nextLine();
		System.out.println("Enter y value :: ");
		int y = scan.nextInt();
		scan.nextLine();
		System.out.println("Enter continent name :: ");
		String continent = scan.nextLine();
		System.out.println("Enter neighbors names (space) :: ");
		String neighbors = scan.nextLine();
		
		String nbors[] = neighbors.split(" "); 
		ArrayList<String> neighborList = new ArrayList<String>(Arrays.asList(nbors));

		Country c = new Country(country, continent, x, y);
		c.setNeighbours(neighborList);
		TERRITORIES.add(c);
		COUNTRYMAPPER.put(c.getName(), c);
	}
}
