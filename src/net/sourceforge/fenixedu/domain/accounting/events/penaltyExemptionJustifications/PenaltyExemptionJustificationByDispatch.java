package net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications;

import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class PenaltyExemptionJustificationByDispatch extends
	PenaltyExemptionJustificationByDispatch_Base {

    protected PenaltyExemptionJustificationByDispatch() {
	super();
    }

    public PenaltyExemptionJustificationByDispatch(final PenaltyExemption penaltyExemption,
	    final PenaltyExemptionJustificationType justificationType, final String reason,
	    final YearMonthDay dispatchDate) {
	this();
	init(penaltyExemption, justificationType, reason, dispatchDate);
    }

    private void init(PenaltyExemption penaltyExemption,
	    PenaltyExemptionJustificationType justificationType, String reason, YearMonthDay dispatchDate) {
	super.init(penaltyExemption, justificationType, reason);
	checkParameters(justificationType, dispatchDate, reason);
	super.setPenaltyExemptionDispatchDate(dispatchDate);
    }

    private void checkParameters(final PenaltyExemptionJustificationType justificationType,
	    final YearMonthDay dispatchDate, final String reason) {
	if (dispatchDate == null || StringUtils.isEmpty(reason)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
		    new LabelFormatter(justificationType.getQualifiedName(),
			    LabelFormatter.ENUMERATION_RESOURCES));
	}

    }

    @Override
    public void setPenaltyExemptionDispatchDate(YearMonthDay penaltyExemptionDispatchDate) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.cannot.modify.penaltyExemptionDispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getPenaltyExemptionJustificationType().getQualifiedName(),
		LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES)
		.appendLabel(" ").appendLabel(
			getPenaltyExemptionDispatchDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT))
		.appendLabel(")");

	return labelFormatter;
    }

}
