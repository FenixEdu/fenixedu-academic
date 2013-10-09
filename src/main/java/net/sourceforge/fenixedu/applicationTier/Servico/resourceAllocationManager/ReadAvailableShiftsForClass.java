/*
 * 
 * Created on 2003/08/13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadAvailableShiftsForClass {

    @Atomic
    public static Object run(InfoClass infoClass) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List infoShifts = null;

        SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());
        Set<Shift> shifts = schoolClass.findAvailableShifts();

        infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                return InfoShift.newInfoFromDomain(shift);
            }
        });

        return infoShifts;
    }

}