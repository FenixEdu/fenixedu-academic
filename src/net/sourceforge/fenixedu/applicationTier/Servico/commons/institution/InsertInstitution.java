package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertInstitution extends FenixService {

	@Service
	public static Unit run(String institutionName) throws FenixServiceException {
		if (UnitUtils.readExternalInstitutionUnitByName(institutionName) != null) {
			throw new ExistingServiceException("error.exception.commons.institution.institutionAlreadyExists");
		}
		return Unit.createNewNoOfficialExternalInstitution(institutionName);
	}
}