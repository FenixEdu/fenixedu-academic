package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurso;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalDegreesRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EnrolmentFilterAllOptionalDegreesRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentFilterAllOptionalDegreesRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}

	public void testApplyFilterAllOptionalCoursesRule() {

		List optionalSpan = null;
		EnrolmentContext enrolmentContext = null;

		autentication();
		Object serviceArgs1[] = { userView };
		enrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs1);

//		doApplyRule(new EnrolmentFilterAllOptionalDegreesRule(), enrolmentContext);

		optionalSpan = enrolmentContext.getDegreesForOptionalCurricularCourses();

		assertEquals("Optional Span size:", optionalSpan.size(), 1);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoPersistente cursoPersistente = sp.getICursoPersistente();
			sp.iniciarTransaccao();
			ICurso degree = cursoPersistente.readBySigla("LERCI");
			sp.confirmarTransaccao();
			assertEquals(true, optionalSpan.contains(degree));
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from data base");
		}
	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
//			enrolmentRule.apply(enrolmentContext);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

}