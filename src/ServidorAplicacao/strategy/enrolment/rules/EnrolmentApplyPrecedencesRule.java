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
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class EnrolmentApplyPrecedencesRule extends EnrolmentPrecedenceRule implements IEnrolmentRule
{
	protected void doApply(StudentEnrolmentContext studentEnrolmentContext, List curricularCoursesToApply)
	{
		doIt(studentEnrolmentContext, curricularCoursesToApply, getScopeToApply());
	}

	protected static void doIt(
		StudentEnrolmentContext studentEnrolmentContext,
		List curricularCoursesToApply,
		PrecedenceScopeToApply precedenceScopeToApply)
	{
		List curricularCoursesToKeep = new ArrayList();
		for (int i = 0; i < curricularCoursesToApply.size(); i++)
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToApply.get(i);
			List precedenceList = null;
			try
			{
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
				precedenceList = precedenceDAO.readByCurricularCourse(curricularCourse, precedenceScopeToApply);
			} catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
				throw new IllegalStateException("Cannot read from database");
			}
			
			if (precedenceList == null || precedenceList.isEmpty())
			{
				if (!curricularCoursesToKeep.contains(curricularCourse))
				{
					curricularCoursesToKeep.add(curricularCourse);
				}
			} else
			{
				boolean evaluate = false;
				for (int j = 0; j < precedenceList.size() && !evaluate; j++)
				{
					IPrecedence precedence = (IPrecedence) precedenceList.get(j);
					if (precedence.evaluate(studentEnrolmentContext))
					{
						evaluate = true;
						ICurricularCourse curricularCourseFromPrecedence = precedence.getCurricularCourse();
						if (!curricularCoursesToKeep.contains(curricularCourseFromPrecedence))
						{
							curricularCoursesToKeep.add(curricularCourseFromPrecedence);
						}
					}
				}
			}
		}

		curricularCoursesToApply = curricularCoursesToKeep;
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APPLY_TO_SPAN;
	}

	/**
	 * @param studentEnrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(StudentEnrolmentContext studentEnrolmentContext)
	{
		return studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled();
	}
}