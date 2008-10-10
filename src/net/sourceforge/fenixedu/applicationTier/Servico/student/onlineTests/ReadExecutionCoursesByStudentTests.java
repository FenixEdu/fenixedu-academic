/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests extends FenixService {

    public List<ExecutionCourse> run(final Student student, final ExecutionYear executionYear) {
	final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
	for (final Registration registration : student.getRegistrationsSet()) {
	    for (Attends attend : registration.getAssociatedAttendsSet()) {
		final ExecutionCourse executionCourse = attend.getExecutionCourse();
		if (executionCourse.getExecutionYear().equals(executionYear)
			&& student.countDistributedTestsByExecutionCourse(executionCourse) != 0) {
		    executionCourses.add(executionCourse);
		}
	    }
	}
	return executionCourses;
    }

}