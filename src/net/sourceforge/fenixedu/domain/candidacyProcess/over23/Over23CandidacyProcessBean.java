package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.DomainReference;

public class Over23CandidacyProcessBean implements Serializable {

    private DomainReference<AcademicPeriod> academicPeriod;

    private DateTime start, end;

    public Over23CandidacyProcessBean() {
    }

    public Over23CandidacyProcessBean(final AcademicPeriod academicPeriod) {
	setAcademicPeriod(academicPeriod);
    }

    public Over23CandidacyProcessBean(final Over23CandidacyProcess process) {
	setAcademicPeriod(process.getCandidacyAcademicPeriod());
	setStart(process.getCandidacyStart());
	setEnd(process.getCandidacyEnd());
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
