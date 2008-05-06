package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.DomainReference;

import org.joda.time.DateTime;

abstract public class CandidacyProcessBean implements Serializable {

    private DomainReference<AcademicPeriod> academicPeriod;

    private DateTime start, end;

    protected CandidacyProcessBean() {
    }

    public AcademicPeriod getAcademicPeriod() {
	return (this.academicPeriod != null) ? this.academicPeriod.getObject() : null;
    }

    public void setAcademicPeriod(AcademicPeriod academicPeriod) {
	this.academicPeriod = (academicPeriod != null) ? new DomainReference<AcademicPeriod>(academicPeriod) : null;
    }

    public DateTime getStart() {
	return start;
    }

    public void setStart(DateTime start) {
	this.start = start;
    }

    public DateTime getEnd() {
	return end;
    }

    public void setEnd(DateTime end) {
	this.end = end;
    }
}
