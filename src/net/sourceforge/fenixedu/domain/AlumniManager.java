package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class AlumniManager {

    public Alumni checkAlumniIdentity(final String documentIdNumber, final String email) {
	Person person = getPerson(documentIdNumber);

	if (person == null) {
	    throw new DomainException("error.alumni.person.does.not.exist", documentIdNumber);
	}

	if (!person.hasStudent()) {
	    throw new DomainException("error.alumni.person.does.not.have.student.info", documentIdNumber);
	}

	checkRulesToRegisterAlumni(person.getStudent().getNumber(), documentIdNumber);

	final Alumni alumni = getAlumni(person.getStudent().getNumber());

	if (alumni.isRegistered()) {
	    throw new DomainException("error.alumni.already.registered");
	}

	alumni.addIfNotExistsEmail(email);
	return alumni;
    }

    private Person getPerson(String documentIdNumber) {
	Collection<Person> personList = Person.readByDocumentIdNumber(documentIdNumber);
	if (personList.size() > 0) {
	    return personList.iterator().next();
	}
	return null;
    }

    public Alumni registerAlumni(final Integer studentNumber, final String documentIdNumber, final String email) {

	checkRulesToRegisterAlumni(studentNumber, documentIdNumber);

	final Alumni alumni = getAlumni(studentNumber);

	if (alumni.isRegistered()) {
	    throw new DomainException("error.alumni.already.registered");
	}

	alumni.setUrlRequestToken(UUID.randomUUID());

	alumni.addIfNotExistsEmail(email);

	return alumni;

    }

    private void checkRulesToRegisterAlumni(final Integer studentNumber, final String documentIdNumber) {
	Student student = Student.readStudentByNumber(studentNumber);
	if (student == null) {

	    if (Person.findPersonByDocumentID(documentIdNumber).isEmpty()) {
		throw new DomainException("error.person.document.number");
	    } else {
		throw new DomainException("error.person.no.student");
	    }
	}

	if (!student.getPerson().getDocumentIdNumber().equals(documentIdNumber)) {
	    throw new DomainException("error.document.number.student.number.mismatch");
	}

	if (!checkAlumni(student)) {
	    if (student.hasAnyRegistrations()) {

		for (Registration registration : student.getRegistrations()) {
		    if (registration.isConcluded()) {
			return;
		    }
		}

		throw new DomainException("error.no.concluded.registrations");
	    } else {
		throw new DomainException("error.no.registrations");
	    }
	}
    }

    private boolean checkAlumni(Student alumniStudent) {

	if (alumniStudent.hasAlumni() || alumniStudent.getPerson().hasRole(RoleType.ALUMNI)) {
	    return true;
	}

	return false;
    }

    private Alumni getAlumni(final Integer studentNumber) {
	final Student student = Student.readStudentByNumber(studentNumber);
	return student.hasAlumni() ? student.getAlumni() : new Alumni(student);
    }

}
