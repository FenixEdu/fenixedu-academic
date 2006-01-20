package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadInstitutionByName extends Service {

    public InfoInstitution run(String institutionName) throws FenixServiceException, ExcepcaoPersistencia {
        InfoInstitution infoInstitution = null;

        Institution institution = persistentSupport.getIPersistentInstitution().readByName(institutionName);

        if (institution != null) {
            infoInstitution = new InfoInstitution();
            infoInstitution.copyFromDomain(institution);
        }

        return infoInstitution;

    }
}