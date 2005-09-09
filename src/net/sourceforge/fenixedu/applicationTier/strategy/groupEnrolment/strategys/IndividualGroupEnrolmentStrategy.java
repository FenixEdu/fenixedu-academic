/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements
        IGroupEnrolmentStrategy {

    public IndividualGroupEnrolmentStrategy() {

    }

    public Integer enrolmentPolicyNewGroup(IGrouping groupProperties, int numberOfStudentsToEnrole,
            IShift shift) throws ExcepcaoPersistencia {

        if (checkNumberOfGroups(groupProperties, shift)) {
            return new Integer(1);
        }

        return new Integer(-1);

    }

    public boolean checkNumberOfGroupElements(IGrouping groupProperties, IStudentGroup studentGroup) {
        return true;
    }
}
