/*
 * Created on 6/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author jpvl
 */
public class PrecedenceRuleTest extends TestCase {

	public PrecedenceRuleTest(String testName){
		super(testName);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(PrecedenceRuleTest.class);
		return suite;
	}
	
	public void testCurricularCoursePrecendence(){
		fail("not done yet!");	
	}
	
	public void testNumberOfCurricularCourseDonePrecedence(){
		fail("not done yet!");
	}
	
	public void testComposedPrecedence(){
		fail("not done yet!");
	}
}
