/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.rules;

import java.util.List;

import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public abstract class EnrolmentPrecedenceRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		List precedenceList = null;

		IStudentCurricularPlan studentActiveCurricularPlan =
			enrolmentContext.getStudentActiveCurricularPlan();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();
			precedenceList = precedenceDAO.readByDegreeCurricularPlan(
					studentActiveCurricularPlan.getDegreeCurricularPlan(),
					getScopeToApply());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from database");
		}

		List curricularCourseToApply = getListOfCurricularCoursesToApply(enrolmentContext);

		doApply(enrolmentContext, precedenceList, curricularCourseToApply);

		return enrolmentContext;
	}
	/**
	 * @param enrolmentContext
	 * @return
	 */
	abstract protected List getListOfCurricularCoursesToApply(EnrolmentContext enrolmentContext);

	abstract protected void doApply(
		EnrolmentContext enrolmentContext,
		List precedenceList,
		List curricularCourseToApply);

	/**
	 * Tells what PrecedenceScopeToAplly
	 * @see PrecedenceScopeToApply
	 * @return PrecedenceScopeToApply
	 */
	abstract protected PrecedenceScopeToApply getScopeToApply();
}
