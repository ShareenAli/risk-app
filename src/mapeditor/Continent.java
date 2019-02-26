package build1;

import java.util.ArrayList;
import java.util.HashMap;

public class Continent {
	private String name;
	ArrayList<Territory> territoriesIn;
	private int controlValue;
	private HashMap<String, Territory> territoryMap;
	
	public Continent(String name, String value)
	{
		this.name = name;
		controlValue = Integer.parseInt(value);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Territory> getTerritoriesIn() {
		return territoriesIn;
	}
	
	public void setTerritoriesIn(ArrayList<Territory> territoriesIn) {
		this.territoriesIn = territoriesIn;
	}
	
	public int getControlValue() {
		return controlValue;
	}
	
	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}
	
	public HashMap<String, Territory> getTerritoryMap() {
		return territoryMap;
	}
	
	public void setTerritoryMap(HashMap<String, Territory> territoryMap) {
		this.territoryMap = territoryMap;
	}
	
}
