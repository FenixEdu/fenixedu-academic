package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class InsertStudentGroupMembers {

    protected Boolean run(Integer executionCourseID, Integer studentGroupID, Integer groupPropertiesID,
            List<String> studentUsernames) throws FenixServiceException {

        final StudentGroup studentGroup = AbstractDomainObject.fromExternalId(studentGroupID);
        if (studentGroup == null) {
            throw new ExistingServiceException();
        }

        final Grouping grouping = studentGroup.getGrouping();
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
            throw new InvalidArgumentsServiceException("error.editStudentGroupMembers");
        }

        StringBuilder sbStudentNumbers = new StringBuilder("");
        sbStudentNumbers.setLength(0);
        for (final String studentUsername : studentUsernames) {
            if (strategy.checkAlreadyEnroled(grouping, studentUsername)) {
                throw new InvalidSituationServiceException();
            }
            final Attends attend = grouping.getStudentAttend(studentUsername);
            if (sbStudentNumbers.length() != 0) {
                sbStudentNumbers.append(", " + attend.getRegistration().getNumber().toString());
            } else {
                sbStudentNumbers.append(attend.getRegistration().getNumber().toString());
            }
            studentGroup.addAttends(attend);
        }

        // no students means no log entry -- list may contain invalid values, so
        // its size cannot be used to test
        if (sbStudentNumbers.length() != 0) {
            List<ExecutionCourse> ecs = grouping.getExecutionCourses();
            for (ExecutionCourse ec : ecs) {
                GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                        "log.executionCourse.groupAndShifts.grouping.group.element.added", Integer.toString(studentUsernames
                                .size()), sbStudentNumbers.toString(), studentGroup.getGroupNumber().toString(), grouping
                                .getName(), ec.getNome(), ec.getDegreePresentationString());
            }
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final InsertStudentGroupMembers serviceInstance = new InsertStudentGroupMembers();

    @Service
    public static Boolean runInsertStudentGroupMembers(Integer executionCourseID, Integer studentGroupID,
            Integer groupPropertiesID, List<String> studentUsernames) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, studentGroupID, groupPropertiesID, studentUsernames);
    }

}