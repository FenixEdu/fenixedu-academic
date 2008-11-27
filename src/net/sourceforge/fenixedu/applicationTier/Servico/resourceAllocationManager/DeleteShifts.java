/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DeleteShifts extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(final List<Integer> shiftOIDs) throws FenixServiceException {
	final List<DomainException> exceptionList = new ArrayList<DomainException>();

	for (final Integer shiftID : shiftOIDs) {
	    try {
		rootDomainObject.readShiftByOID(shiftID).delete();
	    } catch (DomainException e) {
		exceptionList.add(e);
	    }
	}

	if (!exceptionList.isEmpty()) {
	    throw new FenixServiceMultipleException(exceptionList);
	}
    }

}