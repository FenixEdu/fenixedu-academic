package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class InsuranceExemptionJustificationByDispatch extends InsuranceExemptionJustificationByDispatch_Base {

    protected InsuranceExemptionJustificationByDispatch() {
        super();
    }

    public InsuranceExemptionJustificationByDispatch(final InsuranceExemption exemption,
            final InsuranceExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {
        this();
        init(exemption, justificationType, reason, dispatchDate);

    }

    private void init(InsuranceExemption exemption, InsuranceExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        checkParameters(justificationType, reason, dispatchDate);

        super.init(exemption, justificationType, reason);

        super.setDispatchDate(dispatchDate);

    }

    private void checkParameters(InsuranceExemptionJustificationType justificationType, String reason, YearMonthDay dispatchDate) {
        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.InsuranceExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
        }
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
        String formatedDate = getDispatchDate() != null ? getDispatchDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(formatedDate).appendLabel(")");

        return labelFormatter;
    }

    @Deprecated
    public boolean hasDispatchDate() {
        return getDispatchDate() != null;
    }

}
