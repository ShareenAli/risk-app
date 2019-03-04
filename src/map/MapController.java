package map;
import entity.Country;
import entity.Continent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Controller class for a map object
 * 
 * @author Farhan Rahman Wasee
 * @version 1.0.0
 */

public class MapController {

	MapModel MAP;
	Scanner scan = new Scanner(System.in);
	private static ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	private static ArrayList<Country> TERRITORIES = new ArrayList<Country>();
	private static HashMap<String, Country> COUNTRYMAPPER = new HashMap<String, Country>();
	private static HashMap<String, Continent> CONTINENTMAPPER = new HashMap<String, Continent>();
	String errorMessage = "";
	
	/**
	 * Starts map editor pipeline
	 */
		
	public void init()
	{
		MapView newmap = new MapView();
		newmap.showMenu();
	}
	
	/**
	 * Loads existing map file
	 * @param filename Name of the map file, ending with .map
	 */
	
	public void loadExistingMap(String filename) {
		// TODO Auto-generated method stub
		FileReader file1;
		Scanner sn;
		boolean continentsIncoming = false, territoriesIncoming = false;
		
		try 
		{
			file1 = new FileReader(filename);
			sn = new Scanner(file1);
			while(sn.hasNext())
			{
				String s = sn.nextLine();
				
				if(s.equals( "[Continents]" ))
				{
					continentsIncoming = true;
					territoriesIncoming = false;
					continue;
				}
				if(s.equals( "[Territories]" ))
				{
					continentsIncoming = false;
					territoriesIncoming = true;
					continue;
				}
				if(s.equals( "[Map]" ))
				{
					continentsIncoming = false;
					territoriesIncoming = false;
					continue;
				}
				else if(continentsIncoming && !s.equals(""))
				{
					String words[] = s.split("=");
					Continent tempContinent = new Continent(words[0], Integer.parseInt(words[1])); 
					getCONTINENTS().add(tempContinent);
					getCONTINENTMAPPER().put(words[0], tempContinent);
				}
				else if(territoriesIncoming && !s.equals(""))
				{
					String words[] = s.split(",");
					Country newTerritory = new Country(words[0], words[3], Double.parseDouble(words[1]), Double.parseDouble(words[2]));
															
					ArrayList<String> adjacent = new ArrayList<String>(); 
					
					for(int i=4;i<words.length;i++)
					{
						adjacent.add(words[i]);
					}
					
					newTerritory.setNeighbours(adjacent);
					
					getTERRITORIES().add(newTerritory);
					getCOUNTRYMAPPER().put(words[0], newTerritory);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		checkForErrors();
		MAP = new MapModel(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
		
	}

	/**
	 * Gets the list of continents
	 * @return CONTINENTS the list of continents
	 */
	
	public static ArrayList<Continent> getCONTINENTS() {
		return CONTINENTS;
	}

	/**
	 * Sets the list of continents in the map model
	 * @param cONTINENTS ArrayList of Continents
	 */
	
	public static void setCONTINENTS(ArrayList<Continent> cONTINENTS) {
		CONTINENTS = cONTINENTS;
	}

	/**
	 * Gets the list of countries in the map model
	 */
	
	public static ArrayList<Country> getTERRITORIES() {
		return TERRITORIES;
	}
	
	/**
	 * Sets the list of countries in the map model
	 * @param tERRITORIES ArrayList of Countries
	 */

	public static void setTERRITORIES(ArrayList<Country> tERRITORIES) {
		TERRITORIES = tERRITORIES;
	}

	/**
	 * Returns the hashmap that does string to country mapping
	 */
	
	public static HashMap<String, Country> getCOUNTRYMAPPER() {
		return COUNTRYMAPPER;
	}
	
	/**
	 * Sets the hashmap that does string to country mapping
	 * @param cOUNTRYMAPPER Hashmap of String, Country pairs
	 */

	public static void setCOUNTRYMAPPER(HashMap<String, Country> cOUNTRYMAPPER) {
		COUNTRYMAPPER = cOUNTRYMAPPER;
	}

	/**
	 * Returns the hashmap that does string to continent mapping
	 */
	
	public static HashMap<String, Continent> getCONTINENTMAPPER() {
		return CONTINENTMAPPER;
	}

	/**
	 * Sets the hashmap that does string to continent mapping
	 * @param cONTINENTMAPPER Hashmap of String, Continent pairs
	 */
	
	public static void setCONTINENTMAPPER(HashMap<String, Continent> cONTINENTMAPPER) {
		CONTINENTMAPPER = cONTINENTMAPPER;
	}
	
	/**
	 * Add another continent in create mode
	 */
	
	public void addContinent(String continent, int controlValue)
	{
		Continent c = new Continent(continent, controlValue);
		CONTINENTS.add(c);
		CONTINENTMAPPER.put(c.getName(), c);
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
	}
	
	/**
	 * Add another country in create mode
	 */

	public void addCountry(String country, int x, int y, String continent, ArrayList<String> neighborList)
	{
		Country c = new Country(country, continent, x, y);
		c.setNeighbours(neighborList);
		TERRITORIES.add(c);
		COUNTRYMAPPER.put(c.getName(), c);
		String neighbors = String.join(",", neighborList);
		addNeighbours(country, neighbors);
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
	}

	/**
	 * stores the map model to file
	 * @param filename Target filename in which map will be saved
	 */
	
	public void storeToFile(String filename) {
		// TODO Auto-generated method stub
		writeToFile(filename);
		checkForErrors();
	}
	
	/**
	 * Prints list of countries
	 */
	
	public void countryPrinter()
	{
		for(Country c:TERRITORIES)
		{
			System.out.println(c.getName());
		}
	}

	/**
	 * Prints list of continents
	 */
	
	public void continentPrinter()
	{
		for(Continent c:CONTINENTS)
		{
			System.out.println(c.getName());
		}
	}
	
	/**
	 * Remove continent in edit mode
	 */

	public void editModeRemoveContinent(String toBeRemoved) {
		
		for(int i = 0; i<CONTINENTS.size();i++)
		{
			if(CONTINENTS.get(i).getName().equalsIgnoreCase(toBeRemoved))
			{
				CONTINENTS.remove(i);
				i--;
			}
		}
		
		for(int i=0;i<TERRITORIES.size();i++)
		{
			if(TERRITORIES.get(i).getContinent().equalsIgnoreCase(toBeRemoved))
			{
				TERRITORIES.remove(i);
				i--;
			}
		}
		
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
	}
	
	
	
	/**
	 * Checks if each continent has at least one country
	 * @return True If there is no error
	 */
	
	public void editModeRemoveCountry(String toBeRemoved) {
		for(int i = 0; i<TERRITORIES.size();i++)
		{
			if(TERRITORIES.get(i).getName().equalsIgnoreCase(toBeRemoved))
			{
				TERRITORIES.remove(i);
			}
		}
		for(int i=0;i<TERRITORIES.size();i++)
		{
			Country c = TERRITORIES.get(i);
			if(c.getNeighbours().contains(toBeRemoved))
			{
				TERRITORIES.remove(i);
				ArrayList<String> temp = c.getNeighbours();
				temp.remove(toBeRemoved);
				c.setNeighbours(temp);
				TERRITORIES.add(c);
				i--;
			}
		}
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
	}
	
	/**
	 * Checks if each continent has at least one neighbouring country
	 * @return True If there is no error
	 */
	
	public boolean hasAdjacentCountry()
	{
		boolean signal = true;
		for(Country c:TERRITORIES)
		{
			if(c.getNeighbours().size()<1)
			{
				signal = false;
				errorMessage.concat(c.getName() + " does not have any neighbor");
			}
		}
		return signal;
	}

	/**
	 * Checks if each country belongs to one continent
	 * @return True If there is no error
	 */
	
	public boolean hasContinent()
	{
		boolean signal = true;
		for(Country c:TERRITORIES)
		{
			if(c.getContinent().equals("") || c.getContinent().equals(null) )
			{
				signal = false;
				errorMessage.concat(c.getName() + " does not have a valid continent");
			}
		}
		return signal;
	}
	
	/**
	 * Checks if each continent has at least one country
	 * @return True If there is no error
	 */
	
	public boolean hasCountry()
	{
		boolean signal = true;
		for(Continent c:CONTINENTS)
		{
			boolean flag=false;
			for(Country ctry:TERRITORIES)
			{
				if(ctry.getContinent().equalsIgnoreCase(c.getName()))
				{
					flag= true;
				}
			}
			if(!flag)
			{
				errorMessage.concat(c.getName() + " has no country.");
				signal = false;
			}
		}
		return signal;
	}
	
	/**
	 * Combines all the error checks and returns a combined result
	 * @return True There is no errors, based on the checks
	 */
	
	public boolean checkForErrors() 
	{
		boolean noNeighbours, noContinent, noCountryInContinent;

		noContinent = hasContinent();
		noNeighbours = hasAdjacentCountry();
		noCountryInContinent = hasCountry();
		
		return noContinent && noNeighbours && noCountryInContinent;
	}
	
	/**
	 * Writes the current map to a .map file
	 * @param filename Name of the file, ending with .map
	 */
	
	public void writeToFile(String filename)
	{
		//getDetails();
		//getListsOfUser();
		FileWriter fileWriter; 
		try {
			fileWriter = new FileWriter(filename);
			writeContinents(fileWriter);
			writeCountries(fileWriter);
			fileWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	 }
	
	/**
	 * Writes the continents to the file
	 * @param fileWriter The filewriter object
	 */
	
	public void writeContinents(FileWriter fileWriter)
	{
		try 
		{
			fileWriter.write("[CONTINENTS]\n");
			
			for(Continent c:CONTINENTS)
			{
				fileWriter.write(c.getName() + "=" + c.getControlValue() + "\n");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the countries to the file
	 * @param fileWriter The filewriter object
	 */
	
	public void writeCountries(FileWriter fileWriter)
	{
		try 
		{
			fileWriter.write("\n[COUNTRIES]\n");
			
			for(Country c:TERRITORIES)
			{
				fileWriter.write(c.getName() + "," + c.getLatitude() + "," +c.getLongitude() + "," + c.getContinent());
				for(int i=0;i<c.getNeighbours().size();i++)
				{
					fileWriter.write(","+c.getNeighbours().get(i));
				}
				fileWriter.write("\n");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addNeighbours(String source, String destination)
	{
		String neighbors[] = destination.split(",");
		Country c;
		
		for(int i = 0; i<TERRITORIES.size();i++)
		{
			if(TERRITORIES.get(i).getName().equalsIgnoreCase(source))
			{
				c = TERRITORIES.get(i);
				TERRITORIES.remove(i);
				for(int k=0;k<neighbors.length;k++)
				{
					c.addNeighbour(neighbors[k]);
				}
				TERRITORIES.add(c);
				break;
			}
		}
		
		for(int i=0;i<neighbors.length;i++) 
		{
			for(int j=0;j<TERRITORIES.size();j++)
			{
				if(TERRITORIES.get(j).getName().equalsIgnoreCase(neighbors[i]))
				{
					c = TERRITORIES.get(j);
					TERRITORIES.remove(j);
					c.addNeighbour(source);
					TERRITORIES.add(c);
					break;
				}
			}
		}
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
		checkForErrors();
	}
	
	public void removeNeighbours(String source, String destination)
	{
		String neighbors[] = destination.split(",");
		Country c;
		
		for(int i = 0; i<TERRITORIES.size();i++)
		{
			if(TERRITORIES.get(i).getName().equalsIgnoreCase(source))
			{
				c = TERRITORIES.get(i);
				TERRITORIES.remove(i);
				for(int k=0;k<neighbors.length;k++)
				{
					c.removeNeighbour(neighbors[k]);
				}
				TERRITORIES.add(c);
				break;
			}
		}
		
		for(int i=0;i<neighbors.length;i++) 
		{
			for(int j=0;j<TERRITORIES.size();j++)
			{
				if(TERRITORIES.get(j).getName().equalsIgnoreCase(neighbors[i]))
				{
					c = TERRITORIES.get(j);
					TERRITORIES.remove(j);
					c.removeNeighbour(source);
					TERRITORIES.add(c);
					break;
				}
			}
		}
		MAP.updateMap(CONTINENTS, TERRITORIES, COUNTRYMAPPER, CONTINENTMAPPER);
		checkForErrors();
	}
	
}
