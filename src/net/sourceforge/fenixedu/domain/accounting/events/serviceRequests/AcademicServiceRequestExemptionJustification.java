package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AcademicServiceRequestExemptionJustification extends AcademicServiceRequestExemptionJustification_Base {

    private AcademicServiceRequestExemptionJustification() {
	super();
    }

    public AcademicServiceRequestExemptionJustification(final AcademicServiceRequestExemption exemption,
	    final AcademicServiceRequestJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

	this();

	check(justificationType, "error.AcademicServiceRequestExemptionJustification.invalid.justificationType");
	check(dispatchDate, "error.AcademicServiceRequestExemptionJustification.invalid.dispatchDate");

	super.init(exemption, reason);
	setJustificationType(justificationType);
	setDispatchDate(dispatchDate);
    }

    @Override
    public LabelFormatter getDescription() {
	return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

}
