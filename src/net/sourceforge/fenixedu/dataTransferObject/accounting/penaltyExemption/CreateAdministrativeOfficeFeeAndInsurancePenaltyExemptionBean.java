package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;

public class CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean extends
	CreatePenaltyExemptionBean implements Serializable {

    public CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean(
	    final AdministrativeOfficeFeeAndInsuranceEvent administrativeOfficeFeeAndInsuranceEvent) {
	super(administrativeOfficeFeeAndInsuranceEvent);
    }

}
