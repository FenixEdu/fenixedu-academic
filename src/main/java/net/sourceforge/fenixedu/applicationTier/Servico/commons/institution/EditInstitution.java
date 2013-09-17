package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditInstitution {

    @Atomic
    public static void run(String oldInstitutionOID, String newInstitutionName) throws FenixServiceException {

        Unit storedInstitution = UnitUtils.readExternalInstitutionUnitByName(newInstitutionName);

        Unit oldInstitution = (Unit) FenixFramework.getDomainObject(oldInstitutionOID);
        if (oldInstitution == null) {
            throw new NonExistingServiceException("error.exception.commons.institution.institutionNotFound");
        }

        if ((storedInstitution != null) && (!storedInstitution.equals(oldInstitution))) {
            throw new ExistingServiceException("error.exception.commons.institution.institutionAlreadyExists");
        }

        oldInstitution.setName(newInstitutionName);
    }

}