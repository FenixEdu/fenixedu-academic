/*
 * Created on 24/Ago/2004
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

public class InsertGroupingMembers extends Service {

    public Boolean run(final Integer executionCourseCode, final Integer groupPropertiesCode, final List studentCodes)
            throws FenixServiceException, ExcepcaoPersistencia {

        final Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        final List<ExecutionCourse> executionCourses = groupProperties.getExecutionCourses();

        for (final Integer studentCode : (List<Integer>) studentCodes) {
            final Registration registration = rootDomainObject.readRegistrationByOID(studentCode);
            if (!studentHasSomeAttendsInGrouping(registration, groupProperties)) {
        	final Attends attends = findAttends(registration, executionCourses);
        	if (attends != null) {
        	    groupProperties.addAttends(attends);
        	}
            }
        }

        return Boolean.TRUE;
    }

    public static boolean studentHasSomeAttendsInGrouping(final Registration registration, final Grouping groupProperties) {
	return InsertStudentsInGrouping.studentHasSomeAttendsInGrouping(registration, groupProperties);
    }

    private static Attends findAttends(final Registration registration, final List<ExecutionCourse> executionCourses) {
	return InsertStudentsInGrouping.findAttends(registration, executionCourses);
    }
}