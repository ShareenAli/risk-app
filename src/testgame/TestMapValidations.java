package testgame.main;


import src.map.MapView;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class verifies the functionality for the map editor 
 *
 * @author Jasmeet
 * @version 1.0.0
 */
public class TestMapValidations{
	  private MapView mapView = new MapView();
	   private MapController mapController=new MapController();
	   
	    /**
	     * Test Case method for reading an invalid map file
	     */
		@Test
		public void loadMap() {
			String fileName = System.getProperty("user.dir") + "\\src\\Test\\Testing_Controller\\testMap.map";
			assertFalse(this.mapController.loadExistingMap(fileName));
		}
	    /**
	     * Test Case method for adding a continent in map
	     */
	    
	    @Test
	    public void addMap(){
	    	String continent ="ASIA";
	    	int control = 7;
	    	int choice=1;
	    	assertTrue(this.mapController.addContinent(continent,control));
	    	
	    	}
	    /**
	     * Test Case method for editing a map by adding a country
	     */
	   @Test
	   public void editMapByAddingCountry(){
		String country="CHINA";		
	   	int choice=3;
	    assertTrue(this.mapView.editMap());	
	   }
	   
	   
	    /**
	     * Test Case method for editing a map by removing a country
	     */
	   @Test
	   public void editMapByRemovingCountry(){
		String toBeRemoved2="CHINA";		//country to be removed
	   	int choice=4;
	   	assertTrue(this.mapView.editMap());	
	   }
	   
	   /**
	    * Test Case method for editing a map by adding a continent
	    */
	  @Test
	  public void editMapByAddingContinent(){
		String continent="ASIA";		
		int control=2;
	  	int choice=1;
	  	assertTrue(this.mapView.editMap());	
	  }
	  
	   
	   /**
	    * Test Case method for editing a map by removing a continent
	    */
	  @Test
	  public void editMapByRemovingContinent(){
		String toBeRemoved="AFRICA";		//continent to be removed
	  	int choice=2;
	  	assertTrue(this.mapView.editMap());	
	  }
	  
	   /**
	    * Test Case method for storing a map
	    */
	  @Test
	  public void StoreMap(){
		  String fileName = System.getProperty("user.dir") + "\\src\\Test\\Testing_Controller\\PlayMap.map";
		  assertTrue(this.mapView.storeMap());	
	  }
}
