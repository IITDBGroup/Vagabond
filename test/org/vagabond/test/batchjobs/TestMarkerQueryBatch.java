package org.vagabond.test.batchjobs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vagabond.explanation.generation.CopySourceExplanationGenerator;
import org.vagabond.explanation.generation.prov.SourceProvParser;
import org.vagabond.explanation.marker.AttrValueMarker;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.ISingleMarker;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.marker.MarkerParser;
import org.vagabond.explanation.marker.MarkerQueryBatch;
import org.vagabond.explanation.marker.MarkerSet;
import org.vagabond.explanation.model.ExplanationFactory;
import org.vagabond.explanation.model.IExplanationSet;
import org.vagabond.explanation.model.basic.CopySourceError;
import org.vagabond.explanation.model.prov.ProvWLRepresentation;
import org.vagabond.test.AbstractVagabondTest;
import org.vagabond.util.ConnectionManager;
import org.vagabond.util.PropertyWrapper;


public class TestMarkerQueryBatch extends AbstractVagabondTest {

	static Logger log = Logger.getLogger(TestMarkerQueryBatch.class);
	
	private static MarkerSet markers = new MarkerSet();
	private static MarkerQueryBatch mq;
	
	@BeforeClass
	public static void setUp () throws Exception {
		loadToDB("resource/test/simpleBatchTest.xml");
		
	}
	
	@Test
	public void testMarkerQueryBatchSetName () throws Exception {
		String relName = 
			"SELECT 'person'::text AS rel, person.tid, B'10'::bit varying AS att " +
			"FROM target.person " +
			"WHERE person.livesin IS NULL " + 
			"UNION " + 
			"SELECT 'person'::text AS rel, person.tid, B'01'::bit varying AS att " + 
			"FROM target.person " + 
			"WHERE person.livesin IS NOT NULL";
//		String relName = "errm";
		String predicate = "att & 'B10'::varbit != 'B00'::varbit";
		
		mq = new MarkerQueryBatch(relName, predicate);
		ISingleMarker m0 = new AttrValueMarker("person", "1M", "name");
		ISingleMarker m2 = new AttrValueMarker("person", "2M", "name");
		ISingleMarker m4 = new AttrValueMarker("person", "3M", "name");
		markers.add(m0);
		markers.add(m2);
		markers.add(m4);
		
		assert(mq.equals(markers));
		
	}
	
	@Test
	public void testMarkerQueryBatchSetLivesin () throws Exception {
		String relName = 
			"SELECT 'person'::text AS rel, person.tid, B'10'::bit varying AS att " +
			"FROM target.person " +
			"WHERE person.livesin IS NULL " + 
			"UNION " + 
			"SELECT 'person'::text AS rel, person.tid, B'01'::bit varying AS att " + 
			"FROM target.person " + 
			"WHERE person.livesin IS NOT NULL";
//		String relName = "errm";
		String predicate = "att & 'B01'::varbit != 'B00'::varbit";
		
		mq = new MarkerQueryBatch(relName, predicate);
		ISingleMarker m1 = new AttrValueMarker("person", "1|3|2", "livesin");
		ISingleMarker m3 = new AttrValueMarker("person", "2|1|1", "livesin");
		ISingleMarker m5 = new AttrValueMarker("person", "3|3|2", "livesin");
		markers.add(m1);
		markers.add(m3);
		markers.add(m5);
		
		assert(mq.equals(markers));
		
	}
	
}
