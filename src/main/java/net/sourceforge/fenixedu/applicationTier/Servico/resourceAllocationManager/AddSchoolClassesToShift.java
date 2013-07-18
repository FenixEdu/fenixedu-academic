package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddSchoolClassesToShift {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(InfoShift infoShift, List<Integer> schoolClassOIDs) throws FenixServiceException {

        final Shift shift = RootDomainObject.getInstance().readShiftByOID(infoShift.getIdInternal());
        if (shift == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final Integer schoolClassOID : schoolClassOIDs) {
            final SchoolClass schoolClass = RootDomainObject.getInstance().readSchoolClassByOID(schoolClassOID);
            if (schoolClass == null) {
                throw new InvalidArgumentsServiceException();
            }

            shift.associateSchoolClass(schoolClass);
        }
    }

}