/*
 * Created on 3/Ago/2003, 21:37:27
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * This service reads all the Curricular Courses for this student's curricular
 * plans, and not only the Curricular Courses currently in execution. For this
 * case use ReadDisciplinesByStudent Created at 3/Ago/2003, 21:37:27
 * 
 */
public class ReadCurricularCoursesByUsername implements IService {

	public List run(String username) throws BDException, ExcepcaoPersistencia {
		List curricularCourses = new LinkedList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		List curricularPlans = sp.getIStudentCurricularPlanPersistente().readByUsername(username);
		for (Iterator iterator = curricularPlans.iterator(); iterator.hasNext();) {
			StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();
			for (Iterator curricularCoursesIterator = studentCurricularPlan.getDegreeCurricularPlan()
					.getCurricularCourses().iterator(); curricularCoursesIterator.hasNext();) {
				CurricularCourse curricularCourse = (CurricularCourse) curricularCoursesIterator
						.next();
				curricularCourses.add(InfoCurricularCourseWithInfoDegree
						.newInfoFromDomain(curricularCourse));
			}
		}

		return curricularCourses;
	}
}