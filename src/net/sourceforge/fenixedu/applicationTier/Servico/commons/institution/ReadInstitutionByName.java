package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadInstitutionByName implements IService {

    public InfoInstitution run(String institutionName) throws FenixServiceException, ExcepcaoPersistencia {
        InfoInstitution infoInstitution = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IInstitution institution = sp.getIPersistentInstitution().readByName(institutionName);

        if (institution != null) {
            infoInstitution = new InfoInstitution();
            infoInstitution.copyFromDomain(institution);
        }

        return infoInstitution;

    }
}