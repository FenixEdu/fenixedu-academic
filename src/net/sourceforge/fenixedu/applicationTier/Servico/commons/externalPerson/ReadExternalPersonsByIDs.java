package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByIDs extends FenixService {

    @Service
    public static Collection<InfoExternalPerson> run(Collection<Integer> externalPersonsIDs) {

        Collection<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(externalPersonsIDs.size());

        Collection<ExternalContract> externalPersons = ExternalContract.readByIDs(externalPersonsIDs);

        for (ExternalContract externalPerson : externalPersons) {
            infoExternalPersons.add(InfoExternalPerson.newInfoFromDomain(externalPerson));
        }

        return infoExternalPersons;
    }
}