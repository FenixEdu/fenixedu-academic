/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.IGroupProperties;
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

    public Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,
            int numberOfStudentsToEnrole, IShift shift) {

        if (checkNumberOfGroups(groupProperties, shift)) {
            return new Integer(1);
        }

        return new Integer(-1);

    }

    public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,
            IStudentGroup studentGroup) throws ExcepcaoPersistencia {
        return true;
    }
}

