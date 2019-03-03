package map;
import entity.Country;
import entity.Continent;
import java.util.Scanner;

public class MapView {
	
	Scanner scan = new Scanner(System.in);
	MapController controller = new MapController();
	
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
	
	public void loadMap()
	{
		System.out.println("Please enter the name of the map file you want to load");
		String filename = scan.nextLine();
		controller.loadExistingMap(filename);
	}
	
	public void addMap()
	{
		System.out.println("You are now about to create a new map!");
		controller.initiateMapCreation();
	}
	
	public void editMap()
	{
		
	}
	
	public void storeMap()
	{
		System.out.println("What name do you want to save this map into?");
		String filename = scan.nextLine();
		controller.storeToFile(filename);
	}
	
}

