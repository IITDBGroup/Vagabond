package org.vagabond.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.xmlbeans.XmlException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.explanation.marker.ScenarioDictionary;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.model.ValidationException;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.mapping.scenarioToDB.MaterializedViewsBroker;
import org.vagabond.util.ConnectionManager;

public abstract class AbstractVagabondTest {

	@BeforeClass
	public static void setUpLogger () throws FileNotFoundException, IOException, XmlException, ValidationException, SQLException, ClassNotFoundException {
		PropertyConfigurator.configure("resource/test/testLog4jproperties.txt");
		QueryHolder.getInstance().loadFromDir(new File ("resource/queries"));
	}
	
	@AfterClass
	public static void tearDown () throws Exception {
		TestOptions.getInstance().close();
		ConnectionManager.getInstance().closeCon();
	}
	
	@Before
	public void settUp () throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		Connection con = TestOptions.getInstance().getConnection();
		ConnectionManager.getInstance().setConnection(con);
	}
	
	@After
	public void cleanUpp() throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
		TestOptions.getInstance().close();
		ConnectionManager.getInstance().closeCon();
	}
	
	public static void setSchemas (String fileName) throws Exception {
		MapScenarioHolder holder;
		
		holder = ModelLoader.getInstance().load(new File(fileName));
		MapScenarioHolder.getInstance().setDocument(holder.getDocument());
		ScenarioDictionary.getInstance().setSchemas(
				holder.getScenario().getSchemas().getSourceSchema(),
				holder.getScenario().getSchemas().getTargetSchema());
		ScenarioDictionary.getInstance().setMappings(
				holder.getScenario().getMappings());
		ScenarioDictionary.getInstance().initTidMappingGenerating();
		ScenarioDictionary.getInstance().createOffsetsMapping ();
	}
	
	public static void loadToDB (String fileName) throws Exception {
		Connection con = TestOptions.getInstance().getConnection();
		ModelLoader.getInstance().loadToInst(fileName);
		ScenarioDictionary.getInstance().initFromScenario();
		DatabaseScenarioLoader.getInstance().loadScenario(con);
	}
	
}
