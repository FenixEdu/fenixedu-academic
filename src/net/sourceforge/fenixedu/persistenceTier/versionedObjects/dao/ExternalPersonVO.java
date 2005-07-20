package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ExternalPersonVO extends VersionedObjectsBase implements IPersistentExternalPerson {

    public IExternalPerson readByUsername(String username)  {
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        for (final IExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getUsername().equals(username)){
                return externalPerson;
            }
        }
        return null;
    }

    public List<IExternalPerson> readByName(String name) {
        final String nameToMatch = name.replaceAll("%",".*");
        final List<IExternalPerson> matchedExternalPersons = new ArrayList<IExternalPerson>();
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        
        for (final IExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getNome().matches(nameToMatch)){
                matchedExternalPersons.add(externalPerson);
            }
        }

        return matchedExternalPersons;
    }

    public IExternalPerson readByNameAndAddressAndWorkLocationID(String name, String address,
            Integer workLocationID) {
        
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        for (final IExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getNome().equals(name)
                    && externalPerson.getPerson().getMorada().equals(address)
                    && externalPerson.getWorkLocation().getIdInternal().equals(workLocationID)){
                return externalPerson;
            }
        }
        return null;
        
    }

    public List<IExternalPerson> readByWorkLocation(Integer workLocationID) {
        
        final List<IExternalPerson> matchedExternalPersons = new ArrayList<IExternalPerson>();
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        
        for (final IExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getWorkLocation().getIdInternal().equals(workLocationID)) {
                matchedExternalPersons.add(externalPerson);
            }
        }

        return matchedExternalPersons;
        
    }

    public String readLastDocumentIdNumber() {
        
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        
        IExternalPerson personWithMaxDocumentIdNumber = (IExternalPerson)
            Collections.max(allExternalPersons, new Comparator() {
                public int compare(Object o1, Object o2) {
                    IExternalPerson person1 = (IExternalPerson) o1;
                    IExternalPerson person2 = (IExternalPerson) o2;
                    Integer number1 = Integer.valueOf(person1.getPerson().getNumeroDocumentoIdentificacao());
                    Integer number2 = Integer.valueOf(person2.getPerson().getNumeroDocumentoIdentificacao());
                    
                    return (number1 < number2) ? -1 : ((number1 == number2) ? 0 : 1);
                }
            });
        
        return personWithMaxDocumentIdNumber.getPerson().getNumeroDocumentoIdentificacao();
        
    }

    public List<IExternalPerson> readByIDs(Collection<Integer> externalPersonsIDs) {
        
        final List<IExternalPerson> matchedExternalPersons = new ArrayList<IExternalPerson>();
        final List<IExternalPerson> allExternalPersons = (List<IExternalPerson>)readAll(ExternalPerson.class);
        
        for (final IExternalPerson externalPerson : allExternalPersons) {
            if (externalPersonsIDs.contains(externalPerson.getIdInternal())) {
                matchedExternalPersons.add(externalPerson);
            }
        }
        
        return matchedExternalPersons;
        
    }

}