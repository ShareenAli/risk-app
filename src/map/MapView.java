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
		controller.initiateMapCreation();
	}
	
	/**
	 * Takes the user choices in edit mode and passes to controller
	 */
	
	public void editMap()
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
		
		int choice = scan.nextInt();
		scan.nextLine();
		
		switch(choice)
		{
			case 1:				
				controller.continentPrinter();
				controller.editModeAddContinent();
				break;
			case 2:
				controller.continentPrinter();
				controller.editModeRemoveContinent();
				break;
			case 3:
				controller.countryPrinter();
				controller.editModeAddCountry();
				break;
			case 4:
				controller.countryPrinter();
				controller.editModeRemoveCountry();
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
	
	/**
	 * Invokes controller to start storing the map file, sends the filename the user wants
	 */
	
	public void storeMap()
	{
		System.out.println("What name do you want to save this map into?");
		String filename = scan.nextLine();
		controller.storeToFile(filename);
	}
	
}

