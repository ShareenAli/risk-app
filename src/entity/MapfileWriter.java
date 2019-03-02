package build1;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MapfileWriter {

	static MapEditor mapedit = new MapEditor();
	static ArrayList<Continent> CONTINENTS2 = new ArrayList<Continent>();
	static ArrayList<Country> TERRITORIES2 = new ArrayList<Country>();
	static HashMap<String, Country> COUNTRYMAPPER2 = new HashMap<String, Country>();
	static HashMap<String, Continent> CONTINENTMAPPER2 = new HashMap<String, Continent>();
	
	public static void getDetails()
	{
		mapedit.getMapDetails("map.txt");
		CONTINENTS2 = mapedit.getCONTINENTS();
		TERRITORIES2 = mapedit.getTERRITORIES();
		COUNTRYMAPPER2 = mapedit.getCOUNTRYMAPPER();
		CONTINENTMAPPER2 = mapedit.getCONTINENTMAPPER();
	}
	
	public void writeToFile(String fileName)
	{
		getDetails();
		FileWriter fileWriter; 
		try {
			System.out.println("Size is " + CONTINENTS2.size());
			fileWriter = new FileWriter(fileName);
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
			
			for(Continent c:CONTINENTS2)
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
			
			for(Country c:TERRITORIES2)
			{
				fileWriter.write(c.getName() + "," + c.getLatitude() + "," +c.getLongitude() + c.getContinent());
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
