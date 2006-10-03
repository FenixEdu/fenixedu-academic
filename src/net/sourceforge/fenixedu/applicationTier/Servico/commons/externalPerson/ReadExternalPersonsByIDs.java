package net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadExternalPersonsByIDs extends Service {

    public Collection<InfoExternalPerson> run(Collection<Integer> externalPersonsIDs)
	    throws ExcepcaoPersistencia {

	Collection<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>(
		externalPersonsIDs.size());

	Collection<ExternalContract> externalPersons = ExternalContract.readByIDs(externalPersonsIDs);

	for (ExternalContract externalPerson : externalPersons) {
	    infoExternalPersons.add(InfoExternalPerson.newInfoFromDomain(externalPerson));
	}

	return infoExternalPersons;
    }
}