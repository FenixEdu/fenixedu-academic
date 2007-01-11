package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class StudentsSearchBean implements Serializable {

    private Integer number;

    private String identificationNumber;

    private IDDocumentType documentType;

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
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

    public Set<Student> search() {
	final Set<Student> students = new HashSet<Student>();

	if (getNumber() != null) {
	    final Registration registration = Registration.readByNumber(getNumber());
	    if (registration != null) {
		students.add(registration.getStudent());
	    }
	} else if (getIdentificationNumber() != null && getDocumentType() != null) {
	    Person person = Person.readByDocumentIdNumberAndIdDocumentType(getIdentificationNumber(),
		    getDocumentType());
	    if (person != null && person.hasStudent()) {
		students.add(person.getStudent());

	    }
	}

	return students;
    }

    public Set<Student> searchForOffice(final AdministrativeOffice administrativeOffice) {
	final Set<Student> students = new HashSet<Student>();
	for (Student student : search()) {
	    if (student.hasRegistrationForOffice(administrativeOffice)) {
		students.add(student);
	    }
	}
	return students;
    }

    // Convenience method for invocation as bean.
    public Set<Student> getSearch() {
	return search();
    }

}
