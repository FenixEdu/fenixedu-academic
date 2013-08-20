/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author tfc130
 */

public class InfoStudent extends InfoObject {

    private final Registration registration;

    public InfoStudent(final Registration registration) {
        this.registration = registration;
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

    public Boolean getPayedTuition() {
        return getRegistration().getPayedTuition();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoStudent && getRegistration() == ((InfoStudent) obj).getRegistration();
    }

    @Override
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
    public String getExternalId() {
        return getRegistration().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    private Registration getRegistration() {
        return registration;
    }
}