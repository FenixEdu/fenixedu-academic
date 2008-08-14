/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    public IndividualGroupEnrolmentStrategy() {

    }

    public Integer enrolmentPolicyNewGroup(Grouping groupProperties, int numberOfStudentsToEnrole, Shift shift) {

	if (checkNumberOfGroups(groupProperties, shift)) {
	    return Integer.valueOf(1);
	}

	return Integer.valueOf(-1);
    }

    public boolean checkNumberOfGroupElements(Grouping groupProperties, StudentGroup studentGroup) {
	return true;
    }
}
