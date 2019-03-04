package testgame.main;


import src.map.MapView;
import org.junit.Before;
import org.junit.Test;

public class TestMainModel {
    private MapView mapView = new MapView();
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /**
     * Test Case method for reading an invalid map file
     */
    @Test
    public void loadMap(){
    	String fileName=System.getProperty("user.dir")+"//src//Test//Testing_Controller//testMap.map";
    	exceptionRule.expect(Exception.class);
    	exceptionRule.expectMessage("File Invalid");
   		this.mapView.loadMap(fileName);	
    }
    
   
}
