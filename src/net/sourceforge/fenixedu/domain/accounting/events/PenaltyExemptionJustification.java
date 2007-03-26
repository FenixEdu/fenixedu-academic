package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class PenaltyExemptionJustification extends PenaltyExemptionJustification_Base {

    protected PenaltyExemptionJustification() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(this.getClass().getName());
    }

    public PenaltyExemptionJustification(final PenaltyExemption penaltyExemption,
	    final PenaltyExemptionJustificationType justificationType, final String reason) {
	this();
	init(penaltyExemption, justificationType, reason);
    }

    protected void init(PenaltyExemption penaltyExemption,
	    PenaltyExemptionJustificationType justificationType, String reason) {
	checkParameters(justificationType);
	super.init(penaltyExemption, reason);
	super.setPenaltyExemptionJustificationType(justificationType);
    }

    private void checkParameters(PenaltyExemptionJustificationType justificationType) {
	if (justificationType == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.justificationType.cannot.be.null");
	}

    }

    @Override
    public void setPenaltyExemptionJustificationType(
	    PenaltyExemptionJustificationType penaltyExemptionJustificationType) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.cannot.modify.penaltyExemptionJustificationType");
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getPenaltyExemptionJustificationType().getQualifiedName(),
		LabelFormatter.ENUMERATION_RESOURCES);

	return labelFormatter;

    }

}
