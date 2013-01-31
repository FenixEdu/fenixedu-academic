/*
 * Created on 23/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * @author joaosa and rmalo 31/Ago/2004
 */

public class DeleteStudentGroupMembers extends FenixService {

	public Boolean run(Integer executionCourseID, Integer studentGroupID, Integer groupPropertiesID, List studentUsernames)
			throws FenixServiceException {

		final StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupID);
		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		final Grouping grouping = studentGroup.getGrouping();
		final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
		final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

		if (!strategy.checkStudentsUserNamesInGrouping(studentUsernames, grouping)) {
			throw new InvalidArgumentsServiceException();
		}

		StringBuilder sbStudentNumbers = new StringBuilder("");
		sbStudentNumbers.setLength(0);
		for (final String studentUsername : (List<String>) studentUsernames) {
			Attends attend = grouping.getStudentAttend(studentUsername);
			if (attend != null) {
				if (sbStudentNumbers.length() != 0) {
					sbStudentNumbers.append(", " + attend.getRegistration().getNumber().toString());
				} else {
					sbStudentNumbers.append(attend.getRegistration().getNumber().toString());
				}
				attend.removeStudentGroups(studentGroup);
			}
		}

		// no students means no log entry -- list may contain invalid values, so
		// its size cannot be used to test
		if (sbStudentNumbers.length() != 0) {
			List<ExecutionCourse> ecs = grouping.getExecutionCourses();
			for (ExecutionCourse ec : ecs) {
				GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
						"log.executionCourse.groupAndShifts.grouping.group.element.removed", Integer.toString(studentUsernames
								.size()), sbStudentNumbers.toString(), studentGroup.getGroupNumber().toString(), grouping
								.getName(), ec.getNome(), ec.getDegreePresentationString());
			}
		}
		return true;
	}
}