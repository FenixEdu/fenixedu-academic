/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteAllGroupingMembers extends FenixService {

    public Boolean run(Integer objectCode, Integer groupingCode) throws FenixServiceException {
	Grouping grouping = rootDomainObject.readGroupingByOID(groupingCode);

	if (grouping == null) {
	    throw new ExistingServiceException();
	}

	List attendsElements = new ArrayList();
	attendsElements.addAll(grouping.getAttends());
	Iterator iterator = attendsElements.iterator();
	StringBuilder sbStudentNumbers = new StringBuilder("");
	sbStudentNumbers.setLength(0);

	while (iterator.hasNext()) {
	    Attends attend = (Attends) iterator.next();
	    if (sbStudentNumbers.length() != 0) {
		sbStudentNumbers.append(", " + attend.getRegistration().getNumber().toString());
	    } else {
		sbStudentNumbers.append(attend.getRegistration().getNumber().toString());
	    }

	    boolean found = false;
	    Iterator iterStudentsGroups = grouping.getStudentGroups().iterator();
	    while (iterStudentsGroups.hasNext() && !found) {

		StudentGroup studentGroup = (StudentGroup) iterStudentsGroups.next();

		if (studentGroup != null) {
		    studentGroup.removeAttends(attend);
		    found = true;
		}
	    }
	    grouping.removeAttends(attend);
	}

	// no students means no log entry -- list may contain invalid values, so
	// its size cannot be used to test
	if (sbStudentNumbers.length() != 0) {
	    List<ExecutionCourse> ecs = grouping.getExecutionCourses();
	    for (ExecutionCourse ec : ecs) {
		GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
			"log.executionCourse.groupAndShifts.grouping.memberSet.removed",
			Integer.toString(attendsElements.size()), sbStudentNumbers.toString(), grouping.getName(), ec.getNome(),
			ec.getDegreePresentationString());
	    }
	}

	return true;
    }
}