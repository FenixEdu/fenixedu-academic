package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoveShifts {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Boolean run(final InfoClass infoClass, final List shiftOIDs) {
        final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(infoClass.getExternalId());
        final List<Shift> shifts = schoolClass.getAssociatedShifts();

        for (int i = 0; i < shifts.size(); i++) {
            final Shift shift = shifts.get(i);
            if (shiftOIDs.contains(shift.getExternalId())) {
                shifts.remove(shift);
                i--;
            }
        }

        return Boolean.TRUE;
    }

}