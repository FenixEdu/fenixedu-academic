package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class ExternalPerson extends ExternalPerson_Base {

    public ExternalPerson() {
    }

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public ExternalPerson(String name, Gender gender, String address, String phone, String mobile,
            String homepage, String email, String documentIdNumber, IInstitution institution) {

        String username = "e" + documentIdNumber;

        IPerson person = new Person(username, name, gender, address, phone, mobile, homepage, email,
                documentIdNumber, IDDocumentType.EXTERNAL);
        setPerson(person);
        setInstitution(institution);
    }

    public void edit(String name, String address, String phone, String mobile, String homepage,
            String email, IInstitution institution, List<IExternalPerson> allExternalPersons) {
        if (!externalPersonsAlreadyExists(name, address, institution, allExternalPersons)) {
            this.getPerson().edit(name, address, phone, mobile, homepage, email);
            this.setInstitution(institution);
        } else {
            throw new DomainException("error.exception.externalPerson.existingExternalPerson");
        }
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private boolean externalPersonsAlreadyExists(String name, String address,
            IInstitution institution, List<IExternalPerson> allExternalPersons) {
        for (IExternalPerson externalPerson : allExternalPersons) {
            if (externalPerson.getPerson().getNome().equals(name)
                    && externalPerson.getPerson().getMorada().equals(address)
                    && externalPerson.getInstitution().equals(institution)
                    && !externalPerson.equals(this))

                return true;
        }
        return false;
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

}
