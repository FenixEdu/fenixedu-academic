package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemptionJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;

import org.joda.time.YearMonthDay;

public class InsuranceExemptionBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private InsuranceEvent insuranceEvent;

	private InsuranceExemptionJustificationType justificationType;

	private String reason;

	private YearMonthDay dispatchDate;

	public InsuranceExemptionBean(final InsuranceEvent event) {
		setInsuranceEvent(event);
	}

	public InsuranceEvent getInsuranceEvent() {
		return insuranceEvent;
	}

	public void setInsuranceEvent(InsuranceEvent insuranceEvent) {
		this.insuranceEvent = insuranceEvent;
	}

	public InsuranceExemptionJustificationType getJustificationType() {
		return justificationType;
	}

	public void setJustificationType(InsuranceExemptionJustificationType justificationType) {
		this.justificationType = justificationType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public YearMonthDay getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(YearMonthDay dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

}
