package build1;

import java.util.ArrayList;

public class Territory {

	private int xCord, yCord;
	private String name;
	private Continent atContinent;
	private ArrayList<String> neighborTerritories;
	
	public Territory(String name, String x, String y)
	{
		this.name = name;
		xCord = Integer.parseInt(x);
		yCord = Integer.parseInt(y);
	}
	
	public int getxCord() {
		return xCord;
	}
	public void setxCord(int xCord) {
		this.xCord = xCord;
	}
	public int getyCord() {
		return yCord;
	}
	public void setyCord(int yCord) {
		this.yCord = yCord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Continent getAtContinent() {
		return atContinent;
	}
	public void setAtContinent(Continent atContinent) {
		this.atContinent = atContinent;
	}
	public ArrayList<String> getNeighborTerritories() {
		return neighborTerritories;
	}
	public void setNeighborTerritories(ArrayList<String> neighborTerritories) {
		this.neighborTerritories = neighborTerritories;
	}
	
}
