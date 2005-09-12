/*
 * Created on 24/Jul/2003
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

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements
        IGroupEnrolmentStrategy {

    public AtomicGroupEnrolmentStrategy() {
    }

    public Integer enrolmentPolicyNewGroup(IGrouping grouping, int numberOfStudentsToEnrole, IShift shift) {

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

    public boolean checkNumberOfGroupElements(IGrouping grouping, IStudentGroup studentGroup) throws ExcepcaoPersistencia {

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