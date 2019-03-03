package map;
import entity.Country;
import entity.Continent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * MapModel, has all the relevant information related to the .map file that is being loaded
 * 
 * @author Farhan Rahman Wasee
 * @version 1.0.0
 */

public class MapModel {
	
	/**
	 * Stores all the continents inside it
	 */
	
	private static ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	
	/**
	 * Stores all the countries inside it
	 */
	
	private static ArrayList<Country> TERRITORIES = new ArrayList<Country>();
	
	/**
	 * Created, for a mapping for countries and strings, will be used on alter builds.
	 */
	
	private static HashMap<String, Country> COUNTRYMAPPER = new HashMap<String, Country>();
	private static HashMap<String, Continent> CONTINENTMAPPER = new HashMap<String, Continent>();

	
	/**
	 * Constructor for creating a MapModel
	 * @param CONTINENTS list of continents 
	 * @param TERRITORIES list of countries
	 * @param COUNTRYMAPPER hashmap using string to countries
	 * @param CONTINENTMAPPER hashmap using string to continents
	 */
	
	public MapModel(ArrayList<Continent> CONTINENTS, ArrayList<Country> TERRITORIES, HashMap<String, Country> COUNTRYMAPPER
			, HashMap<String, Continent> CONTINENTMAPPER)
	{
		this.CONTINENTS = CONTINENTS;
		this.TERRITORIES = TERRITORIES;
		this.COUNTRYMAPPER = COUNTRYMAPPER;
		this.CONTINENTMAPPER = CONTINENTMAPPER;
	}
	
	/**
	 * Model updater, that can update the data in the MAP object
	 * @param CONTINENTS list of continents 
	 * @param TERRITORIES list of countries
	 * @param COUNTRYMAPPER hashmap using string to countries
	 * @param CONTINENTMAPPER hashmap using string to continents
	 */
	
	public void updateMap(ArrayList<Continent> CONTINENTS, ArrayList<Country> TERRITORIES, HashMap<String, Country> COUNTRYMAPPER
			, HashMap<String, Continent> CONTINENTMAPPER)
	{
		this.CONTINENTS = CONTINENTS;
		this.TERRITORIES = TERRITORIES;
		this.COUNTRYMAPPER = COUNTRYMAPPER;
		this.CONTINENTMAPPER = CONTINENTMAPPER;
		TERRITORIES.sort((o1, o2) -> o1.getContinent().compareTo(o2.getContinent()));
	}
	
	/**
	 * Gets the list of continents in the map model
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
	
	
	
}
