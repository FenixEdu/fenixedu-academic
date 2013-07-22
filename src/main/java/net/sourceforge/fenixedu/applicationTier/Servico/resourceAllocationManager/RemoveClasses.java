/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoveClasses {

    @Atomic
    public static Boolean run(InfoShift infoShift, List<String> classOIDs) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());

        for (int i = 0; i < classOIDs.size(); i++) {
            final SchoolClass schoolClass = FenixFramework.getDomainObject(classOIDs.get(i));
            shift.getAssociatedClasses().remove(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }
        return Boolean.TRUE;
    }

}