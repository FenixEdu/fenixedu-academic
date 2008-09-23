package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class DeleteExternalUnit extends FenixService {

    public void run(final Unit externalUnit) throws FenixServiceException {
	if (externalUnit.isOfficialExternal()) {
	    externalUnit.delete();
	} else {
	    throw new FenixServiceException("error.DeleteExternalUnit.invalid.unit");
	}
    }
}
