/*
 * Created on 24/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public AtomicGroupEnrolmentStrategy() {
    }

    public Integer enrolmentPolicyNewGroup(Grouping grouping, int numberOfStudentsToEnrole, Shift shift) {

	if (checkNumberOfGroups(grouping, shift)) {
	    Integer maximumCapacity = grouping.getMaximumCapacity();
	    Integer minimumCapacity = grouping.getMinimumCapacity();
	    Integer nrStudents = Integer.valueOf(numberOfStudentsToEnrole);

	    if (maximumCapacity == null && minimumCapacity == null)
		return Integer.valueOf(1);
	    if (minimumCapacity != null) {
		if (nrStudents.compareTo(minimumCapacity) < 0)
		    return Integer.valueOf(-2);
	    }
	    if (maximumCapacity != null) {
		if (nrStudents.compareTo(maximumCapacity) > 0)
		    return Integer.valueOf(-3);
	    }
	} else
	    return Integer.valueOf(-1);

	return Integer.valueOf(1);
    }

    public boolean checkNumberOfGroupElements(Grouping grouping, StudentGroup studentGroup) {

	boolean result = false;
	final Integer minimumCapacity = grouping.getMinimumCapacity();

	if (minimumCapacity == null)
	    result = true;
	else {
	    final int numberOfGroupElements = studentGroup.getAttendsCount();
	    if (numberOfGroupElements > minimumCapacity.intValue())
		result = true;
	}
	return result;
    }
}