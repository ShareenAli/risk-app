package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.game.main.TestMainModel;
import test.map.TestMapController;
import test.map.TestMapModel;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestMapModel.class, TestMapController.class, TestMainModel.class})
public class TestSuite {

}
