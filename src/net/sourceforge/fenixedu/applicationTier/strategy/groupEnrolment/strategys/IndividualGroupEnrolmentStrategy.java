/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements
        IGroupEnrolmentStrategy {

    public IndividualGroupEnrolmentStrategy() {

    }

    public Integer enrolmentPolicyNewGroup(IGrouping groupProperties, int numberOfStudentsToEnrole,
            IShift shift) {

        if (checkNumberOfGroups(groupProperties, shift)) {
            return Integer.valueOf(1);
        }

        return Integer.valueOf(-1);
    }

    public boolean checkNumberOfGroupElements(IGrouping groupProperties, IStudentGroup studentGroup) {
        return true;
    }
}
