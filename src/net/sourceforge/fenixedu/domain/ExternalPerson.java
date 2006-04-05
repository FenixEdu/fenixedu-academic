package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class ExternalPerson extends ExternalPerson_Base {

    public ExternalPerson() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public ExternalPerson(String name, Gender gender, String address, String phone, String mobile,
            String homepage, String email, String documentIdNumber, Unit institution) {

        this();
        Person person = Person.createPersonToExternalPerson(name, gender, address, phone, mobile,
                homepage, email, documentIdNumber, IDDocumentType.EXTERNAL);

        setPerson(person);
        setInstitutionUnit(institution);
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
            String email, Unit institution, List<ExternalPerson> allExternalPersons) {

        if (!externalPersonsAlreadyExists(name, address, institution, allExternalPersons)) {
            this.getPerson().edit(name, address, phone, mobile, homepage, email);
            this.setInstitutionUnit(institution);
        } else {
            throw new DomainException("error.exception.externalPerson.existingExternalPerson");
        }
    }

    public void delete() {
        removeInstitutionUnit();
        removePerson();
        deleteDomainObject();
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private boolean externalPersonsAlreadyExists(String name, String address, Unit institution,
            List<ExternalPerson> allExternalPersons) {
        for (ExternalPerson externalPerson : allExternalPersons) {
            Person person = externalPerson.getPerson();
            if (((person.getNome() != null && person.getNome().equals(name)) || ((person.getNome() == null || person
                    .getNome().equals("")) && name.equals("")))
                    && ((person.getAddress() != null && person.getAddress().equals(address)) || ((person
                            .getAddress() == null || person.getAddress().equals("")) && address
                            .equals("")))
                    && externalPerson.getInstitutionUnit().equals(institution)
                    && !externalPerson.equals(this))

                return true;
        }
        return false;
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

    public static List<ExternalPerson> readByName(String name) {
        final String nameToMatch = (name == null) ? null : name.replaceAll("%", ".*").toLowerCase();
        List<ExternalPerson> allExternalPersons = new ArrayList();
        for (ExternalPerson externalPerson : RootDomainObject.getInstance().getExternalPersons()) {
            if (externalPerson.getPerson().getName().toLowerCase().matches(nameToMatch)) {
                allExternalPersons.add(externalPerson);
            }
        }
        return allExternalPersons;
    }

    public static ExternalPerson readByNameAndAddressAndInstitutionID(String name, String address,
            Integer institutionID) {
        for (ExternalPerson externalPerson : RootDomainObject.getInstance().getExternalPersons()) {
            if (externalPerson.getPerson().getName().equals(name)
                    && externalPerson.getInstitutionUnit().equals(institutionID)
                    && externalPerson.getPerson().getAddress().equals(address)) {
                return externalPerson;
            }
        }
        return null;
    }

    public static List<ExternalPerson> readByIDs(Collection<Integer> externalPersonsIDs) {
        List<ExternalPerson> externalPersons = new ArrayList();
        if (externalPersonsIDs == null || externalPersonsIDs.isEmpty()) {
            return externalPersons;
        }
        for (ExternalPerson externalPerson : RootDomainObject.getInstance().getExternalPersons()) {
            if (externalPersonsIDs.contains(externalPerson.getIdInternal())) {
                externalPersons.add(externalPerson);
            }
        }
        return externalPersons;
    }
}
