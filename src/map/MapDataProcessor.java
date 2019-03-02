package map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import entity.Country;
import entity.Continent;

public class MapDataProcessor {
	
	private static ArrayList<Continent> CONTINENTS = new ArrayList<Continent>();
	private static ArrayList<Country> TERRITORIES = new ArrayList<Country>();
	private static HashMap<String, Country> COUNTRYMAPPER = new HashMap<String, Country>();
	private static HashMap<String, Continent> CONTINENTMAPPER = new HashMap<String, Continent>();
	
	public void getMapDetails(String filename)
	{
		FileReader file1;
		Scanner sn;
		boolean continentsIncoming = false, territoriesIncoming = false;
		int mapperTracker = 0;
		
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
					mapperTracker++;
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
		
		for(int i=0; i<getTERRITORIES().size();i++)
		{
			System.out.println(getTERRITORIES().get(i).getName());
		}
		
		System.out.println("The current state is " + continentHasTerritory());
		
	}
	
	public void addContinent()
	{
		Scanner scan = new Scanner(System.in);
		String countryName;
		int controlValue;
		
		System.out.println("Enter the name of the continent");
		countryName = scan.nextLine();
		System.out.println("Enter the control value of the continent");
		controlValue = scan.nextInt();
		
		
		
	}
	
	public static boolean continentHasTerritory()
	{
		for(Continent c: getCONTINENTS())
		{
			for(Country t: getTERRITORIES())
			{
				if(t.getContinent().equals(c.getName()))
					return true;
			}
		}
		return false;
	}
	/*
	public void territoryHasContinent()
	{
		Continent residesHere;
		
		for(Country t: TERRITORIES)
		{
			for(Country t: TERRITORIES)
			{
				if(t.getAtContinent().getName().equals(c.getName()))
					return true;
			}
		}
		return false;
	}
	*/

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
}

