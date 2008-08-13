package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

public class EditInstitution extends Service {

    public void run(Integer oldInstitutionOID, String newInstitutionName) throws FenixServiceException {

	Unit storedInstitution = UnitUtils.readExternalInstitutionUnitByName(newInstitutionName);

	Unit oldInstitution = (Unit) rootDomainObject.readPartyByOID(oldInstitutionOID);
	if (oldInstitution == null) {
	    throw new NonExistingServiceException("error.exception.commons.institution.institutionNotFound");
	}

	if ((storedInstitution != null) && (!storedInstitution.equals(oldInstitution))) {
	    throw new ExistingServiceException("error.exception.commons.institution.institutionAlreadyExists");
	}

	oldInstitution.setName(newInstitutionName);
    }

}
