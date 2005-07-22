package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class SearchPerson implements IService {

	/*
     * This service return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List run(HashMap searchParameters) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

        String name = (String) searchParameters.get(new String("name"));
        String email = (String) searchParameters.get(new String("email"));
        String username = (String) searchParameters.get(new String("username"));
        String documentIdNumber = (String) searchParameters.get(new String("documentIdNumber"));
        Integer startIndex = (Integer) searchParameters.get(new String("startIndex"));
        Integer numberOfElementsInSpan = (Integer) searchParameters.get(new String("numberOfElements"));

        
        List<IPerson> persons = persistentPerson.readActivePersonByNameAndEmailAndUsernameAndDocumentId(name, email,
                username, documentIdNumber, startIndex, numberOfElementsInSpan);
        Integer totalPersons = persistentPerson.countActivePersonByNameAndEmailAndUsernameAndDocumentId(name, email,
                username, documentIdNumber, startIndex);

        List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();
        for (IPerson person : persons) {
        	infoPersons.add(InfoPerson.newInfoFromDomain(person));
        }

        List<Object> result = new ArrayList<Object>(2);
        
        result.add(totalPersons);
        result.add(infoPersons);

        return result;
    }
}