/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsByClass {

    @Atomic
    public static Object run(InfoClass infoClass) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());

        Collection<Shift> shifts = schoolClass.getAssociatedShifts();

        return CollectionUtils.collect(shifts, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                return infoShift;
            }
        });
    }

}