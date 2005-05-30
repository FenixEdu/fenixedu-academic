package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonWithPersonAndWLocation;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByIDs implements IService {

    public Collection<InfoExternalPerson> run(Collection<Integer> externalPersonsIDs)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Collection<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(
                externalPersonsIDs.size());
        Collection<IExternalPerson> externalPersons = sp.getIPersistentExternalPerson().readByIDs(
                externalPersonsIDs);

        for (IExternalPerson externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPersonWithPersonAndWLocation
                    .newInfoFromDomain(externalPerson));
        }

        return infoExternalPersons;
    }
}