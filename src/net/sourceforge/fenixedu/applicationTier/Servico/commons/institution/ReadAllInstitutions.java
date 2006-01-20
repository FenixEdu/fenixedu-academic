package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllInstitutions extends Service {

    public Object run() throws FenixServiceException, ExcepcaoPersistencia {
        List infoInstitutions = new ArrayList();

        List<Institution> institutions = (List<Institution>)persistentSupport.getIPersistentInstitution().readAll();

        for (Institution institution : institutions) {
            infoInstitutions.add(InfoInstitution.newInfoFromDomain(institution));
        }

        return infoInstitutions;
    }
}