package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class RegistrationFormalizationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate whenStartedStudies;

    public LocalDate getWhenStartedStudies() {
	return whenStartedStudies;
    }

    public void setWhenStartedStudies(LocalDate whenStartedStudies) {
	this.whenStartedStudies = whenStartedStudies;
    }

}
