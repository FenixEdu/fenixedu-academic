package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.LocalDate;

public class RegistrationFormalizationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PhdProgramCandidacyProcess process;
    private LocalDate whenStartedStudies;

    private boolean selectRegistration;
    private Registration registration;

    public RegistrationFormalizationBean(PhdProgramCandidacyProcess process) {
	this.process = process;
    }

    public LocalDate getWhenStartedStudies() {
	return whenStartedStudies;
    }

    public void setWhenStartedStudies(LocalDate whenStartedStudies) {
	this.whenStartedStudies = whenStartedStudies;
    }

    public Registration getRegistration() {
	return registration;
    }

    public void setRegistration(Registration registration) {
	this.registration = registration;
    }

    public boolean isSelectRegistration() {
	return selectRegistration;
    }

    public void setSelectRegistration(boolean selectRegistration) {
	this.selectRegistration = selectRegistration;
    }

    public Collection<Registration> getAvailableRegistrationsToAssociate() {
	return Collections.singleton(process.getPerson().getStudent().getActiveRegistrationFor(
		process.getPhdProgramLastActiveDegreeCurricularPlan()));
    }

    public boolean hasRegistration() {
	return registration != null;
    }
}
