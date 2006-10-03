
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPersons;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;

public class InsertInexistentAuthors extends Service {

    public List<Person> run(final List<InfoAuthor> infoAuthorsList) throws FenixServiceException {
        
        final List<Person> authorsList = new ArrayList<Person>(infoAuthorsList.size());
        
        List<Integer> externalPersonsIndexes = new ArrayList<Integer>();

        int index = 0;
        final List<InfoExternalPersonEditor> infoExternalPersons = new ArrayList<InfoExternalPersonEditor>();
        for (final InfoAuthor infoAuthor : infoAuthorsList) {
            if (infoAuthor.getIdInternal() == null) {
                InfoExternalPersonEditor infoExternalPerson = new InfoExternalPersonEditor();
                
                infoExternalPerson.setName(infoAuthor.getAuthor());
                
                InfoInstitution infoInstitution = new InfoInstitution();
                infoInstitution.setName(infoAuthor.getOrganization());
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
        List<ExternalContract> externalPersons = iep.run(infoExternalPersons);
        
        Iterator<Integer> indexIterator =  externalPersonsIndexes.iterator();
        for (ExternalContract externalPerson : externalPersons) {
            
            authorsList.add(indexIterator.next(),externalPerson.getPerson());
            
        }
        
        return authorsList;
    }
    
}