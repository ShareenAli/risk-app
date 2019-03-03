package map;
import entity.Country;
import entity.Continent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MapModel {
	
	private static ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	private static ArrayList<Country> TERRITORIES = new ArrayList<Country>();
	private static HashMap<String, Country> COUNTRYMAPPER = new HashMap<String, Country>();
	private static HashMap<String, Continent> CONTINENTMAPPER = new HashMap<String, Continent>();

	public MapModel(ArrayList<Continent> CONTINENTS, ArrayList<Country> TERRITORIES, HashMap<String, Country> COUNTRYMAPPER
			, HashMap<String, Continent> CONTINENTMAPPER)
	{
		this.CONTINENTS = CONTINENTS;
		this.TERRITORIES = TERRITORIES;
		this.COUNTRYMAPPER = COUNTRYMAPPER;
		this.CONTINENTMAPPER = CONTINENTMAPPER;
	}
	
	public void loadedMap(ArrayList<Continent> CONTINENTS, ArrayList<Country> TERRITORIES, HashMap<String, Country> COUNTRYMAPPER
			, HashMap<String, Continent> CONTINENTMAPPER)
	{
		this.CONTINENTS = CONTINENTS;
		this.TERRITORIES = TERRITORIES;
		this.COUNTRYMAPPER = COUNTRYMAPPER;
		this.CONTINENTMAPPER = CONTINENTMAPPER;
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
	
	static ArrayList<Continent> CONTINENTS2;
	static ArrayList<Country> TERRITORIES2;
	static HashMap<String, Country> COUNTRYMAPPER2;
	static HashMap<String, Continent> CONTINENTMAPPER2;
	
	public void writeToFile(String filename)
	{
		//getDetails();
		//getListsOfUser();
		FileWriter fileWriter; 
		try {
			System.out.println("Size is " + CONTINENTS.size());
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
	
}
