package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonWithPersonAndWLocation;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByIDs extends Service {

    public Collection<InfoExternalPerson> run(Collection<Integer> externalPersonsIDs)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Collection<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(
                externalPersonsIDs.size());
        Collection<ExternalPerson> externalPersons = sp.getIPersistentExternalPerson().readByIDs(
                externalPersonsIDs);

        for (ExternalPerson externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPersonWithPersonAndWLocation
                    .newInfoFromDomain(externalPerson));
        }

        return infoExternalPersons;
    }
}