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
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterSemesterRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import Tools.dbaccess;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class SemesterFilterRuleTest extends BaseEnrolmentRuleTest {

	private dbaccess dbAcessPoint;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SemesterFilterRuleTest.class);
		return suite;
	}

	public void testApplySemesterRule() {
		List finalSpan = new ArrayList();
		List initialSpan = null;

		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(3), new TipoCurso(TipoCurso.LICENCIATURA), new Integer(1));
		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();

		doApplyRule(new EnrolmentFilterSemesterRule(), enrolmentContext);

		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();

		assertEquals("Inital span Size:", true, initialSpan.size() == 6);
		assertEquals("Final span size:", true, finalSpan.size() == 5);
		assertEquals("Contains assertion!", true, initialSpan.containsAll(finalSpan));

		ICurricularCourse curricularCourse = getCurricularCourse("Analise Matematica I", "AMI");
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, finalSpan.contains(curricularCourseScope));

		curricularCourse = getCurricularCourse("Analise Matematica II", "AMII");
		curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, !finalSpan.contains(curricularCourseScope));

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
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
