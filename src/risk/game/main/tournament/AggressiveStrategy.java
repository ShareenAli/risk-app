package risk.game.main.tournament;

import risk.game.settings.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entity.Country;
import entity.Player;
import risk.game.main.MainController;
import risk.game.main.MainModel;
import risk.game.main.phases.PhaseController;;

public class AggressiveStrategy implements Strategy
{
	private MainModel data = MainModel.getInstance();
	SettingsModel settingsData = SettingsModel.getInstance();
	private PhaseController phase = PhaseController.getInstance();
	Player player = data.getPlayer(phase.getModel().getActivePlayer());
	List<Player> players = settingsData.getPlayers();

	/**
	 * This method implements the reinforcement phase 
	 * @param armiesToAllocate number of armies to allocate during the phase
	 * @param country country name to which the armies to be added
	 * @return message of successful allocation
	 */
	@Override
	public String reinforce(String country, int armiesToAllocate) 
	{
		String strongestCountry = getStrongestCountry();
		player.addArmies(strongestCountry, armiesToAllocate);
		return strongestCountry;
	}

	/**
	 * This method implements attack
	 * @return number of armies to be moved
	 */
	@Override
	public List<String> attack() 
	{ 
		List<String> attackerAndEnemy = new ArrayList<>();
		attackerAndEnemy.add(getStrongestCountry());
		attackerAndEnemy.add(getWeakestNeighbour(attackerAndEnemy.get(0)));
		return attackerAndEnemy;
	}

	/**
	 * This method implements the fortification phase
	 * @return message of successful fortification
	 */
	@Override
	public ArrayList<String> fortify() 
	{
		ArrayList<String> fortifyData = new ArrayList<>();
		String sourceCountry;
		String targetCountry = getStrongestCountry();
		Country country = data.getCountry(targetCountry);
		List<String> CountryNeighbours = country.getNeighbours();

		int maxArmy = -999;
		sourceCountry = "";
		for (Map.Entry<String, Integer> entry : player.getCountries().entrySet()) {
			if ((CountryNeighbours.indexOf(entry.getKey()) > -1) && (entry.getValue() > maxArmy)) 
			{
				maxArmy = entry.getValue();
				sourceCountry = entry.getKey();
			}
		}

		fortifyData.add(sourceCountry);
		fortifyData.add(targetCountry);
		fortifyData.add(String.valueOf(maxArmy));
		
		return fortifyData;
	}

	/**
	 * Finds weakest neighbor
	 * @param countryName Name of the country for whom the weakest neighbor is returned
	 * @return name of the neighbor
	 */
	public String getWeakestNeighbour(String countryName) 
	{
		List<String> neighboursNotConquered = getNeighborsNotConquered(countryName);
		String weakestTargetNeighbour = null, tempTarget= null;
		int weakestArmyCount =999999, armyCount=0;
		
		for(int j=0;j<neighboursNotConquered.size();j++) //Traverse over the unconquered neighbours 
		{
			for (int i = 0; i < players.size(); i++) // Check for each player 
			{
				Player eachPlayer = players.get(i);
			
				if (eachPlayer .getName().equalsIgnoreCase(player.getName())) //If player is the source player
					continue;
				
				HashMap<String, Integer> countriesConquered = eachPlayer.getCountries(); //Get all countries for other player
				Iterator eachCountryConquered = countriesConquered.entrySet().iterator();
				
				while (eachCountryConquered.hasNext()) //Iterate over all the countries owned by the other player
				{
					Map.Entry oneCountry = (Map.Entry) eachCountryConquered.next(); //get one country that belongs to the other player
					if (oneCountry.getKey().equals(neighboursNotConquered.get(j))) //if that one country is not conquered by own player 
					{
						armyCount  = (int) oneCountry.getValue();
						tempTarget=neighboursNotConquered.get(j);
						if(armyCount < weakestArmyCount) 
						{
							weakestArmyCount = armyCount;
							weakestTargetNeighbour = tempTarget;
						}
					}
				}
			}
			if(armyCount < weakestArmyCount) 
			{
				weakestArmyCount = armyCount;
				weakestTargetNeighbour = tempTarget;
			}
		}
		return weakestTargetNeighbour;
	}


	public String getStrongestCountry() 
	{
		ArrayList<String> temp = new ArrayList<>();
		String strongestCountryName = null;
		int maxArmies=0;
		HashMap<String, Integer> countriesConquered = player.getCountries();	//get list of countries of player
		
		while (temp.size() != countriesConquered.size())	//traverse all the countries already owned  
		{
			maxArmies = 0;
			for (Entry<String, Integer> entry : countriesConquered.entrySet()) 
			{
				if (temp.indexOf(entry.getKey()) != -1)	//if conquered country not there
					continue;
				Country countryObj = data.getCountries().get(entry.getKey());
				List<String> neighbours = countryObj.getNeighbours(); // retrieve list of neighbors of conquered country

				for (String neighbour : neighbours) //for each neigbor that belong to a conqured country
				{
					if (countriesConquered.containsKey(neighbour) && (entry.getValue() > maxArmies)) //also conquered and check if it has max army 
					{
						maxArmies = entry.getValue();
						strongestCountryName = entry.getKey();
					}
				}
			}
		
            if (strongestCountryName != null) 
            {
            	temp.add(strongestCountryName);

                if (getNeighborsNotConquered(strongestCountryName).size() == 0) 
                {
                    strongestCountryName = null;
                } 
                else
                    break;
            } 
            else
                break;
		}

		return strongestCountryName;
	}

	public List<String> getNeighborsNotConquered (String attackFrom) 
	{
		List<String> allNeighbours = new ArrayList<>();
		Country country = data.getCountry(attackFrom);
		allNeighbours = country.getNeighbours(); //list of all neighbours from the country
		HashMap<String,Integer> conqueredNeighbours = player.getCountries();
		List<String> countriesNotOwned = new ArrayList<>();
		for (int i = 0; i < allNeighbours.size(); i++) 
		{
			String neighbour = allNeighbours.get(i);
			if (!conqueredNeighbours.containsKey(neighbour)) 
			{
				countriesNotOwned.add(neighbour); // add it to the list , if its a neighbour but not yet conquered
			}
		}
		return countriesNotOwned;
	}
}
