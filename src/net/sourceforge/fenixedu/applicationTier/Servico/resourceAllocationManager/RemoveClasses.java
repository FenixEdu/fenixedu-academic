/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveClasses extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Boolean run(InfoShift infoShift, List classOIDs) {
        final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

        for (int i = 0; i < classOIDs.size(); i++) {
            final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID((Integer) classOIDs.get(i));
            shift.getAssociatedClasses().remove(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }
        return Boolean.TRUE;
    }

}