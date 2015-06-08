package org.fenixedu.academic.dto.administrativeOffice.dismissal;

import java.io.Serializable;

import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.joda.time.LocalDate;

public class CreditsBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Credits credits;

    private LocalDate officialDate;

    public CreditsBean(final Credits credits) {
        this.credits = credits;
        this.officialDate = credits.getOfficialDate();
    }

    public Credits getCredits() {
        return credits;
    }

    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    public LocalDate getOfficialDate() {
        return officialDate;
    }

    public void setOfficialDate(LocalDate officialDate) {
        this.officialDate = officialDate;
    }

}
