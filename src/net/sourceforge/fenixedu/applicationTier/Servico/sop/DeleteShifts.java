/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteShifts extends Service {

    public void run(final List<Integer> shiftOIDs) throws FenixServiceException, ExcepcaoPersistencia {
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
