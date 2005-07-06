package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;

import org.apache.commons.beanutils.BeanComparator;

public class PessoaVO extends PersistentObjectOJB implements IPessoaPersistente {

    public IPerson lerPessoaPorUsername(final String username) throws ExcepcaoPersistencia {
        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (person.getUsername().equalsIgnoreCase(username)){
                return person;
            }
        }
        return null;
    }

    public List findPersonByName(String name) throws ExcepcaoPersistencia {

        final String nameToMatch = name.replaceAll("%",".*");
        final List persons = new ArrayList();
        
        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (person.getNome().toLowerCase().matches(nameToMatch.toLowerCase())){
                persons.add(person);
            }
        }
        return persons;

    }

    public List findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        final ArrayList persons = new ArrayList(findPersonByName(name));
        
        return persons.subList(startIndex,startIndex+numberOfElementsInSpan);

    }

    public Integer countAllPersonByName(String name) throws ExcepcaoPersistencia {

        return new Integer(findPersonByName(name).size());

    }

    public IPerson lerPessoaPorNumDocIdETipoDocId(final String numeroDocumentoIdentificacao,
            final IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {

        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (person.getNumeroDocumentoIdentificacao().equals(numeroDocumentoIdentificacao) &&
                person.getIdDocumentType().equals(tipoDocumentoIdentificacao)) {
                return person;
            }
        }
        return null;
        
    }

    /*
     * This method return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List findActivePersonByNameAndEmailAndUsernameAndDocumentId(final String name, final String email,
            final String username, final String documentIdNumber, final Integer startIndex, final Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        
        final List result = new ArrayList(2);
        
        final String nameToMatch = name.replaceAll("%",".*");
        final String emailToMatch = email.replaceAll("%",".*");
        final String usernameToMatch = email.replaceAll("%",".*");
        final String documentIdNumberToMatch = documentIdNumber.replaceAll("%",".*");
        
        final List allPersons = (List) readAll(Person.class);
        
        List filteredPersons = new ArrayList();
        
        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (name != null && name.length() > 0 && !person.getNome().matches(nameToMatch)) {
                continue;
            }
            
            if (email != null && email.length() > 0 && !person.getEmail().matches(emailToMatch)) {
                continue;
            }
            
            if (username != null && username.length() > 0) {
                
                if (!person.getUsername().matches(usernameToMatch)) {
                    continue;
                }
                
                if (person.getUsername().matches("INA.*")) {
                    continue;
                }
                
            }

            if (documentIdNumber != null && documentIdNumber.length() > 0 && 
                    !person.getNumeroDocumentoIdentificacao().matches(documentIdNumberToMatch)) {
                continue;
            }
            
            filteredPersons.add(person);
        }

        result.add(0, new Integer(allPersons.size()));

        if (startIndex == null && numberOfElementsInSpan == null) {
            
            Collections.sort(filteredPersons,new BeanComparator("nome"));
            result.add(1,filteredPersons);
            
        } else if (startIndex != null && numberOfElementsInSpan != null) {
            
            result.add(1,filteredPersons.subList(startIndex,startIndex+numberOfElementsInSpan));
            
        }

        return result;

    }

}