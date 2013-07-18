/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteClasses {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Boolean run(final List<Integer> classOIDs) {
        for (final Integer classId : classOIDs) {
            RootDomainObject.getInstance().readSchoolClassByOID(classId).delete();
        }
        return Boolean.TRUE;
    }

}