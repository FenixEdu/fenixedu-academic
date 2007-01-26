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
	    final PenaltyExemptionJustificationType justificationType, final String comments) {
	this();
	init(penaltyExemption, justificationType, comments);
    }

    protected void init(PenaltyExemption penaltyExemption,
	    PenaltyExemptionJustificationType justificationType, String comments) {
	checkParameters(penaltyExemption, justificationType);
	super.setPenaltyExemption(penaltyExemption);
	super.setJustificationType(justificationType);
	super.setComments(comments);
    }

    private void checkParameters(PenaltyExemption penaltyExemption,
	    PenaltyExemptionJustificationType justificationType) {

	if (penaltyExemption == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.penaltyExemption.cannot.be.null");
	}

	if (justificationType == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.justificationType.cannot.be.null");
	}

    }

    @Override
    public void setPenaltyExemption(PenaltyExemption penaltyExemption) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.cannot.modify.penaltyExemption");
    }

    @Override
    public void setJustificationType(PenaltyExemptionJustificationType justificationType) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.cannot.modify.justificationType");
    }

    @Override
    public void setComments(String comments) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustification.cannot.modify.comments");
    }

    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getJustificationType().getQualifiedName(),
		LabelFormatter.ENUMERATION_RESOURCES);

	return labelFormatter;

    }

}
