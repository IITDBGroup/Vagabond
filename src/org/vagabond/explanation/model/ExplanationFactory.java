package org.vagabond.explanation.model;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.vagabond.explanation.model.basic.CopySourceError;
import org.vagabond.explanation.model.basic.CorrespondenceError;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.explanation.model.basic.IBasicExplanation.ExplanationType;
import org.vagabond.explanation.model.basic.InfluenceSourceError;
import org.vagabond.explanation.model.basic.SourceSkeletonMappingError;
import org.vagabond.explanation.model.basic.SuperflousMappingError;
import org.vagabond.explanation.model.basic.TargetSkeletonMappingError;
import org.vagabond.util.LogProviderHolder;

public class ExplanationFactory {

	static Logger log = LogProviderHolder.getInstance().getLogger(ExplanationFactory.class);
	
	private static ExplanationFactory instance = new ExplanationFactory();
	
	private ExplanationFactory () {
		
	}
	
	public static ExplanationCollection newExplanationCollection () {
		return new ExplanationCollection();
	}
	
	public static ExplanationCollection newExplanationCollection 
			(IExplanationSet ... elems) {
		ExplanationCollection result =  new ExplanationCollection();
		
		for(IExplanationSet set: elems) {
			result.addExplSet(set.getExplains().iterator().next(), set);
		}
		
		return result;
	}
	
	public static IExplanationSet newExplanationSet () {
		return new SimpleExplanationSet ();
	}
	
	public static IExplanationSet newExplanationSet (Comparator<IBasicExplanation> comp) {
		return new SimpleExplanationSet(comp);
	}
	
	public static IExplanationSet newExplanationSet (Comparator<IBasicExplanation> comp,
			IBasicExplanation ... elems) {
		SimpleExplanationSet result;
		
		result = new SimpleExplanationSet(comp);
		
		for(IBasicExplanation expl: elems) {
			result.addUnique(expl);
		}
		
		return result;
	}
	
	public static IExplanationSet newExplanationSet (IBasicExplanation ... elems) {
		SimpleExplanationSet result;
		
		result = new SimpleExplanationSet();
		
		for(IBasicExplanation expl: elems) {
			result.addExplanation(expl);
		}
		
		return result;
	}
	 
	public static IBasicExplanation newBasicExpl (ExplanationType type) throws Exception {
		switch(type) {
		case CopySourceError:
			return new CopySourceError();
		case InfluenceSourceError:
			return new InfluenceSourceError();
		case SuperflousMappingError:
			return new SuperflousMappingError();
		case CorrespondenceError:
			return new CorrespondenceError();
		case SourceSkeletonMappingError:
			return new SourceSkeletonMappingError();
		case TargetSkeletonMappingError:
			return new TargetSkeletonMappingError();
		default:
			throw new Exception("now explanation class for type: <" 
					+ type + ">");
		}
	}
}
