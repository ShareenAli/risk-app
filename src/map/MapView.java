package map;
import entity.Country;
import entity.Continent;
import java.util.Scanner;

/**
 * View class of a map object
 *  
 * @author Farhan Rahman Wasee
 * @version 1.0.0
 */

public class MapView {
	
	Scanner scan = new Scanner(System.in);
	MapController controller = new MapController();
	
	/**
	 * Shows the menu to user
	 */
	
	public void showMenu()
	{
		while(true)
		{
			System.out.println("********************");
			System.out.println("Welcome to map-editor");
			System.out.println("********************");
			System.out.println("********************");
			System.out.println("Press 1 to load map");
			System.out.println("********************");
			System.out.println("Press 2 to add map");
			System.out.println("********************");
			System.out.println("Press 3 to edit map");
			System.out.println("********************");
			System.out.println("Press 4 to store map");
			
			int choice = scan.nextInt();
			scan.nextLine();
			
			switch(choice)
			{
				case 1:
					loadMap();
					break;
				case 2:
					addMap();
					break;
				case 3:
					editMap();
					break;
				case 4:
					storeMap();
					break;
			}
		}
		
		
	}
	
	/**
	 * Calls controller to load an existing map
	 */
	
	public void loadMap()
	{
		System.out.println("Please enter the name of the map file you want to load");
		String filename = scan.nextLine();
		controller.loadExistingMap(filename);
	}
	
	/**
	 * Calls controller for handling map creation
	 */
	
	public void addMap()
	{
		System.out.println("You are now about to create a new map!");
		initiateMapCreation();
	}
	
	/**
	 * Takes the user choices in edit mode and passes to controller
	 */
	
	public void editMap()
	{
		int choice = 0;
				
		do
		{
			System.out.println("You are now in edit mode");
			System.out.println("Press 1 to add Continent");
			System.out.println("********************");
			System.out.println("Press 2 to remove Continent");
			System.out.println("********************");
			System.out.println("Press 3 to add Country");
			System.out.println("********************");
			System.out.println("Press 4 to remove Country");
			System.out.println("********************");
			System.out.println("Press 5 to add neighbors");
			System.out.println("********************");
			System.out.println("Press 6 to remove neighbors");
			System.out.println("********************");
			System.out.println("Press 7 to exit");
			System.out.println("********************");
			
			choice = scan.nextInt();
			scan.nextLine();
			
			switch(choice)
			{
				case 1:				
					controller.continentPrinter();
					System.out.println("Enter continent name :: ");
					String continent = scan.nextLine();
					System.out.println("Enter control value :: ");
					int control = scan.nextInt();
					scan.nextLine();
					controller.addContinent(continent, control);
					break;
				case 2:
					controller.continentPrinter();
					System.out.println("Which continent you want to remove");
					String toBeRemoved = scan.nextLine();
					controller.editModeRemoveContinent(toBeRemoved);
					break;
				case 3:
					controller.countryPrinter();
					System.out.println("Enter country name :: ");
					String country = scan.nextLine();
					System.out.println("Enter x value :: ");
					int x = scan.nextInt();
					scan.nextLine();
					System.out.println("Enter y value :: ");
					int y = scan.nextInt();
					scan.nextLine();
					System.out.println("Enter continent name :: ");
					String continent2 = scan.nextLine();
					System.out.println("Enter neighbors names (comma) :: ");
					String neighbors = scan.nextLine();
					String nbors[] = neighbors.split(","); 
					ArrayList<String> neighborList = new ArrayList<String>(Arrays.asList(nbors));
					controller.addCountry(country, x, y, continent2, neighborList);
					break;
				case 4:
					controller.countryPrinter();
					System.out.println("Which country you want to remove");
					String toBeRemoved2 = scan.nextLine();
					controller.editModeRemoveCountry(toBeRemoved2);
					System.out.println("Na");
					break;
				case 5:
					controller.countryPrinter();
					System.out.println("Enter name of country to add neighbours to");
					String source = scan.nextLine();
					System.out.println("Enter name of neighbors to add (Space separated)");
					String destination = scan.nextLine();
					controller.addNeighbours(source, destination);
					break;
				case 6:
					controller.countryPrinter();
					System.out.println("Enter name of country to remove neighbours from");
					String source2 = scan.nextLine();
					System.out.println("Enter name of neighbors to remove (Space separated)");
					String destination2 = scan.nextLine();
					controller.removeNeighbours(source2, destination2);
					break;
			}
		}
		while(choice!=7);
		
	}
	
	/**
	 * Invokes controller to start storing the map file, sends the filename the user wants
	 */
	
	public void storeMap()
	{
		System.out.println("What name do you want to save this map into?");
		String filename = scan.nextLine();
		controller.storeToFile(filename);
	}
	
	public void initiateMapCreation()
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
					System.out.println("Enter continent name :: ");
					String continent = scan.nextLine();
					System.out.println("Enter control value :: ");
					int control = scan.nextInt();
					scan.nextLine();
					controller.addContinent(continent, control);
					break;
				case 2:
					System.out.println("Enter country name :: ");
					String country = scan.nextLine();
					System.out.println("Enter x value :: ");
					int x = scan.nextInt();
					scan.nextLine();
					System.out.println("Enter y value :: ");
					int y = scan.nextInt();
					scan.nextLine();
					System.out.println("Enter continent name :: ");
					String continent2 = scan.nextLine();
					System.out.println("Enter neighbors names (comma) :: ");
					String neighbors = scan.nextLine();
					String nbors[] = neighbors.split(","); 
					ArrayList<String> neighborList = new ArrayList<String>(Arrays.asList(nbors));
					controller.addCountry(country, x, y, continent2, neighborList);
					break;
			}
		}
		while(choice!=3);
	}
	
}

