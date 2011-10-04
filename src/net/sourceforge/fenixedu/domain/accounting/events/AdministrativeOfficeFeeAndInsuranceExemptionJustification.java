package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustification extends
	AdministrativeOfficeFeeAndInsuranceExemptionJustification_Base {

    protected AdministrativeOfficeFeeAndInsuranceExemptionJustification() {
	super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemptionJustification(Exemption exemption,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason) {
	this();
	init(exemption, justificationType, reason);
    }

    protected void init(Exemption exemption,
	    AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason) {
	super.init(exemption, reason);
	checkParameters(exemption, justificationType);
	super.setJustificationType(justificationType);
    }

    private void checkParameters(Exemption exemption, AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType) {
	if(!exemption.isForAdministrativeOfficeFee()) {
	    throw new DomainException(
		    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustification.exemption.for.administrative.office.fee");
	}
	
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
