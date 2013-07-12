package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveShifts {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static Boolean run(final InfoClass infoClass, final List<String> shiftOIDs) {
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());
        final Collection<Shift> shifts = schoolClass.getAssociatedShiftsSet();

        for (String externalId : shiftOIDs) {
            Shift shift = FenixFramework.getDomainObject(externalId);
            shifts.remove(shift);
        }

        return Boolean.TRUE;
    }

}