package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType;

public class MobilityApplicationProcessBean extends CandidacyProcessBean {

    private ErasmusApplyForSemesterType forSemester;

    public MobilityApplicationProcessBean(final ExecutionInterval executionInterval) {
	super(executionInterval);
    }

    public MobilityApplicationProcessBean(final CandidacyProcess process) {
	super(process);
    }

    public ErasmusApplyForSemesterType getForSemester() {
	return forSemester;
    }

    public void setForSemester(ErasmusApplyForSemesterType forSemester) {
	this.forSemester = forSemester;
    }

}
