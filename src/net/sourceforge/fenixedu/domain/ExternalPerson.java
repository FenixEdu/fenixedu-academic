package net.sourceforge.fenixedu.domain;

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

        String username = "e" + documentIdNumber;

        Person person = new Person(username, name, gender, address, phone, mobile, homepage, email,
                documentIdNumber, IDDocumentType.EXTERNAL);
        
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
    
    public void delete(){        
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
            if (((person.getNome() != null && person.getNome().equals(name)) || ((person.getNome() == null || person.getNome()
                    .equals("")) && name.equals("")))
                    && ((person.getMorada() != null && person.getMorada().equals(address)) || ((person
                            .getMorada() == null || person.getMorada().equals("")) && address.equals("")))
                    && externalPerson.getInstitutionUnit().equals(institution)
                    && !externalPerson.equals(this))

                return true;
        }
        return false;
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

}
