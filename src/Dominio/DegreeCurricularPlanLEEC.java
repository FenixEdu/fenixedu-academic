package Dominio;

import java.util.ArrayList;
import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;


/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEEC extends DegreeCurricularPlan implements IDegreeCurricularPlan
{
	public DegreeCurricularPlanLEEC()
	{
		ojbConcreteClass = getClass().getName();
	}

    public List getCurricularCoursesFromArea(IBranch area, AreaType areaType) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);
		
		List curricularCourses = new ArrayList();
		
		int groupsSize = groups.size();

		for (int i = 0; i < groupsSize; i++)
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) groups.get(i);
			
			List courses = curricularCourseGroup.getCurricularCourses();

			int coursesSize = courses.size();

			for (int j = 0; j < coursesSize; j++)
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) courses.get(j);

				if (!curricularCourses.contains(curricularCourse))
				{
					curricularCourses.add(curricularCourse);
				}
			}
		}
		
		return curricularCourses;
	}
}