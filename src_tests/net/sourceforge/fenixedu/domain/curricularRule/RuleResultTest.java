package net.sourceforge.fenixedu.domain.curricularRule;

import junit.framework.TestCase;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;

public class RuleResultTest extends TestCase {
    
    public void testMixed() {

	RuleResult result = RuleResult.createFalse();
	
	result = result.or(RuleResult.createFalse()).or(RuleResult.createTrue()).and(RuleResult.createFalse());
	assertTrue("MIXED: Result should be false", result.isFalse());
	
	result = result.or(RuleResult.createTrue()).or(RuleResult.createFalse());
	assertTrue("MIXED: Result should be true", result.isTrue());
	
	result = result.and(RuleResult.createTrue()).and(RuleResult.createTrue());
	assertTrue("MIXED: Result should be true", result.isTrue());
	
	result = result.and(RuleResult.createFalse()).or(RuleResult.createTrue());
	assertTrue("MIXED: Result should be true", result.isTrue());
	
	result = result.and(RuleResult.createFalse()).and(RuleResult.createTrue());
	assertTrue("MIXED: Result should be false", result.isFalse());
    }

    public void testOR() {
	RuleResult[] ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createFalse(), RuleResult.createFalse()};
	
	RuleResult result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be false", result.isFalse());
	
	ruleResults = new RuleResult[] {RuleResult.createTrue(), RuleResult.createFalse()};
	result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createTrue(), RuleResult.createTrue()};
	result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createTrue(), RuleResult.createTrue(), RuleResult.createNA()};
	result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createFalse(), RuleResult.createNA()};
	result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be false", result.isFalse());
	
	ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createTrue(), RuleResult.createNA()};
	result = RuleResult.createFalse();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createNA(), RuleResult.createNA(), RuleResult.createNA()};
	result = RuleResult.createNA();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.or(ruleResult);
	}
	assertTrue("OR: Result should be NA", result.isNA());	
    }

    public void testAND() {
	RuleResult[] ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createFalse(), RuleResult.createTrue()};
	
	RuleResult result = RuleResult.createTrue();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be false", result.isFalse());
	
	ruleResults = new RuleResult[] {RuleResult.createTrue(), RuleResult.createTrue()};
	result = RuleResult.createTrue();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createTrue(), RuleResult.createTrue(), RuleResult.createNA()};
	result = RuleResult.createTrue();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be true", result.isTrue());
	
	ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createFalse(), RuleResult.createNA()};
	result = RuleResult.createTrue();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be false", result.isFalse());
	
	ruleResults = new RuleResult[] {RuleResult.createFalse(), RuleResult.createTrue(), RuleResult.createNA()};
	result = RuleResult.createTrue();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be false", result.isFalse());
	
	ruleResults = new RuleResult[] {RuleResult.createNA(), RuleResult.createNA(), RuleResult.createNA()};
	result = RuleResult.createNA();
	for (final RuleResult ruleResult : ruleResults) {
	    result = result.and(ruleResult);
	}
	assertTrue("AND: Result should be NA", result.isNA());
    }

}
