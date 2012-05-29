package org.vagabond.performance.bitmarker;

import java.sql.Connection;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.vagabond.explanation.marker.BitMarkerSet;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.ITupleMarker;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.marker.ScenarioDictionary;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.test.TestOptions;
import org.vagabond.util.ConnectionManager;
import org.vagabond.util.GlobalResetter;

public class TestBitMarkerPerformance {
	static Logger log = Logger.getLogger(TestBitMarkerPerformance.class);
	
	private static IAttributeValueMarker attr;
	
	public static void main (String[] args) throws Exception {
		PropertyConfigurator.configure("resource/test/perfLog4jproperties.txt");
		//loadToDB("resource/exampleScenarios/homeless.xml");
		loadToDB("resource/exampleScenarios/performanceTest1.xml");
		IMarkerSet bitset1 = MarkerFactory.newBitMarkerSet();
		IMarkerSet bitset2 = MarkerFactory.newBitMarkerSet();
		IMarkerSet markerset1 = MarkerFactory.newMarkerSet();
		IMarkerSet markerset2 = MarkerFactory.newMarkerSet();

		
		
		System.out.println("------BitSet Adding Test------");
		AddingTest(bitset1);
		System.out.println("------MarkerSet Adding Test------");
		AddingTest(markerset1);
		
		System.out.println("------BitSet Containing Test------");
		ContainingTest(bitset1);
		System.out.println("------MarkerSet Containing Test------");
		ContainingTest(markerset1);
		
		
//		resetBitSet(bitset1, bitset2);
//		resetMarkerSet(markerset1, markerset2);
		UnionTest();
//		System.out.println("------MarkerSet Union Test------");
//		UnionTest(markerset1, markerset2);
		
		resetBitSet(bitset1, bitset2);
		resetMarkerSet(markerset1, markerset2);
		System.out.println("------BitSet Intesect Test------");
		IntersectTest(bitset1, bitset2);
		System.out.println("------MarkerSet Intersect Test------");
		IntersectTest(markerset1, markerset2);
		
		resetBitSet(bitset1, bitset2);
		resetMarkerSet(markerset1, markerset2);
		System.out.println("------BitSet Diff Test------");
		DiffTest(bitset1, bitset2);
		System.out.println("------MarkerSet Diff Test------");
		DiffTest(markerset1, markerset2);
		
		resetBitSet(bitset1, bitset2);
		resetMarkerSet(markerset1, markerset2);
		System.out.println("------BitSet Clone Test------");
		CloneTest(bitset1, bitset2);
		System.out.println("------MarkerSet Clone Test------");
		CloneTest(markerset1, markerset2);
		
		
		
		
		
		
	}
	public static void  CloneTest(IMarkerSet set1, IMarkerSet set2) throws Exception{
		Random number = new Random();
		int maxRel = 3, maxAttr = 3, maxTid = 99999;
		
		setToElement(set2, 1000, maxRel, maxTid, maxAttr, number);
		
		long test1start = System.currentTimeMillis();
		for(int i = 0; i< 10; i++)
			set1 = set2.cloneSet();
		long test1end = System.currentTimeMillis();
		
		
		long test2start = System.currentTimeMillis();
		for(int i = 0; i< 100; i++)
			set1 = set2.cloneSet();
		long test2end = System.currentTimeMillis();
		
		
		long test3start = System.currentTimeMillis();
		for(int i = 0; i< 1000; i++)
			set1 = set2.cloneSet();
		long test3end = System.currentTimeMillis();
		

		log.debug("Clone a Sets of 1000 elements 10 time: " + (test1end - test1start));
		log.debug("Clone a Sets of 1000 elements 100 time: " + (test2end - test2start));
		log.debug("Clone a Sets of 1000 elements 1000 time: " + (test3end - test3start));
		
	}
	
	public static void resetBitSet(IMarkerSet set1, IMarkerSet set2){
		set1 = MarkerFactory.newBitMarkerSet();
		set2 = MarkerFactory.newBitMarkerSet();
	}
	
	public static void resetMarkerSet(IMarkerSet set1, IMarkerSet set2){
		set1 = MarkerFactory.newMarkerSet();
		set2 = MarkerFactory.newMarkerSet();
	}
	
	public static void  DiffTest(IMarkerSet set1, IMarkerSet set2) throws Exception{
		Random number = new Random();
		int maxRel = 3, maxAttr = 3, maxTid = 99999;
		
		setToElement(set1, 10, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 10, maxRel, maxTid, maxAttr, number);
		
		long test1start = System.currentTimeMillis();
		set1.diff(set2);
		long test1end = System.currentTimeMillis();
		
		setToElement(set1, 100, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 100, maxRel, maxTid, maxAttr, number);
		
		long test2start = System.currentTimeMillis();
		set1.diff(set2);
		long test2end = System.currentTimeMillis();
		
		
		setToElement(set1, 1000, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 1000, maxRel, maxTid, maxAttr, number);
		
		long test3start = System.currentTimeMillis();
		set1.diff(set2);
		long test3end = System.currentTimeMillis();
		

		log.debug("Union Between 2 Sets of 10 elements time: " + (test1end - test1start));
		log.debug("Union Between 2 Sets of 100 elements time: " + (test2end - test2start));
		log.debug("Union Between 2 Sets of 1000 elements time: " + (test3end - test3start));
		
	}
	
	public static void  IntersectTest(IMarkerSet set1, IMarkerSet set2) throws Exception{
		Random number = new Random();
		int maxRel = 3, maxAttr = 3, maxTid = 99999;
		
		setToElement(set1, 10, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 10, maxRel, maxTid, maxAttr, number);
		
		long test1start = System.currentTimeMillis();
		set1.intersect(set2);
		long test1end = System.currentTimeMillis();
		
		setToElement(set1, 100, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 100, maxRel, maxTid, maxAttr, number);
		
		long test2start = System.currentTimeMillis();
		set1.intersect(set2);
		long test2end = System.currentTimeMillis();
		
		
		setToElement(set1, 1000, maxRel, maxTid, maxAttr, number);
		setToElement(set2, 1000, maxRel, maxTid, maxAttr, number);
		
		long test3start = System.currentTimeMillis();
		set1.intersect(set2);
		long test3end = System.currentTimeMillis();
		

		log.debug("Union Between 2 Sets of 10 elements time: " + (test1end - test1start));
		log.debug("Union Between 2 Sets of 100 elements time: " + (test2end - test2start));
		log.debug("Union Between 2 Sets of 1000 elements time: " + (test3end - test3start));
		
	}
	
	
	
	public static void  UnionTest() throws Exception{
		unionSets(10, 200);
		unionSets(100, 200);
		unionSets(1000, 200);
	}
	
	public static void unionSets (int size, int numSets) throws Exception {
		Random number = new Random();
		int maxRel = 3, maxAttr = 3, maxTid = 99999;
		IMarkerSet[] sets;
		IMarkerSet[] bitSets;
		
		sets = genSets(false, numSets, size, maxRel, maxTid, maxAttr, number);
		bitSets = genSets(true, numSets, size, maxRel, maxTid, maxAttr, number);
		
		long test1start = System.currentTimeMillis();
		for(int i = 0; i < sets.length; i+=2)
			sets[i].union(sets[i+1]);
		long test1end = System.currentTimeMillis();
		
		long test2start = System.currentTimeMillis();
		for(int i = 0; i < bitSets.length; i+=2)
			bitSets[i].union(bitSets[i+1]);
		long test2end = System.currentTimeMillis();
		
		log.debug("------ SIZE " + size + " ------");
		log.debug("MARKER: Union Between " + numSets + " Sets of " + size + " elements each time: " + (test1end - test1start));
		log.debug("BITMARKER: Union Between " + numSets + " Sets of " + size + " elements each time: " + (test2end - test2start));
	}
	
	
	public static IMarkerSet[] genSets (boolean bit, int numSets, int card, int maxRel, int maxTid, int maxAttr, Random number) throws Exception {
		IMarkerSet[] result = new IMarkerSet[numSets];
		
		for(int i = 0; i < numSets; i++) {
			if (bit)
				result[i] = MarkerFactory.newBitMarkerSet();
			else
				result[i] = MarkerFactory.newMarkerSet();

			setToElement(result[i], card, maxRel, maxTid, maxAttr, number);
		}
		
		return result;
	}
	
	public static void setToElement(IMarkerSet set1, int setNumber, int maxRel, int maxTid, int maxAttr, Random number) throws Exception{
		while(set1.getNumElem() < setNumber)
			set1.add(randMarker(maxRel, maxTid, maxAttr, number));
	}
	
	
	
	public static void  ContainingTest(IMarkerSet set1) throws Exception{
		Random number = new Random();
		int maxRelid = 3, maxAttr = 3, maxTid = 99999;
		
		long before = System.currentTimeMillis();
		for(int i = 0; i< 10; i++){
			set1.contains(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		
		long breakpoint1 = System.currentTimeMillis();
		for(int i = 0; i< 100; i++){
			set1.contains(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		long breakpoint2 = System.currentTimeMillis();
		for(int i = 0; i< 890; i++){
			set1.contains(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		long end = System.currentTimeMillis();
		log.debug("Set Adding 10 elements time: " + (breakpoint1 - before));
		log.debug("Set Adding 100 elements time: " + (breakpoint2 - breakpoint1));
		log.debug("Set Adding 1000 elements time: " + (end - before));
		
	}
	
	

	public static void  AddingTest(IMarkerSet set1) throws Exception{
		Random number = new Random();
		int maxRelid = 3, maxAttr = 3, maxTid = 99999;
		
		long before = System.currentTimeMillis();
		for(int i = 0; i< 10; i++){
			set1.add(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		
		long breakpoint1 = System.currentTimeMillis();
		for(int i = 0; i< 100; i++){
			set1.add(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		long breakpoint2 = System.currentTimeMillis();
		for(int i = 0; i< 890; i++){
			set1.add(randMarker(maxRelid, maxTid, maxAttr, number));
		}
		long end = System.currentTimeMillis();
		log.debug("Number of element added " + set1.getNumElem() );
		log.debug("Set Adding 10 elements time: " + (breakpoint1 - before));
		log.debug("Set Adding 100 elements time: " + (breakpoint2 - breakpoint1));
		log.debug("Set Adding 1000 elements time: " + (end - before));
		
	}
	
	private static IAttributeValueMarker randMarker (int maxRel, int maxTid, int maxAttr, Random number) throws Exception {
		int relid;
		int attrid;
		int tid;
		String tidString;
		
		relid = number.nextInt(3);
		tid = number.nextInt(99999);
		attrid = number.nextInt(3);
		tidString = ScenarioDictionary.getInstance().getTidString(tid, relid);
		return  MarkerFactory.newAttrMarker(relid,tidString,attrid);
	}

	public static void loadToDB (String fileName) throws Exception {
		Connection con = TestOptions.getInstance().getConnection();
		
		GlobalResetter.getInstance().reset();
		ModelLoader.getInstance().loadToInst(fileName);
		DatabaseScenarioLoader.getInstance().loadScenario(con);
		ConnectionManager.getInstance().setConnection(con);
		ScenarioDictionary.getInstance().initFromScenario();
	}

	
}