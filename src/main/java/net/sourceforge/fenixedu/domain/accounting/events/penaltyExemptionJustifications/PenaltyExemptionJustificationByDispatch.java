package net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications;

import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class PenaltyExemptionJustificationByDispatch extends PenaltyExemptionJustificationByDispatch_Base {

    protected PenaltyExemptionJustificationByDispatch() {
        super();
    }

    public PenaltyExemptionJustificationByDispatch(final PenaltyExemption penaltyExemption,
            final PenaltyExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {
        this();
        init(penaltyExemption, justificationType, reason, dispatchDate);
    }

    private void init(PenaltyExemption penaltyExemption, PenaltyExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        super.init(penaltyExemption, justificationType, reason);
        checkParameters(justificationType, dispatchDate, reason);
        super.setPenaltyExemptionDispatchDate(dispatchDate);
    }

    private void checkParameters(final PenaltyExemptionJustificationType justificationType, final YearMonthDay dispatchDate,
            final String reason) {
        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
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
        String penaltyExemptionDate =
                getPenaltyExemptionDispatchDate() != null ? getPenaltyExemptionDispatchDate().toString(
                        DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(penaltyExemptionDate).appendLabel(")");

        return labelFormatter;
    }

    @Deprecated
    public boolean hasPenaltyExemptionDispatchDate() {
        return getPenaltyExemptionDispatchDate() != null;
    }

}
