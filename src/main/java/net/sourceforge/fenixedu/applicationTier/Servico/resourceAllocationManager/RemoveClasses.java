/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class RemoveClasses {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Boolean run(InfoShift infoShift, List<String> classOIDs) {
        final Shift shift = AbstractDomainObject.fromExternalId(infoShift.getExternalId());

        for (int i = 0; i < classOIDs.size(); i++) {
            final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(classOIDs.get(i));
            shift.getAssociatedClasses().remove(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }
        return Boolean.TRUE;
    }

}