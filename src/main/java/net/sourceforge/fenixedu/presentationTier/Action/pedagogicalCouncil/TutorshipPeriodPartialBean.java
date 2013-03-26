package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Tutorship;

import org.joda.time.Partial;

public class TutorshipPeriodPartialBean extends TeacherTutorshipCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Partial endDate;
    private Tutorship tutorship;

    public TutorshipPeriodPartialBean(Tutorship tutorship, ExecutionDegree executionDegree) {
        super(executionDegree);
        this.tutorship = tutorship;
    }

    public Partial getEndDate() {
        return endDate;
    }

    public void setEndDate(Partial endDate) {
        this.endDate = endDate;
    }

    public Tutorship getTutorship() {
        return tutorship;
    }

    public void setTutorship(Tutorship tutorship) {
        this.tutorship = tutorship;
    }

}
