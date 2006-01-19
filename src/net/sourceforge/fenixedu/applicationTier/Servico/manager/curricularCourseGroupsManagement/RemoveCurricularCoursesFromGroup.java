/*
 * Created on Jul 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.curricularCourseGroupsManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author João Mota
 * 
 */
public class RemoveCurricularCoursesFromGroup implements IService {

	public void run(Integer groupId, Integer[] courseIds) throws FenixServiceException,
			ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
				.getIPersistentCurricularCourse();
		IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
				.getIPersistentCurricularCourseGroup();
		CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) persistentCurricularCourseGroup
				.readByOID(CurricularCourseGroup.class, groupId);

		curricularCourseGroup.getCurricularCourses().clear();
		for (int i = 0; i < courseIds.length; i++) {
			Integer courseId = courseIds[i];
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse
					.readByOID(CurricularCourse.class, courseId);
			curricularCourseGroup.addCurricularCourses(curricularCourse);
		}
	}
}
