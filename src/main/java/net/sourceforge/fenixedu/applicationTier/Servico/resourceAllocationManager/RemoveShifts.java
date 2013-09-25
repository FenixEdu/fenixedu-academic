package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveShifts {

    @Atomic
    public static Boolean run(final InfoClass infoClass, final List<String> shiftOIDs) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());
        final Collection<Shift> shifts = schoolClass.getAssociatedShiftsSet();

        for (String externalId : shiftOIDs) {
            Shift shift = FenixFramework.getDomainObject(externalId);
            shifts.remove(shift);
        }

        return Boolean.TRUE;
    }

}