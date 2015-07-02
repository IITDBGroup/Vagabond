package org.vagabond.explanation.ranking;

import java.util.Iterator;

import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.IExplanationSet;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.explanation.ranking.scoring.IScoringFunction;

public interface IExplanationRanker extends Iterator<IExplanationSet> {

	public void initialize (ExplanationCollection coll);
	public boolean ready ();
	// user confirmed 
	public void confirmExplanation (IBasicExplanation correctExpl);
	
	public IExplanationSet getRankedExpl (int rank);
	public int getScore (int rank);
	public int getIterPos();
	public IExplanationSet previous();
	public boolean hasPrevious();
	public int getNumberOfExplSets ();
	public int getNumberPrefetched ();
	public void resetIter();
	
	public boolean hasAtLeast (int numElem); // check that this ranker can produce at least this many explanation sets
	boolean isFullyRanked();
	public void rankFull();
	public IScoringFunction getScoreF();
}
