package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustification extends
	AdministrativeOfficeFeeAndInsuranceExemptionJustification_Base {

    protected AdministrativeOfficeFeeAndInsuranceExemptionJustification() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemptionJustification(AdministrativeOfficeFeeAndInsuranceExemption exemption,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason) {
	this();
	init(exemption, justificationType, reason);
    }

    protected void init(AdministrativeOfficeFeeAndInsuranceExemption exemption,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason) {
	super.init(exemption, reason);
	checkParameters(justificationType);
	super.setJustificationType(justificationType);
    }

    private void checkParameters(AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType) {
	if (justificationType == null) {
	    throw new DomainException(
		    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustification.justificationType.cannot.be.null");
	}

    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);

	return labelFormatter;
    }

}
