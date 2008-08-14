/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public boolean checkNumberOfGroups(Grouping grouping, Shift shift) {

	if (grouping.getGroupMaximumNumber() == null) {
	    return true;
	}
	int numberOfGroups = 0;
	if (shift != null) {
	    numberOfGroups = grouping.readAllStudentGroupsBy(shift).size();
	} else {
	    numberOfGroups = grouping.getStudentGroupsWithoutShift().size();
	}
	if (numberOfGroups < grouping.getGroupMaximumNumber()) {
	    return true;
	}
	return false;
    }

    public boolean checkEnrolmentDate(Grouping grouping, Calendar actualDate) {
	Long actualDateInMills = new Long(actualDate.getTimeInMillis());
	Long enrolmentBeginDayInMills = null;
	Long enrolmentEndDayInMills = null;

	if (grouping.getEnrolmentBeginDay() != null)
	    enrolmentBeginDayInMills = new Long(grouping.getEnrolmentBeginDay().getTimeInMillis());

	if (grouping.getEnrolmentEndDay() != null)
	    enrolmentEndDayInMills = new Long(grouping.getEnrolmentEndDay().getTimeInMillis());

	if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills == null)
	    return true;

	if (enrolmentBeginDayInMills != null && enrolmentEndDayInMills == null) {
	    if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0)
		return true;
	}

	if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills != null) {
	    if (actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
		return true;
	}

	if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0 && actualDateInMills.compareTo(enrolmentEndDayInMills) < 0)
	    return true;

	return false;
    }

    public boolean checkShiftType(Grouping grouping, Shift shift) {
	if (shift != null) {
	    return shift.containsType(grouping.getShiftType());
	} else {
	    return grouping.getShiftType() == null;
	}
    }

    public List checkShiftsType(Grouping grouping, List shifts) {
	List result = new ArrayList();
	if (grouping.getShiftType() != null) {
	    for (final Shift shift : (List<Shift>) shifts) {
		if (shift.containsType(grouping.getShiftType())) {
		    result.add(shift);
		}
	    }
	}
	return result;
    }

    public boolean checkAlreadyEnroled(Grouping grouping, String studentUsername) {

	final Attends studentAttend = grouping.getStudentAttend(studentUsername);

	if (studentAttend != null) {
	    List<StudentGroup> groupingStudentGroups = grouping.getStudentGroups();
	    for (final StudentGroup studentGroup : groupingStudentGroups) {
		List<Attends> studentGroupAttends = studentGroup.getAttends();
		for (final Attends attend : studentGroupAttends) {
		    if (attend == studentAttend) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public boolean checkNotEnroledInGroup(Grouping grouping, StudentGroup studentGroup, String studentUsername) {

	final Attends studentAttend = grouping.getStudentAttend(studentUsername);

	if (studentAttend != null) {
	    List<Attends> studentGroupAttends = studentGroup.getAttends();
	    for (final Attends attend : studentGroupAttends) {
		if (attend == studentAttend) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean checkPossibleToEnrolInExistingGroup(Grouping grouping, StudentGroup studentGroup) {

	final int numberOfElements = studentGroup.getAttendsCount();
	final Integer maximumCapacity = grouping.getMaximumCapacity();
	if (maximumCapacity == null)
	    return true;
	if (numberOfElements < maximumCapacity)
	    return true;

	return false;
    }

    public boolean checkIfStudentGroupIsEmpty(Attends attend, StudentGroup studentGroup) {

	final List allStudentGroupAttends = studentGroup.getAttends();
	if (allStudentGroupAttends.size() == 1 && allStudentGroupAttends.contains(attend)) {
	    return true;
	}
	return false;
    }

    public boolean checkStudentInGrouping(Grouping grouping, String username) {

	final Attends attend = grouping.getStudentAttend(username);
	return attend != null;
    }

    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, Grouping grouping) {
	for (final String studentUsername : studentUsernames) {
	    if (grouping.getStudentAttend(studentUsername) == null) {
		return false;
	    }
	}
	return true;
    }

    public boolean checkHasShift(Grouping grouping) {
	return grouping.getShiftType() != null;
    }

    public abstract Integer enrolmentPolicyNewGroup(Grouping grouping, int numberOfStudentsToEnrole, Shift shift);

    public abstract boolean checkNumberOfGroupElements(Grouping grouping, StudentGroup studentGroup);

}
