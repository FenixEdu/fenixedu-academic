/*
 * Created on 22/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class SearchPerson implements IService {
    public SearchPerson() {

    }

    /*
     * This service return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List run(HashMap searchParameters) throws FenixServiceException {
        ISuportePersistente sp;
        List result;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            result = doSearch(searchParameters, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems with database!", e);
        }
        if (result == null || result.size() < 2) {
            throw new FenixServiceException();
        }

        List infoList = (List) CollectionUtils.collect((List) result.get(1), new Transformer() {
            public Object transform(Object input) {
                InfoObject infoObject = cloneDomainObject((IDomainObject) input);
                return infoObject;
            }
        });
        result.set(1, infoList);

        return result;
    }

    protected InfoObject cloneDomainObject(IDomainObject object) {
        InfoPerson infoPerson = InfoPerson.newInfoFromDomain((IPerson) object);

        return infoPerson;
    }

    protected List doSearch(HashMap parametersSearch, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

        String name = (String) parametersSearch.get(new String("name"));
        String email = (String) parametersSearch.get(new String("email"));
        String username = (String) parametersSearch.get(new String("username"));
        String documentIdNumber = (String) parametersSearch.get(new String("documentIdNumber"));

        Integer startIndex = (Integer) parametersSearch.get(new String("startIndex"));
        Integer numberOfElementsInSpan = (Integer) parametersSearch.get(new String("numberOfElements"));

        return persistentPerson.findActivePersonByNameAndEmailAndUsernameAndDocumentId(name, email,
                username, documentIdNumber, startIndex, numberOfElementsInSpan);
    }
}