package build1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String args[])
	{
		FileReader file1;
		Scanner sn;
		boolean continentsIncoming = false, territoriesIncoming = false;
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		ArrayList<Continent> continents = new ArrayList<Continent>();
		ArrayList<Territory> territories = new ArrayList<Territory>();
		
		try 
		{
			file1 = new FileReader("map.txt");
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
					continents.add(new Continent(words[0], words[1]));
				}
				else if(territoriesIncoming && !s.equals(""))
				{
					String words[] = s.split(",");
					Territory newTerritory = new Territory(words[0], words[1], words[2]);
										
					for(Continent c:continents)
					{
						if(c.getName().equals(words[3]))
						{
							newTerritory.setAtContinent(c);
						}
					}
					
					ArrayList<String> adjacent = new ArrayList<String>(); 
					
					for(int i=4;i<words.length;i++)
					{
						adjacent.add(words[i]);
					}
					
					newTerritory.setNeighborTerritories(adjacent);
					
					territories.add(newTerritory);
					
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		for(int i=0; i<territories.size();i++)
		{
			System.out.println(territories.get(i).getName());
		}
		
	}
	
}
