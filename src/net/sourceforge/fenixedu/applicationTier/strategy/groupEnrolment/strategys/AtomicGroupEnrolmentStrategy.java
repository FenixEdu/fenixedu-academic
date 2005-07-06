/*
 * Created on 24/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 *  
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements
        IGroupEnrolmentStrategy {

    public AtomicGroupEnrolmentStrategy() {
    }

    public Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,
            int numberOfStudentsToEnrole, IShift shift) {

        if (checkNumberOfGroups(groupProperties, shift)) {
            Integer maximumCapacity = groupProperties.getMaximumCapacity();
            Integer minimumCapacity = groupProperties.getMinimumCapacity();
            Integer nrStudents = new Integer(numberOfStudentsToEnrole);

            if (maximumCapacity == null && minimumCapacity == null)
                return new Integer(1);
            if (minimumCapacity != null) {
                if (nrStudents.compareTo(minimumCapacity) < 0)
                    return new Integer(-2);
            }
            if (maximumCapacity != null) {
                if (nrStudents.compareTo(maximumCapacity) > 0)
                    return new Integer(-3);
            }
        } else
            return new Integer(-1);

        return new Integer(1);

    }

    public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,
            IStudentGroup studentGroup) throws ExcepcaoPersistencia {

        boolean result = false;
        Integer minimumCapacity = groupProperties.getMinimumCapacity();

        if (minimumCapacity == null)
            result = true;
        else {

            List allStudentGroupAttend = studentGroup.getStudentGroupAttends();

            int numberOfGroupElements = allStudentGroupAttend.size();

            if (numberOfGroupElements > minimumCapacity.intValue())
                result = true;

        }
        return result;
    }
}