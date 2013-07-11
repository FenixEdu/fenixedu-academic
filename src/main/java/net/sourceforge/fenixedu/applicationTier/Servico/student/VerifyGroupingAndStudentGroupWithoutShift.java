/*
 * Created on 20/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyGroupingAndStudentGroupWithoutShift {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Atomic
    public static Integer run(String studentGroupCode, String groupPropertiesCode, String shiftCodeString, String username)
            throws FenixServiceException {
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidSituationServiceException();
        }

        if (studentGroup.getShift() != null && shiftCodeString == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (studentGroup.getShift() == null) {
            if (shiftCodeString != null) {
                throw new InvalidArgumentsServiceException();
            }
        } else {
            if (!studentGroup.getShift().getExternalId().equals(shiftCodeString)) {
                throw new InvalidArgumentsServiceException();
            }
        }

        if (studentGroup.getShift() != null && groupProperties.getShiftType() != null) {
            return Integer.valueOf(1);
        }

        if (studentGroup.getShift() != null && groupProperties.getShiftType() == null) {
            return Integer.valueOf(2);
        }

        if (studentGroup.getShift() == null && groupProperties.getShiftType() != null) {
            return Integer.valueOf(3);
        }

        if (studentGroup.getShift() == null && groupProperties.getShiftType() == null) {
            return Integer.valueOf(4);
        }

        return Integer.valueOf(5);

    }
}