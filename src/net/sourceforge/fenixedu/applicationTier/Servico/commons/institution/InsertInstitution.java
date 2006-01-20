package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertInstitution extends Service {

    public Institution run(String institutionName) throws ExcepcaoPersistencia,
            ExistingServiceException {
        final Institution storedInstitution = persistentSupport.getIPersistentInstitution().readByName(
                institutionName);

        if (storedInstitution != null) {
            throw new ExistingServiceException(
                    "error.exception.commons.institution.institutionAlreadyExists");
        }

        Institution institution = DomainFactory.makeInstitution();
        institution.setName(institutionName);

        return institution;
    }

}
