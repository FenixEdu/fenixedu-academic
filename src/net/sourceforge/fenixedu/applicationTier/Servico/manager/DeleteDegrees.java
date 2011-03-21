package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class DeleteDegrees extends FenixService {

    // delete a set of degrees
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List degreesInternalIds) throws FenixServiceException {
	Iterator iter = degreesInternalIds.iterator();

	List<String> undeletedDegreesNames = new ArrayList<String>();
	while (iter.hasNext()) {
	    Integer internalId = (Integer) iter.next();
	    Degree degree = rootDomainObject.readDegreeByOID(internalId);

	    if (degree != null) {

		try {
		    degree.delete();
		} catch (DomainException e) {
		    undeletedDegreesNames.add(degree.getNome());
		}
	    }
	}

	return undeletedDegreesNames;
    }

}