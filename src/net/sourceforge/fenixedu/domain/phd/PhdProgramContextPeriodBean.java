package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdProgramContextPeriodBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private PhdProgram phdProgram;
	private LocalDate beginDate;
	private LocalDate endDate;

	public PhdProgramContextPeriodBean(final PhdProgram phdProgram) {
		setPhdProgram(phdProgram);
	}

	public PhdProgram getPhdProgram() {
		return phdProgram;
	}

	public void setPhdProgram(PhdProgram phdProgram) {
		this.phdProgram = phdProgram;
	}

	public LocalDate getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public DateTime getBeginDateAtMidnight() {
		return getBeginDate().toDateMidnight().toDateTime();
	}

	public DateTime getEndDateBeforeMidnight() {
		if (getEndDate() == null) {
			return null;
		}

		return getEndDate().plusDays(1).toDateMidnight().toDateTime().minusSeconds(1);
	}
}
