/*
 * Created on 08/Mars/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertStudentsInGrouping extends Service {

    public Boolean run(final Integer executionCourseCode, final Integer groupPropertiesCode, final String[] selected)
            throws FenixServiceException, ExcepcaoPersistencia {

	if (selected == null) {
            return Boolean.TRUE;
        }

        final Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        final List<ExecutionCourse> executionCourses = groupProperties.getExecutionCourses();

        for (final String number : selected) {
            if (number.equals("Todos os Alunos")) {
            } else {
                Registration registration = rootDomainObject.readRegistrationByOID(new Integer(number));
                if (!studentHasSomeAttendsInGrouping(registration, groupProperties)) {
                    final Attends attends = findAttends(registration, executionCourses);
                    if (attends != null) {
                	groupProperties.addAttends(attends);
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

    public static boolean studentHasSomeAttendsInGrouping(final Registration registration, final Grouping groupProperties) {
	for (final Attends attends : groupProperties.getAttendsSet()) {
	    final Registration otherRegistration = attends.getRegistration();
	    if (registration == otherRegistration) {
		return true;
	    }
	}
	return false;
    }

    public static Attends findAttends(final Registration registration, final List<ExecutionCourse> executionCourses) {
	for (final ExecutionCourse executionCourse : executionCourses) {
	    final Attends attends = registration.readAttendByExecutionCourse(executionCourse);
	    if (attends != null) {
		return attends;
	    }
	}
	return null;
    }

}