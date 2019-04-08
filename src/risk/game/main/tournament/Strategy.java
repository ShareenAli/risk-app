package risk.game.main.tournament;

import java.util.List;

public interface Strategy {
	
	/**
     * To be overriden; Containing code to reinforce for each particular strategy  
     * @param armiesToAllocate armies for allocating to a country
     * @param country name of the country for adding the army to
     * @return String country name of the country for adding the army to
     */
	String reinforce(int armiesToAllocate, String country);
	
	/**
     * To be overriden; Containing code to attack for each particular strategy
     * @return Strongest country and target country
     */
	List<String> attack();
	
	/**
     * To be overriden; Containing code to fortify for each particular strategy
     */
	String fortify();
}
