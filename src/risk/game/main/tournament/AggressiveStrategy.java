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
	public String reinforce(int armiesToAllocate, String country) {
		String countryName= getStrongestCountry();
		return countryName;
	}

	/**
	 * This method implements attack
	 * @return number of armies to be moved
	 */
	@Override
	public List<String> attack() { 
		List<String> attackWithTarget = new ArrayList<>();
		attackWithTarget.add(getStrongestCountry());
		attackWithTarget.add(weakestNeighbourForAttack(attackWithTarget.get(0)));
		return attackWithTarget;
	}

	/**
	 * This method implements the fortification phase
	 * @return message of successful fortification
	 */
	@Override
	public String fortify() 
	{
		String targetCountry=getStrongestCountry();
		String sourceCountry=null;
		Country country = data.getCountry(targetCountry);
		List<String> CountryNeighbours = country.getNeighbours();

		int maxArmy = -1;
		sourceCountry = "";
		for (Map.Entry<String, Integer> entry : player.getCountries().entrySet()) {
			if ((CountryNeighbours.indexOf(entry.getKey()) > -1) && (entry.getValue() > maxArmy)) {
				maxArmy = entry.getValue();
				sourceCountry = entry.getKey();
			}
		}

		return sourceCountry+"-"+targetCountry+"-"+maxArmy;
	}

	/**
	 * Finds weakest neighbor
	 * @param countryName Name of the country for whom the weakest neighbor is returned
	 * @return name of the neighbor
	 */
	public String getWeakestNeighbour(String countryName) 
	{
		List<String> neighboursNotConquered = getNeighborsNotConquered(countryName);
		String weakestNeighbour=null,tempneighbor = null;
		int lowestArmies=0, noOfArmies=0;
		
		//for each country in the neighbours list
		for(int j=0;j<neighboursNotConquered.size();j++) 
		{
			// for each player in the list
			for (int i = 0; i < players.size(); i++) 
			{
				Player temp = players.get(i);
				if (temp.getName().equalsIgnoreCase(player.getName()))
					continue;
				// get a particular player's conquered country list
				HashMap<String, Integer> countriesConqueredTmp = temp.getCountries();
				Iterator eachCountryConquered = countriesConqueredTmp.entrySet().iterator();// iterator for countries conqureeed by player
				while (eachCountryConquered.hasNext()) 
				{
					Map.Entry pair = (Map.Entry) eachCountryConquered.next();// if the player has the country in the conqueredcountry list
					//if the key equal to the neighbor teh get number of armies in the country
					if (pair.getKey().equals(neighboursNotConquered.get(j))) {
						noOfArmies  = (int) pair.getValue();
						tempneighbor=neighboursNotConquered.get(j);
						if(lowestArmies==0) {
							lowestArmies=noOfArmies;
							weakestNeighbour=tempneighbor;
						}
						System.out.println("Neigbour:: "+pair.getKey()+"armies::"+pair.getValue());
					}
				}
			}
			//if the number of armies in the neigbour country is lowest till now
			if (noOfArmies < lowestArmies) {
				lowestArmies = noOfArmies;
				weakestNeighbour =tempneighbor;
			}
		}
		System.out.println("The weakest neighbour is "+weakestNeighbour+" with number of armies:" +lowestArmies);
		return weakestNeighbour;
	}


	public String getStrongestCountry() 
	{
		String strongestCountryName = null;
		int maximumArmies=0;
		//get the active player

		// retrieving the countries conquered by the player
		HashMap<String, Integer> countriesConquered = player.getCountries();
		List<String> tried = new ArrayList<>();
		//a country can a strongest country if 1) it has a neighbour that is also conquered and 2) iff it has the highest number of armies

		while (tried.size() != countriesConquered.size()) 
		{
			maximumArmies = 0;
			System.out.println(tried);
			for (Entry<String, Integer> entry : countriesConquered.entrySet()) 
			{
				if (tried.indexOf(entry.getKey()) != -1)
					continue;
				Country countryObj = data.getCountries().get(entry.getKey());
				List<String> neighbours = countryObj.getNeighbours();//list of neighbours of conquered country

				for (String neighbour : neighbours) 
				{
					if (countriesConquered.containsKey(neighbour) && (entry.getValue() > maximumArmies)) //also conquered 
					{
						maximumArmies = entry.getValue();
						strongestCountryName = entry.getKey();
					}
				}
			}
			///here we are getting the strongest country
			/*
            if (strongestCountryName != null) 
            {
                tried.add(strongestCountryName);
                AttackController controller = new AttackController();
                if (controller.getNeighboursForAttack(strongestCountryName).size() == 0) 
                {
                    strongestCountryName = null;
                } 
                else
                    break;
            } 
            else
                break;*/
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
