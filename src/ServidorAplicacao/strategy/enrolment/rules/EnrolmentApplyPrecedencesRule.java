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
	protected void doApply(StudentEnrolmentContext studentEnrolmentContext)
	{
		List curricularCoursesToKeep = new ArrayList();
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

		studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().clear();
		studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().addAll(curricularCoursesToKeep);
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