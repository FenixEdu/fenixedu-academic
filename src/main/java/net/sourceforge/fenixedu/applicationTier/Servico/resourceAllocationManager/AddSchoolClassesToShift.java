package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AddSchoolClassesToShift {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(InfoShift infoShift, List<Integer> schoolClassOIDs) throws FenixServiceException {

        final Shift shift = AbstractDomainObject.fromExternalId(infoShift.getExternalId());
        if (shift == null) {
            throw new InvalidArgumentsServiceException();
        }

        for (final Integer schoolClassOID : schoolClassOIDs) {
            final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(schoolClassOID);
            if (schoolClass == null) {
                throw new InvalidArgumentsServiceException();
            }

            shift.associateSchoolClass(schoolClass);
        }
    }

}