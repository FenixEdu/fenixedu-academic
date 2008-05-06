package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;

public class Over23CandidacyProcessBean extends CandidacyProcessBean {

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

}
