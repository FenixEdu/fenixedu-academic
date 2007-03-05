/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests extends Service {

    public Object run(final Registration someRegistration) throws ExcepcaoPersistencia {
	final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();

	final Student student = someRegistration.getStudent();
	for (final Registration registration : student.getRegistrationsSet()) {
	    for (Attends attend : registration.getAssociatedAttendsSet()) {
		final ExecutionCourse executionCourse = attend.getExecutionCourse();
		if (student.countDistributedTestsByExecutionCourse(executionCourse) != 0) {
		    infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
		}
	    }
	}

	return infoExecutionCourses;
    }

}