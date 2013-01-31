package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.AdministrativeOfficeExemptionAppliance;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;

import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceExemptionBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent;

	private AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType;

	private String reason;

	private YearMonthDay dispatchDate;

	private AdministrativeOfficeExemptionAppliance applyExemptionOn;

	public AdministrativeOfficeFeeAndInsuranceExemptionBean(final AdministrativeOfficeFeeAndInsuranceEvent event) {
		setAdministrativeOfficeFeeAndInsuranceEvent(event);
	}

	public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeAndInsuranceEvent() {
		return this.administrativeOfficeFeeAndInsuranceEvent;
	}

	public void setAdministrativeOfficeFeeAndInsuranceEvent(
			AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent) {
		this.administrativeOfficeFeeAndInsuranceEvent = administrativeOfficeFeeAndInsuranceEvent;
	}

	public AdministrativeOfficeFeeAndInsuranceExemptionJustificationType getJustificationType() {
		return justificationType;
	}

	public void setJustificationType(AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType) {
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

	public AdministrativeOfficeExemptionAppliance getApplyExemptionOn() {
		return applyExemptionOn;
	}

	public void setApplyExemptionOn(AdministrativeOfficeExemptionAppliance applyExemptionOn) {
		this.applyExemptionOn = applyExemptionOn;
	}
}
