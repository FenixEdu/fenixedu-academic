/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests extends FenixService {

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static Set<ExecutionCourse> run(final Student student, final ExecutionYear executionYear) {
		final Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
		for (final Registration registration : student.getRegistrationsSet()) {
			if (registration.isActive()) {
				for (Attends attend : registration.getAssociatedAttendsSet()) {
					final ExecutionCourse executionCourse = attend.getExecutionCourse();
					if (executionCourse.getExecutionYear().equals(executionYear)
							&& student.countDistributedTestsByExecutionCourse(executionCourse) != 0) {
						executionCourses.add(executionCourse);
					}
				}
			}
		}
		return executionCourses;
	}

}