/*
 * Created on 6/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.PrecedenceRule;
import ServidorPersistente.ExcepcaoPersistencia;
import Tools.dbaccess;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class PrecedenceRuleTest extends BaseEnrolmentRuleTest {

	private dbaccess dbAcessPoint;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(PrecedenceRuleTest.class);
		return suite;
	}
	
	public void testApplyPrecendenceRule(){
		List finalSpan = new ArrayList();
		List initialSpan = null;
		
		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA), new Integer(1));
		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();		
		
		doApplyRule(new PrecedenceRule(), enrolmentContext);
		
		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		
		assertEquals("Final span size:",true, initialSpan.size() > finalSpan.size());
		assertEquals ("Contains assertion!",true,initialSpan.containsAll(finalSpan));
		
		ICurricularCourse curricularCourse = getCurricularCourse("Analise Matematica I", "AMI");
		assertEquals(true, !finalSpan.contains(curricularCourse));
		
		enrolmentContext = getEnrolmentContext(new Integer(1), new TipoCurso(TipoCurso.LICENCIATURA), new Integer(1));		
		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		
		doApplyRule(new PrecedenceRule(), enrolmentContext);
		
		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		
		assertEquals("Final span size:",true, initialSpan.size() == finalSpan.size());
		assertEquals ("Contains assertion!",true,initialSpan.containsAll(finalSpan));
	}
	
	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext){
		try {
			sp.iniciarTransaccao();
			enrolmentRule.apply(enrolmentContext);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace();
			fail("Applying rule!");
		}
	}
	
	
}
