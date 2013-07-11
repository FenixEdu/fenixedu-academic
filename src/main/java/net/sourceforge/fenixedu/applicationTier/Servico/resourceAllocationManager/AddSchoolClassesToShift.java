package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddSchoolClassesToShift {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(InfoShift infoShift, List<String> schoolClassOIDs) throws FenixServiceException {

        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());
        if (shift == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final String schoolClassOID : schoolClassOIDs) {
            final SchoolClass schoolClass = FenixFramework.getDomainObject(schoolClassOID);
            if (schoolClass == null) {
                throw new InvalidArgumentsServiceException();
            }

            shift.associateSchoolClass(schoolClass);
        }
    }

}