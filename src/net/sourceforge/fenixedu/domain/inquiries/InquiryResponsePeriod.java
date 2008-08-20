package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;

public class InquiryResponsePeriod extends InquiryResponsePeriod_Base {

    public final static Comparator<InquiryResponsePeriod> PERIOD_COMPARATOR = new BeanComparator("executionPeriod");

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

    public static InquiryResponsePeriod readLastPeriod() {
	List<InquiryResponsePeriod> inquiryResponsePeriods = RootDomainObject.getInstance().getInquiryResponsePeriods();
	return Collections.max(inquiryResponsePeriods, PERIOD_COMPARATOR);
    }

    public static InquiryResponsePeriod readOpenPeriod() {
	final List<InquiryResponsePeriod> inquiryResponsePeriods = RootDomainObject.getInstance().getInquiryResponsePeriods();
	for (final InquiryResponsePeriod inquiryResponsePeriod : inquiryResponsePeriods) {
	    if (inquiryResponsePeriod.getBegin().isBeforeNow() && inquiryResponsePeriod.getEnd().isAfterNow()) {
		return inquiryResponsePeriod;
	    }
	}
	return null;
    }

    static public boolean hasOpenPeriod() {
	return readOpenPeriod() != null;
    }
}
