package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IPrecedence;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos
 * @author David Santos in Jan 27, 2004
 */

public class EnrolmentApplyPrecedencesForEnrollmentValidationRule extends EnrolmentPrecedenceRule implements IEnrolmentRule
{
	protected void doApply(StudentEnrolmentContext studentEnrolmentContext)
	{
//		EnrolmentValidationResult enrolmentValidationResult = studentEnrolmentContext.getEnrolmentValidationResult();
		for (int i = 0; i < studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().size(); i++)
		{
			ICurricularCourse curricularCourse =
				(ICurricularCourse) studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().get(i);
			List precedenceList = null;
			try
			{
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
				precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse, getScopeToApply());
			} catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
				throw new IllegalStateException("Cannot read from database");
			}
			
			if (precedenceList != null && !precedenceList.isEmpty())
			{
				for (int j = 0; j < precedenceList.size(); j++)
				{
					IPrecedence precedence = (IPrecedence) precedenceList.get(j);
					if (!precedence.evaluate(studentEnrolmentContext))
					{
//					// FIXME [DAVID]: The apropriate error must be sent. This is not done yet because this rule is not used yet.
//					enrolmentValidationResult.setErrorMessage(
//						EnrolmentValidationResult.PRECEDENCE_DURING_ENROLMENT,
//						precedenceCurricularCourse.getName());
					}
				}
			}
		}
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APPLY_DURING_ENROLMENT;
	}

	/**
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(StudentEnrolmentContext studentEnrolmentContext)
	{
		// Put enrolment in optional to
		// List actualEnrolment = enrolmentContext.getActualEnrolments();
		// FIXME [DAVID]: The apropriate list must be returned. This is not done yet because this rule is not used yet.
		return new ArrayList();
	}
}