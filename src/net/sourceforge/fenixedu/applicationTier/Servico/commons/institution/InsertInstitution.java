package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertInstitution extends Service {

    public Unit run(String institutionName) throws FenixServiceException {
        if (UnitUtils.readExternalInstitutionUnitByName(institutionName) != null) {
            throw new ExistingServiceException("error.exception.commons.institution.institutionAlreadyExists");
        }      
        return Unit.createNewNoOfficialExternalInstitution(institutionName);
    }
}
