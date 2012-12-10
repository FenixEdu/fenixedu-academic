package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.StudentGroup;

public class InsertStudentGroupMembers extends FenixService {

    public Boolean run(Integer executionCourseID, Integer studentGroupID, Integer groupPropertiesID, List<String> studentUsernames)
	    throws FenixServiceException {

	final StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupID);
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

}
