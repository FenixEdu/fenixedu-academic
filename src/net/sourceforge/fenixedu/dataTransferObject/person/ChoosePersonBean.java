package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class ChoosePersonBean implements Serializable {

    private DomainReference<Person> person;

    private String identificationNumber;

    private IDDocumentType documentType;

    public ChoosePersonBean() {
	super();
    }

    public ChoosePersonBean(Person person) {
	this();
	setPerson(person);
    }

    public Person getPerson() {
	return (this.person != null) ? this.person.getObject() : null;
    }

    public void setPerson(Person person) {
	this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public IDDocumentType getDocumentType() {
	return documentType;
    }

    public void setDocumentType(IDDocumentType documentType) {
	this.documentType = documentType;
    }

    public String getIdentificationNumber() {
	return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
	this.identificationNumber = identificationNumber;
    }

}
