
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPersons;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertInexistentAuthors extends Service {

    public List<Person> run(final List<InfoAuthor> infoAuthorsList) throws ExcepcaoPersistencia, FenixServiceException {
        
        final List<Person> authorsList = new ArrayList<Person>(infoAuthorsList.size());
        
        List<Integer> externalPersonsIndexes = new ArrayList<Integer>();

        List<InfoExternalPerson> infoExternalPersons = new ArrayList<InfoExternalPerson>();
        int index = 0;
        for (InfoAuthor infoAuthor : infoAuthorsList) {
            if (infoAuthor.getIdInternal() == null) {
                InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
                InfoPerson infoPerson = new InfoPerson();
//                infoPerson.setIdInternal(infoAuthor.getIdInternal());
                infoPerson.setNome(infoAuthor.getAuthor());
                InfoInstitution infoInstitution = new InfoInstitution();
                infoInstitution.setName(infoAuthor.getOrganization());
                infoExternalPerson.setInfoPerson(infoPerson);
                infoExternalPerson.setInfoInstitution(infoInstitution);
                infoExternalPersons.add(infoExternalPerson);
                externalPersonsIndexes.add(index);
            }
            else {
                Person person = (Person) rootDomainObject.readPartyByOID(infoAuthor.getIdInternal());
                authorsList.add(person);
            }
            index++;
            
        }
        
        InsertExternalPersons iep = new InsertExternalPersons();
        List<ExternalPerson> externalPersons = iep.run(infoExternalPersons);
        
        Iterator<Integer> indexIterator =  externalPersonsIndexes.iterator();
        for (ExternalPerson externalPerson : externalPersons) {
            
            authorsList.add(indexIterator.next(),externalPerson.getPerson());
            
        }
        
        return authorsList;
    }
    
}