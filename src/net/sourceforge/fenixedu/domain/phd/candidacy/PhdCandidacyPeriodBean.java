package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.joda.time.DateTime;

public class PhdCandidacyPeriodBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ExecutionYear executionYear;
	private DateTime start;
	private DateTime end;
	private PhdCandidacyPeriodType type;

	private PhdProgram phdProgram;

	private List<PhdProgram> phdProgramList;

	public PhdCandidacyPeriodBean() {
	}

	public PhdCandidacyPeriodBean(final PhdCandidacyPeriod phdCandidacyPeriod) {
		this.start = phdCandidacyPeriod.getStart();
		this.end = phdCandidacyPeriod.getEnd();
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

	public PhdCandidacyPeriodType getType() {
		return type;
	}

	public void setType(PhdCandidacyPeriodType type) {
		this.type = type;
	}

	public ExecutionYear getExecutionYear() {
		return executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public PhdProgram getPhdProgram() {
		return phdProgram;
	}

	public void setPhdProgram(PhdProgram phdProgram) {
		this.phdProgram = phdProgram;
	}

	public List<PhdProgram> getPhdProgramList() {
		return phdProgramList;
	}

	public void setPhdProgramList(List<PhdProgram> phdProgramList) {
		this.phdProgramList = phdProgramList;
	}

}
