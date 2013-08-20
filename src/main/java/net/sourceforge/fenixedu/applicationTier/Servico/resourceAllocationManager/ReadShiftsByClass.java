/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadShiftsByClass {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Object run(InfoClass infoClass) {
        SchoolClass schoolClass = AbstractDomainObject.fromExternalId(infoClass.getExternalId());

        List<Shift> shifts = schoolClass.getAssociatedShifts();

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