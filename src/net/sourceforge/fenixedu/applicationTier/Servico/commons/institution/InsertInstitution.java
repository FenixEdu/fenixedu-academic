package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InsertInstitution implements IService {

    public IInstitution run(String institutionName) throws ExcepcaoPersistencia,
            ExistingServiceException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IInstitution storedInstitution = sp.getIPersistentInstitution().readByName(
                institutionName);

        if (storedInstitution != null) {
            throw new ExistingServiceException(
                    "error.exception.commons.institution.institutionAlreadyExists");
        }

        IInstitution institution = DomainFactory.makeInstitution();
        institution.setName(institutionName);

        return institution;
    }

}
