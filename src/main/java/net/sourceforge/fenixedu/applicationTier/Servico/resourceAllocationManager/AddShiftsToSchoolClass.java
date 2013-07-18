package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddShiftsToSchoolClass {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(InfoClass infoClass, List<String> shiftOIDs) throws FenixServiceException {
        final SchoolClass schoolClass = RootDomainObject.getInstance().readSchoolClassByOID(infoClass.getIdInternal());
        if (schoolClass == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final String shiftOID : shiftOIDs) {
            final Shift shift = RootDomainObject.getInstance().readShiftByOID(new Integer(shiftOID));
            if (shift == null) {
                throw new InvalidArgumentsServiceException();
            }
            schoolClass.associateShift(shift);
        }
    }

}