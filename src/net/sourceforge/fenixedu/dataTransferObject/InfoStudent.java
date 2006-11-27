/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

    private final DomainReference<Registration> registration;

    public InfoStudent(final Registration registration) {
	this.registration = new DomainReference<Registration>(registration);
    }

    public InfoPerson getInfoPerson() {
	return InfoPerson.newInfoFromDomain(getRegistration().getPerson());
    }

    public Integer getNumber() {
	return getRegistration().getNumber();
    }

    public DegreeType getDegreeType() {
	return getRegistration().getDegreeType();
    }

    public InfoStudentKind getInfoStudentKind() {
	return InfoStudentKind.newInfoFromDomain(getRegistration().getStudentKind());
    }

    public Boolean getPayedTuition() {
	return getRegistration().getPayedTuition();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoStudent && getRegistration() == ((InfoStudent) obj).getRegistration();
    }

    public String toString() {
	return getRegistration().toString();
    }

    public static InfoStudent newInfoFromDomain(Registration registration) {
	return registration == null ? null : new InfoStudent(registration);
    }

    public Boolean getFlunked() {
	return getRegistration().getFlunked();
    }

    public Boolean getRequestedChangeDegree() {
	return getRegistration().getRequestedChangeDegree();
    }

    public Boolean getInterruptedStudies() {
	return getRegistration().getInterruptedStudies();
    }

    @Override
    public Integer getIdInternal() {
	return getRegistration().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    private Registration getRegistration() {
	return registration == null ? null : registration.getObject();
    }
}