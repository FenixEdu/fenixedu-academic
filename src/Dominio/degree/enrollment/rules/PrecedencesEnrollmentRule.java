package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.precedences.IPrecedence;
import Dominio.precedences.PrecedenceContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class PrecedencesEnrollmentRule implements IEnrollmentRule
{
	protected PrecedenceContext precedenceContext;

	public List apply(List curricularCoursesWhereToApply)
	{
		List curricularCoursesToKeep = new ArrayList();

		for (int i = 0; i < curricularCoursesWhereToApply.size(); i++)
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesWhereToApply.get(i);

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
					if (precedence.evaluate(precedenceContext))
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

		curricularCoursesWhereToApply.clear();
		curricularCoursesWhereToApply.addAll(curricularCoursesToKeep);

		return curricularCoursesWhereToApply;
	}

	/**
	 * Tells what PrecedenceScopeToAplly
	 * @see PrecedenceScopeToApply
	 * @return PrecedenceScopeToApply
	 */
	abstract protected PrecedenceScopeToApply getScopeToApply();
}
