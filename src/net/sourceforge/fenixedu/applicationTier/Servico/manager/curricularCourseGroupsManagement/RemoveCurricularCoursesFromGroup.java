/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 */
public class RemoveCurricularCoursesFromGroup extends Service {

	public void run(Integer groupId, Integer[] courseIds) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourseGroup curricularCourseGroup = rootDomainObject.readCurricularCourseGroupByOID(groupId);
		curricularCourseGroup.getCurricularCourses().clear();
		for (int i = 0; i < courseIds.length; i++) {
			Integer courseId = courseIds[i];
			CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(courseId);
			curricularCourseGroup.addCurricularCourses(curricularCourse);
		}
	}
    
}
