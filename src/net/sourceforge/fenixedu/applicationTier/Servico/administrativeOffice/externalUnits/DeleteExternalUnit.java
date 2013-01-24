package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteExternalUnit extends FenixService {

    @Service
    public static void run(final Unit externalUnit) throws FenixServiceException {
	if (externalUnit.isOfficialExternal()) {
	    externalUnit.delete();
	} else {
	    throw new FenixServiceException("error.DeleteExternalUnit.invalid.unit");
	}
    }
}