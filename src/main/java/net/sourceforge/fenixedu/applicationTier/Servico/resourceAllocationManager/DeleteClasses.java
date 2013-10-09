/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteClasses {

    @Atomic
    public static Boolean run(final List<String> classOIDs) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        for (final String classId : classOIDs) {
            FenixFramework.<SchoolClass> getDomainObject(classId).delete();
        }
        return Boolean.TRUE;
    }

}