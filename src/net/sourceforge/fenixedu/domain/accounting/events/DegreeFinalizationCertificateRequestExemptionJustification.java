package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DegreeFinalizationCertificateRequestExemptionJustification extends
	DegreeFinalizationCertificateRequestExemptionJustification_Base {

    private DegreeFinalizationCertificateRequestExemptionJustification() {
	super();
    }

    DegreeFinalizationCertificateRequestExemptionJustification(final DegreeFinalizationCertificateRequestExemption exemption,
	    final DegreeFinalizationCertificateRequestJustificationType justificationType, final String reason) {
	this();
	checkParameters(justificationType);
	super.init(exemption, reason);
	setJustificationType(justificationType);
    }

    private void checkParameters(final DegreeFinalizationCertificateRequestJustificationType justificationType) {
	if (justificationType == null) {
	    throw new DomainException(
		    "error.DegreeFinalizationCertificateRequestExemptionJustification.invalid.justificationType");
	}
    }

    @Override
    public LabelFormatter getDescription() {
	return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

}
