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
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;

/**
 * @author João Mota
 * 
 */
public class AddCurricularCoursesToGroup extends Service {

	public void run(Integer groupId, Integer[] courseIds) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentCurricularCourse persistentCurricularCourse = persistentSupport
				.getIPersistentCurricularCourse();
		IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSupport
				.getIPersistentCurricularCourseGroup();
		CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) persistentCurricularCourseGroup
				.readByOID(CurricularCourseGroup.class, groupId);
		for (int i = 0; i < courseIds.length; i++) {
			Integer courseId = courseIds[i];
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse
					.readByOID(CurricularCourse.class, courseId);
			curricularCourseGroup.getCurricularCourses().add(curricularCourse);
		}
	}
}
