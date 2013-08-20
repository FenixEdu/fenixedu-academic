/*
 * Created on 08/Mars/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertStudentsInGrouping {

    protected Boolean run(final String executionCourseCode, final String groupPropertiesCode, final String[] selected)
            throws FenixServiceException {

        if (selected == null) {
            return Boolean.TRUE;
        }

        final Grouping groupProperties = AbstractDomainObject.fromExternalId(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        final List<ExecutionCourse> executionCourses = groupProperties.getExecutionCourses();
        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);
        // studentCodes list has +1 entry if "select all" was selected
        int totalStudentsProcessed = 0;

        for (final String number : selected) {
            if (number.equals("Todos os Alunos")) {
            } else {
                Registration registration = AbstractDomainObject.fromExternalId(number);
                if (!studentHasSomeAttendsInGrouping(registration, groupProperties)) {
                    final Attends attends = findAttends(registration, executionCourses);
                    if (attends != null) {
                        if (sbStudentNumbers.length() != 0) {
                            sbStudentNumbers.append(", " + registration.getNumber().toString());
                        } else {
                            sbStudentNumbers.append(registration.getNumber().toString());
                        }
                        totalStudentsProcessed++;
                        groupProperties.addAttends(attends);
                    }
                }
            }
        }

        if (totalStudentsProcessed > 0) {
            List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.attends.added", Integer.toString(totalStudentsProcessed),
                        sbStudentNumbers.toString(), groupProperties.getName(), ec.getNome(), ec.getDegreePresentationString());
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

    // Service Invokers migrated from Berserk

    private static final InsertStudentsInGrouping serviceInstance = new InsertStudentsInGrouping();

    @Service
    public static Boolean runInsertStudentsInGrouping(String executionCourseCode, String groupPropertiesCode, String[] selected)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupPropertiesCode, selected);
    }

}