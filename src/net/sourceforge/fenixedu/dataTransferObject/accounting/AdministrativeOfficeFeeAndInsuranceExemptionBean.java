package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationType;

import org.joda.time.YearMonthDay;

public class AdministrativeOfficeFeeAndInsuranceExemptionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<AdministrativeOfficeFeeAndInsuranceEvent> administrativeOfficeFeeAndInsuranceEvent;

    private AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType;

    private String reason;

    private YearMonthDay dispatchDate;

    public AdministrativeOfficeFeeAndInsuranceExemptionBean(final AdministrativeOfficeFeeAndInsuranceEvent event) {
	setAdministrativeOfficeFeeAndInsuranceEvent(event);
    }

    public AdministrativeOfficeFeeAndInsuranceEvent getAdministrativeOfficeFeeAndInsuranceEvent() {
	return (this.administrativeOfficeFeeAndInsuranceEvent != null) ? this.administrativeOfficeFeeAndInsuranceEvent
		.getObject() : null;
    }

    public void setAdministrativeOfficeFeeAndInsuranceEvent(
	    AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent) {
	this.administrativeOfficeFeeAndInsuranceEvent = (administrativeOfficeFeeAndInsuranceEvent != null) ? new DomainReference<AdministrativeOfficeFeeAndInsuranceEvent>(
		administrativeOfficeFeeAndInsuranceEvent)
		: null;
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

}
