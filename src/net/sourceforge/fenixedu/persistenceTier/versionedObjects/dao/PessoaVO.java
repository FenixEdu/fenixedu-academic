package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;

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
     * This method return a list with elements returned by the limited search.
     */
    public List<IPerson> readActivePersonByNameAndEmailAndUsernameAndDocumentId(final String name, final String email,
            final String username, final String documentIdNumber, final Integer startIndex, final Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        
        final List<IPerson> persons = new ArrayList<IPerson>();
        
        final String nameToMatch = name.replaceAll("%",".*");
        final String emailToMatch = email.replaceAll("%",".*");
        final String usernameToMatch = email.replaceAll("%",".*");
        final String documentIdNumberToMatch = documentIdNumber.replaceAll("%",".*");
        
        final List<IPerson> allPersons = (List<IPerson>) readAll(Person.class);
        
        List<IPerson> filteredPersons = new ArrayList<IPerson>();
        
        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (name != null && name.length() > 0 && !person.getNome().matches(nameToMatch)) { continue; }
            if (email != null && email.length() > 0 && !person.getEmail().matches(emailToMatch)) { continue; }
            if (username != null && username.length() > 0) {
                if (!person.getUsername().matches(usernameToMatch)) { continue; }
                if (person.getUsername().matches("INA.*")) { continue; }
            }

            if (documentIdNumber != null && documentIdNumber.length() > 0 && 
                    !person.getNumeroDocumentoIdentificacao().matches(documentIdNumberToMatch)) {
                continue;
            }
            filteredPersons.add(person);
        }

        List<IPerson> result = new ArrayList<IPerson>();
        if (startIndex == null && numberOfElementsInSpan == null) {
            Collections.sort(filteredPersons,new BeanComparator("nome"));
            result = filteredPersons;
        } else if (startIndex != null && numberOfElementsInSpan != null) {
            result = (filteredPersons.subList(startIndex,startIndex+numberOfElementsInSpan));
        }

        return result;
    }
    
    public Integer countActivePersonByNameAndEmailAndUsernameAndDocumentId(final String name, final String email,
            final String username, final String documentIdNumber, final Integer startIndex)
            throws ExcepcaoPersistencia {
        
        final String nameToMatch = name.replaceAll("%",".*");
        final String emailToMatch = email.replaceAll("%",".*");
        final String usernameToMatch = email.replaceAll("%",".*");
        final String documentIdNumberToMatch = documentIdNumber.replaceAll("%",".*");
        
        final List<IPerson> allPersons = (List<IPerson>) readAll(Person.class);
        
        int count = 0;        
        for (final IPerson person : (List<IPerson>) readAll(Person.class)) {
            if (name != null && name.length() > 0 && !person.getNome().matches(nameToMatch)) { continue; }
            if (email != null && email.length() > 0 && !person.getEmail().matches(emailToMatch)) { continue; }
            if (username != null && username.length() > 0) {
                if (!person.getUsername().matches(usernameToMatch)) { continue; }
                if (person.getUsername().matches("INA.*")) { continue; }
            }

            if (documentIdNumber != null && documentIdNumber.length() > 0 && 
                    !person.getNumeroDocumentoIdentificacao().matches(documentIdNumberToMatch)) {
                continue;
            }
            count++;
        }
        return new Integer(count);
    }

    public List<IPerson> readPersonsBySubName(String subName) throws ExcepcaoPersistencia {
        
        final List<IPerson> persons = (List<IPerson>) readAll(Person.class);

        final String stringToMatch = subName.replace("%", ".*").replace(" ", ".*");

        return (List<IPerson>) CollectionUtils.select(persons, new Predicate() {

            public boolean evaluate(Object object) {
                IPerson person = (IPerson) object;
                if ( person.getNome().toLowerCase().matches(stringToMatch.toLowerCase())) 
                {
                    return true;
                }
                return false;
            }

        });
    }

}