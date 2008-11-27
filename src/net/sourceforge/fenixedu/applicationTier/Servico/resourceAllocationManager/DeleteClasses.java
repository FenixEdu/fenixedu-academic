/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;

public class DeleteClasses extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static Boolean run(final List<Integer> classOIDs) {
	for (final Integer classId : classOIDs) {
	    rootDomainObject.readSchoolClassByOID(classId).delete();
	}
	return Boolean.TRUE;
    }

}