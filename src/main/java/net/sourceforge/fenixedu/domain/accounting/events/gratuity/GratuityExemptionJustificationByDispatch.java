package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class GratuityExemptionJustificationByDispatch extends GratuityExemptionJustificationByDispatch_Base {

    protected GratuityExemptionJustificationByDispatch() {
        super();
    }

    public GratuityExemptionJustificationByDispatch(final GratuityExemption gratuityExemption,
            final GratuityExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {
        this();
        init(gratuityExemption, justificationType, reason, dispatchDate);
    }

    protected void init(GratuityExemption gratuityExemption, GratuityExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        checkParameters(justificationType, reason, dispatchDate);
        super.init(gratuityExemption, justificationType, reason);
        super.setGratuityExemptionDispatchDate(dispatchDate);
    }

    private void checkParameters(final GratuityExemptionJustificationType justificationType, final String reason,
            final YearMonthDay dispatchDate) {
        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.GratuityExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
        }

    }

    @Override
    public void setGratuityExemptionDispatchDate(YearMonthDay dispatchDate) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustificationByDispatch.cannot.modify.dispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getGratuityExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);
        String gratuityExemptionDate =
                getGratuityExemptionDispatchDate() != null ? getGratuityExemptionDispatchDate().toString(
                        DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(gratuityExemptionDate).appendLabel(")");

        return labelFormatter;
    }

    @Deprecated
    public boolean hasGratuityExemptionDispatchDate() {
        return getGratuityExemptionDispatchDate() != null;
    }

}
