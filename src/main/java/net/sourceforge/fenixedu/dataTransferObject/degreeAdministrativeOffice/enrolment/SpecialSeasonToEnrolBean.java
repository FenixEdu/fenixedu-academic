package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.enrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;

public class SpecialSeasonToEnrolBean implements Serializable {

    private Enrolment enrolment;
    private boolean toSubmit;
    private EnrollmentCondition enrolmentCondition;

    public EnrollmentCondition getEnrolmentCondition() {
        return enrolmentCondition;
    }

    public void setEnrolmentCondition(EnrollmentCondition enrollmentCondition) {
        this.enrolmentCondition = enrollmentCondition;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public boolean isToSubmit() {
        return toSubmit;
    }

    public void setToSubmit(boolean toSubmit) {
        this.toSubmit = toSubmit;
    }

}
