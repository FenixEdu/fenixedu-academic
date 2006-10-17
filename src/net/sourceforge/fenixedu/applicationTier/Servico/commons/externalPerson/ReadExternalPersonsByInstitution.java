package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByInstitution extends Service {

    public List run(Integer institutionID) throws FenixServiceException, ExcepcaoPersistencia {
        List infoExternalPersons = new ArrayList();
        
        Unit institution = (Unit) rootDomainObject.readPartyByOID(institutionID);
        Collection<ExternalContract> externalPersons = institution.getExternalPersons();               

        for (ExternalContract externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPerson.newInfoFromDomain(externalPerson));
        }

        return infoExternalPersons;
    }
}