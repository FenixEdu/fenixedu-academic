package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddShiftsToSchoolClass {

    @Atomic
    public static void run(InfoClass infoClass, List<String> shiftOIDs) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());
        if (schoolClass == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final String shiftOID : shiftOIDs) {
            final Shift shift = FenixFramework.getDomainObject(shiftOID);
            if (shift == null) {
                throw new InvalidArgumentsServiceException();
            }
            schoolClass.associateShift(shift);
        }
    }

}