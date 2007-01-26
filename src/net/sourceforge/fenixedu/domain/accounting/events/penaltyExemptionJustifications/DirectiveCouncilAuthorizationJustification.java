package net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications;

import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.YearMonthDay;

public class DirectiveCouncilAuthorizationJustification extends
	DirectiveCouncilAuthorizationJustification_Base {

    protected DirectiveCouncilAuthorizationJustification() {
	super();
    }

    public DirectiveCouncilAuthorizationJustification(final PenaltyExemption penaltyExemption,
	    final String comments, final YearMonthDay dispatchDate) {
	this();
	init(penaltyExemption, PenaltyExemptionJustificationType.DIRECTIVE_COUNCIL_AUTHORIZATION,
		comments, dispatchDate);
    }

    private void init(PenaltyExemption penaltyExemption,
	    PenaltyExemptionJustificationType penaltyExemptionType, String comments,
	    YearMonthDay dispatchDate) {
	super.init(penaltyExemption, penaltyExemptionType, comments);
	checkParameters(dispatchDate);
	super.setDispatchDate(dispatchDate);
    }

    private void checkParameters(YearMonthDay dispatchDate) {
	if (dispatchDate == null) {
	    throw new DomainException(
		    "error.accounting.events.penaltyExemptionJustifications.DirectiveCouncilAuthorizationJustification.dispatchDate.for.justification.cannot.be.null");
	}

    }

    @Override
    public void setDispatchDate(YearMonthDay dispatchDate) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications.DirectiveCouncilAuthorizationJustification.cannot.modify.dispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(getJustificationType().getQualifiedName(),
		LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES)
		.appendLabel(" ").appendLabel(
			getDispatchDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT)).appendLabel(")");

	return labelFormatter;
    }

}
