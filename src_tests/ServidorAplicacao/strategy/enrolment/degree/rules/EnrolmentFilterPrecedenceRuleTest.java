/*
 * Created on 6/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class EnrolmentFilterPrecedenceRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentFilterPrecedenceRuleTest.class);
		return suite;
	}
	
	public void testApplyEnrolmentFilterPrecedenceRule(){
//		List finalSpan = new ArrayList();
//		List initialSpan = null;
//		
//		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA));
//		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();		
//		
//		doApplyRule(new EnrolmentApplyPrecedencesRule(), enrolmentContext);
//		
//		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
//		
//		assertEquals("Final span size:",true, initialSpan.size() >= finalSpan.size());
//		assertEquals ("Contains assertion!",true,initialSpan.containsAll(finalSpan));
//		
//		enrolmentContext = getEnrolmentContext(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA));		
//		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
//		
//		doApplyRule(new EnrolmentApplyPrecedencesRule(), enrolmentContext);
//		
//		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
//		
//		assertEquals("Final span size:",true, initialSpan.size() == finalSpan.size());
//		assertEquals ("Contains assertion!",true,initialSpan.containsAll(finalSpan));
	}
	
	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext){
		try {
			sp.iniciarTransaccao();
//			enrolmentRule.apply(enrolmentContext);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Applying rule!");
		}
	}
}
