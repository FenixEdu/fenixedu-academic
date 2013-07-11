package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoverTurno {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static Object run(final InfoShift infoShift, final InfoClass infoClass) {
        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());
        if (shift == null) {
            return Boolean.FALSE;
        }
        final SchoolClass schoolClass = (SchoolClass) CollectionUtils.find(shift.getAssociatedClasses(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final SchoolClass schoolClass = (SchoolClass) arg0;
                return schoolClass.getExternalId().equals(infoClass.getExternalId());
            }
        });
        if (schoolClass == null) {
            return Boolean.FALSE;
        }
        shift.getAssociatedClasses().remove(schoolClass);
        return Boolean.TRUE;
    }

}