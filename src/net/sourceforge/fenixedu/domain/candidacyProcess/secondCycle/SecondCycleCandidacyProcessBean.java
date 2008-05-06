package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;

public class SecondCycleCandidacyProcessBean extends CandidacyProcessBean {
    
    public SecondCycleCandidacyProcessBean() {
    }

    public SecondCycleCandidacyProcessBean(final AcademicPeriod academicPeriod) {
	setAcademicPeriod(academicPeriod);
    }

    public SecondCycleCandidacyProcessBean(final SecondCycleCandidacyProcess process) {
	setAcademicPeriod(process.getCandidacyAcademicPeriod());
	setStart(process.getCandidacyStart());
	setEnd(process.getCandidacyEnd());
    }

}
