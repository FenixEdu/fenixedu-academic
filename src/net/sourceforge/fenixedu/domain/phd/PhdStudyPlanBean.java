package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;

public class PhdStudyPlanBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Degree> degree;

    private DomainReference<PhdIndividualProgramProcess> process;

    private boolean exempted = false;

    public PhdStudyPlanBean(final PhdIndividualProgramProcess process) {
	setProcess(process);
    }

    public PhdStudyPlanBean(final PhdStudyPlan studyPlan) {
	setProcess(studyPlan.getProcess());
	setDegree(studyPlan.getDegree());
	setExempted(studyPlan.getExempted().booleanValue());
    }

    public Degree getDegree() {
	return (this.degree != null) ? this.degree.getObject() : null;
    }

    public void setDegree(Degree arg) {
	this.degree = (arg != null) ? new DomainReference<Degree>(arg) : null;
    }

    public PhdIndividualProgramProcess getProcess() {
	return (this.process != null) ? this.process.getObject() : null;
    }

    public void setProcess(PhdIndividualProgramProcess arg) {
	this.process = (arg != null) ? new DomainReference<PhdIndividualProgramProcess>(arg) : null;
    }

    public boolean isExempted() {
	return exempted;
    }

    public void setExempted(boolean exempted) {
	this.exempted = exempted;
    }

}
