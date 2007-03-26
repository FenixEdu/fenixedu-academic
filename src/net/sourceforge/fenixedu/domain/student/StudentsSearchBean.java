package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.apache.commons.lang.StringUtils;

public class StudentsSearchBean implements Serializable {

    private Integer number;

    private String identificationNumber;

    private IDDocumentType documentType;

    private String name;

    private String username;

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

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public boolean hasSearchParameters() {
	return getNumber() != null
		|| (!StringUtils.isEmpty(getIdentificationNumber()) && getDocumentType() != null)
		|| !StringUtils.isEmpty(getName()) || !StringUtils.isEmpty(getUsername());
    }

    public Set<Student> search() {
	final Set<Student> students = new HashSet<Student>();

	if (getNumber() != null) {
	    for (final Registration registration : Registration.readByNumber(getNumber())) {
		students.add(registration.getStudent());
	    }

	    final Student student = Student.readStudentByNumber(getNumber());
	    if (student != null) {
		students.add(student);
	    }

	} else if (!StringUtils.isEmpty(getIdentificationNumber()) && getDocumentType() != null) {
	    final Person person = Person.readByDocumentIdNumberAndIdDocumentType(
		    getIdentificationNumber(), getDocumentType());
	    if (person != null && person.hasStudent()) {
		students.add(person.getStudent());
	    }
	} else if (!StringUtils.isEmpty(getName())) {
	    for (final PersonName personName : PersonName.find(getName(), Integer.MAX_VALUE)) {
		students.add(personName.getPerson().getStudent());
	    }
	} else if (!StringUtils.isEmpty(getUsername())) {
	    Login login = Login.readLoginByUsername(getUsername());
	    if (login != null && login.getUser().getPerson().hasStudent()) {
		students.add(login.getUser().getPerson().getStudent());
	    }
	}

	return students;
    }

    public Set<Student> searchForOffice(final AdministrativeOffice administrativeOffice) {
	final Set<Student> students = new TreeSet<Student>(Student.NUMBER_COMPARATOR);
	for (Student student : search()) {
	    if (student != null && student.hasRegistrationForOffice(administrativeOffice)) {
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
