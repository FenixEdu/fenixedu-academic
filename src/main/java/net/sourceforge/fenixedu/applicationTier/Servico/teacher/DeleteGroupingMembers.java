/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembers {

    protected Boolean run(String executionCourseCode, String groupingCode, List<String> studentUsernames)
            throws FenixServiceException {

        final Grouping grouping = FenixFramework.getDomainObject(groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidSituationServiceException();
        }

        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);

        for (final String studentUsername : studentUsernames) {
            Attends attend = grouping.getStudentAttend(studentUsername);
            if (sbStudentNumbers.length() != 0) {
                sbStudentNumbers.append(", " + attend.getRegistration().getNumber().toString());
            } else {
                sbStudentNumbers.append(attend.getRegistration().getNumber().toString());
            }
            removeAttendFromStudentGroups(grouping, attend);
            grouping.removeAttends(attend);
        }

        // no students means no log entry -- list may contain invalid values, so
        // its size cannot be used to test
        if (sbStudentNumbers.length() != 0) {
            List<ExecutionCourse> ecs = grouping.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.memberSet.removed",
                        Integer.toString(studentUsernames.size()), sbStudentNumbers.toString(), grouping.getName(), ec.getNome(),
                        ec.getDegreePresentationString());
            }
        }

        return true;
    }

    private void removeAttendFromStudentGroups(final Grouping grouping, Attends attend) {
        if (attend != null) {
            for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
                studentGroup.removeAttends(attend);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteGroupingMembers serviceInstance = new DeleteGroupingMembers();

    @Atomic
    public static Boolean runDeleteGroupingMembers(String executionCourseCode, String groupingCode, List<String> studentUsernames)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, groupingCode, studentUsernames);
    }

}