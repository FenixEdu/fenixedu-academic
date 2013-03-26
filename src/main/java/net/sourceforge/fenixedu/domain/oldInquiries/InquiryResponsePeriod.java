package net.sourceforge.fenixedu.domain.oldInquiries;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriodType;

import org.joda.time.DateTime;

public class InquiryResponsePeriod extends InquiryResponsePeriod_Base {

    public final static Comparator<InquiryResponsePeriod> PERIOD_COMPARATOR = new Comparator<InquiryResponsePeriod>() {

        @Override
        public int compare(InquiryResponsePeriod o1, InquiryResponsePeriod o2) {
            return o1.getExecutionPeriod().compareTo(o2.getExecutionPeriod());
        }

    };

    public InquiryResponsePeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public InquiryResponsePeriod(final ExecutionSemester executionSemester, final Date inquiryResponseBegin,
            final Date inquiryResponseEnd) {
        this();
        setExecutionPeriod(executionSemester);
        setPeriod(inquiryResponseBegin, inquiryResponseEnd);
    }

    public void setPeriod(final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
        setBegin(new DateTime(inquiryResponseBegin));
        setEnd(new DateTime(inquiryResponseEnd));
    }

    public boolean isInsideResponsePeriod() {
        return getBegin().isBeforeNow() && getEnd().isAfterNow();
    }

    public boolean insidePeriod() {
        return !getBegin().isAfterNow() && !getEnd().isBeforeNow();
    }

    public static InquiryResponsePeriod readOpenPeriod(final InquiryResponsePeriodType type) {
        final List<InquiryResponsePeriod> inquiryResponsePeriods = RootDomainObject.getInstance().getInquiryResponsePeriods();
        for (final InquiryResponsePeriod inquiryResponsePeriod : inquiryResponsePeriods) {
            if (inquiryResponsePeriod.getType() == type && inquiryResponsePeriod.isOpen()) {
                return inquiryResponsePeriod;
            }
        }
        return null;
    }

    public boolean isOpen() {
        return getBegin().isBeforeNow() && getEnd().isAfterNow();
    }

    static public boolean hasOpenPeriod(final InquiryResponsePeriodType type) {
        return readOpenPeriod(type) != null;
    }

    public static InquiryResponsePeriod readLastPeriod(final InquiryResponsePeriodType type) {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        while (executionSemester.getInquiryResponsePeriod(type) == null) {
            executionSemester = executionSemester.getPreviousExecutionPeriod();
        }
        return executionSemester.getInquiryResponsePeriod(type);
    }

    public boolean isBeforeNow() {
        return getEnd().isBeforeNow();
    }

    public boolean isAfterNow() {
        return getBegin().isAfterNow();
    }
}
