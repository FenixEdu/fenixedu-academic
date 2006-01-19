package net.sourceforge.fenixedu.applicationTier.Servico.commons.institution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllInstitutions implements IService {

    public Object run() throws FenixServiceException, ExcepcaoPersistencia {
        List infoInstitutions = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<Institution> institutions = (List<Institution>)sp.getIPersistentInstitution().readAll();

        for (Institution institution : institutions) {
            infoInstitutions.add(InfoInstitution.newInfoFromDomain(institution));
        }

        return infoInstitutions;
    }
}