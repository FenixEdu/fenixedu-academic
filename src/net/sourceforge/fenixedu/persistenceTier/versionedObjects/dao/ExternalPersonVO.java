package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ExternalPersonVO extends VersionedObjectsBase implements IPersistentExternalPerson {

    public ExternalPerson readByUsername(String username)  {
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        for (final ExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getUsername().equals(username)){
                return externalPerson;
            }
        }
        return null;
    }

    public List<ExternalPerson> readByName(String name) {
        final String nameToMatch = name.replaceAll("%",".*");
        final List<ExternalPerson> matchedExternalPersons = new ArrayList<ExternalPerson>();
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        
        for (final ExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getNome().matches(nameToMatch)){
                matchedExternalPersons.add(externalPerson);
            }
        }

        return matchedExternalPersons;
    }

    public ExternalPerson readByNameAndAddressAndInstitutionID(String name, String address,
            Integer institutionID) {
        
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        for (final ExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getNome().equals(name)
                    && externalPerson.getPerson().getMorada().equals(address)
                    && externalPerson.getInstitution().getIdInternal().equals(institutionID)){
                return externalPerson;
            }
        }
        return null;
        
    }

    public List<ExternalPerson> readByInstitution(Integer institutionID) {
        
        final List<ExternalPerson> matchedExternalPersons = new ArrayList<ExternalPerson>();
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        
        for (final ExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getInstitution().getIdInternal().equals(institutionID)) {
                matchedExternalPersons.add(externalPerson);
            }
        }

        return matchedExternalPersons;
        
    }

    public String readLastDocumentIdNumber() {
        
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        
        ExternalPerson personWithMaxDocumentIdNumber = (ExternalPerson)
            Collections.max(allExternalPersons, new Comparator() {
                public int compare(Object o1, Object o2) {
                    ExternalPerson person1 = (ExternalPerson) o1;
                    ExternalPerson person2 = (ExternalPerson) o2;
                    Integer number1 = Integer.valueOf(person1.getPerson().getNumeroDocumentoIdentificacao());
                    Integer number2 = Integer.valueOf(person2.getPerson().getNumeroDocumentoIdentificacao());
                    
                    return (number1 < number2) ? -1 : ((number1 == number2) ? 0 : 1);
                }
            });
        
        return personWithMaxDocumentIdNumber.getPerson().getNumeroDocumentoIdentificacao();
        
    }

    public List<ExternalPerson> readByIDs(Collection<Integer> externalPersonsIDs) {
        
        final List<ExternalPerson> matchedExternalPersons = new ArrayList<ExternalPerson>();
        final List<ExternalPerson> allExternalPersons = (List<ExternalPerson>)readAll(ExternalPerson.class);
        
        for (final ExternalPerson externalPerson : allExternalPersons) {
            if (externalPersonsIDs.contains(externalPerson.getIdInternal())) {
                matchedExternalPersons.add(externalPerson);
            }
        }
        
        return matchedExternalPersons;
        
    }

}