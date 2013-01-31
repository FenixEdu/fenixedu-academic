package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;

public class PhdStudyPlanBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Degree degree;

	private PhdIndividualProgramProcess process;

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
		return this.degree;
	}

	public void setDegree(Degree arg) {
		this.degree = arg;
	}

	public PhdIndividualProgramProcess getProcess() {
		return this.process;
	}

	public void setProcess(PhdIndividualProgramProcess arg) {
		this.process = arg;
	}

	public boolean isExempted() {
		return exempted;
	}

	public void setExempted(boolean exempted) {
		this.exempted = exempted;
	}

}
