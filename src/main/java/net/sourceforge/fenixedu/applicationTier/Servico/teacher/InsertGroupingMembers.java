/*
 * Created on 24/Ago/2004
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

public class InsertGroupingMembers {

    protected Boolean run(final String executionCourseCode, final String groupPropertiesCode, final List<String> studentCodes)
            throws FenixServiceException {

        final Grouping groupProperties = AbstractDomainObject.fromExternalId(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        final List<ExecutionCourse> executionCourses = groupProperties.getExecutionCourses();
        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);
        // studentCodes list has +1 entry if "select all" was selected
        int totalStudentsProcessed = 0;

        for (final String studentCode : studentCodes) {
            final Registration registration = AbstractDomainObject.fromExternalId(studentCode);
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

        if (totalStudentsProcessed != 0) {
            List<ExecutionCourse> ecs = groupProperties.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.memberSet.added", Integer.toString(totalStudentsProcessed),
                        sbStudentNumbers.toString(), groupProperties.getName(), ec.getNome(), ec.getDegreePresentationString());
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

    // Service Invokers migrated from Berserk

    private static final InsertGroupingMembers serviceInstance = new InsertGroupingMembers();

    @Service
    public static Boolean runInsertGroupingMembers(String executionCourseCode, String groupPropertiesCode,
            List<String> studentCodes) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupPropertiesCode, studentCodes);
    }

}