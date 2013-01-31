package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionYear extends InfoObject {

	private final ExecutionYear executionYearDomainReference;

	public InfoExecutionYear(final ExecutionYear executionYear) {
		executionYearDomainReference = executionYear;
	}

	public ExecutionYear getExecutionYear() {
		return executionYearDomainReference;
	}

	public String getYear() {
		return getExecutionYear().getYear();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InfoExecutionYear) {
			InfoExecutionYear infoExecutionYear = (InfoExecutionYear) obj;
			return getYear().equals(infoExecutionYear.getYear());
		}
		return false;
	}

	@Override
	public String toString() {
		return getExecutionYear().getYear();
	}

	public PeriodState getState() {
		return getExecutionYear().getState();
	}

	public int compareTo(Object arg0) {
		InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;
		return this.getYear().compareTo(infoExecutionYear.getYear());
	}

	public Date getBeginDate() {
		return getExecutionYear().getBeginDate();
	}

	public Date getEndDate() {
		return getExecutionYear().getEndDate();
	}

	public static InfoExecutionYear newInfoFromDomain(final ExecutionYear executionYear) {
		return executionYear == null ? null : new InfoExecutionYear(executionYear);
	}

	public String getNextExecutionYearYear() {
		return getExecutionYear().getNextYearsYearString();
	}

	public boolean after(InfoExecutionYear infoExecutionYear) {
		return getBeginDate().after(infoExecutionYear.getEndDate());
	}

	public InfoExecutionYear getNextInfoExecutionYear() {
		final ExecutionYear nextExecutionYear = getExecutionYear().getNextExecutionYear();
		return nextExecutionYear == null ? null : InfoExecutionYear.newInfoFromDomain(nextExecutionYear);
	}

	@Override
	public Integer getIdInternal() {
		return getExecutionYear().getIdInternal();
	}

	@Override
	public void setIdInternal(Integer integer) {
		throw new Error("Method should not be called!");
	}

}