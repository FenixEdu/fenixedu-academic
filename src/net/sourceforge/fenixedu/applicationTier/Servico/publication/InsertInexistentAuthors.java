
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPersons;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertInexistentAuthors implements IService {

    public List<IPerson> run(final List<InfoAuthor> infoAuthorsList) throws ExcepcaoPersistencia, ExistingServiceException {
        
        final List<IPerson> authorsList = new ArrayList<IPerson>(infoAuthorsList.size());
        
        List<Integer> externalPersonsIndexes = new ArrayList<Integer>();
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente personDAO = sp.getIPessoaPersistente();
        
        List<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>();
        int index = 0;
        for (InfoAuthor infoAuthor : infoAuthorsList) {
            if (infoAuthor.getIdInternal() == null) {
                InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
                InfoPerson infoPerson = new InfoPerson();
                infoPerson.setIdInternal(infoAuthor.getIdInternal());
                infoPerson.setNome(infoAuthor.getAuthor());
                InfoWorkLocation infoWorkLocation = new InfoWorkLocation();
                infoWorkLocation.setName(infoAuthor.getOrganization());
                infoExternalPerson.setInfoPerson(infoPerson);
                infoExternalPerson.setInfoWorkLocation(infoWorkLocation);
                infoExternalPersons.add(infoExternalPerson);
                externalPersonsIndexes.add(index);
            }
            else {
                IPerson person = (IPerson) personDAO.readByOID(Person.class, infoAuthor.getIdInternal());
                authorsList.add(person);
            }
            index++;
            
        }
        
        InsertExternalPersons iep = new InsertExternalPersons();
        List<IExternalPerson> externalPersons = iep.run(infoExternalPersons);
        
        Iterator<Integer> indexIterator =  externalPersonsIndexes.iterator();
        for (IExternalPerson externalPerson : externalPersons) {
            
            authorsList.add(indexIterator.next(),externalPerson.getPerson());
            
        }
        
        return authorsList;
    }
    
}