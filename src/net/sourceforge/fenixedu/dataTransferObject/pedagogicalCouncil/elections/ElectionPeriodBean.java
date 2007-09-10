package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;

import org.joda.time.YearMonthDay;

public class ElectionPeriodBean implements Serializable {
	private DegreeType degreeType;
	
	private DomainReference<ExecutionYear> executionYear;
	
	private DomainReference<Degree> degree;
	
	private DomainReference<CurricularYear> curricularYear;
	
	private YearMonthDay startDate;
	
	private YearMonthDay endDate;
	
	private DomainReference<DelegateElection> election;
	
	private boolean removeCandidacyPeriod;
	
	public ElectionPeriodBean() {
		setCurricularYear(null);
		setDegree(null);
	}

	public CurricularYear getCurricularYear() {
		return (curricularYear == null ? null : curricularYear.getObject());
	}

	public void setCurricularYear(CurricularYear curricularYear) {
		this.curricularYear = new DomainReference<CurricularYear>(curricularYear);
	}

	public Degree getDegree() {
		return (degree == null ? null : degree.getObject());
	}

	public void setDegree(Degree degree) {
		this.degree = new DomainReference<Degree>(degree);
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

	public YearMonthDay getEndDate() {
		return endDate;
	}

	public void setEndDate(YearMonthDay endDate) {
		this.endDate = endDate;
	}

	public YearMonthDay getStartDate() {
		return startDate;
	}

	public void setStartDate(YearMonthDay startDate) {
		this.startDate = startDate;
	}
	
	public DelegateElection getElection() {
		return (election == null ? null : election.getObject());
	}

	public void setElection(DelegateElection election) {
		this.election = new DomainReference<DelegateElection>(election);
	}

	public boolean getRemoveCandidacyPeriod() {
		return removeCandidacyPeriod;
	}

	public void setRemoveCandidacyPeriod(boolean removeCandidacyPeriod) {
		this.removeCandidacyPeriod = removeCandidacyPeriod;
	}

	public ExecutionYear getExecutionYear() {
		return (executionYear == null ? null : executionYear.getObject());
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
}
