/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StudentState;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

	private final Registration registration;

    public InfoStudent(final Registration registration) {
    	this.registration = registration;
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(registration.getPerson());
    }

    public Integer getNumber() {
        return registration.getNumber();
    }

    public StudentState getState() {
        return registration.getState();
    }

    public DegreeType getDegreeType() {
        return registration.getDegreeType();
    }

    public InfoStudentKind getInfoStudentKind() {
        return InfoStudentKind.newInfoFromDomain(registration.getStudentKind());
    }

    public Boolean getPayedTuition() {
        return registration.getPayedTuition();
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoStudent && registration == ((InfoStudent) obj).registration;
    }

    public String toString() {
    	return registration.toString();
    }

    public static InfoStudent newInfoFromDomain(Registration registration) {
    	return registration == null ? null : new InfoStudent(registration);
    }

	public Boolean getFlunked() {
		return registration.getFlunked();
	}

	public Boolean getRequestedChangeDegree() {
		return registration.getRequestedChangeDegree();
	}

	public Boolean getInterruptedStudies() {
		return registration.getInterruptedStudies();
	}

	@Override
	public Integer getIdInternal() {
		return registration.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}