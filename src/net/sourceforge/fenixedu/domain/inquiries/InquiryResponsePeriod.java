package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class InquiryResponsePeriod extends InquiryResponsePeriod_Base {
    
    public InquiryResponsePeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public InquiryResponsePeriod(final ExecutionPeriod executionPeriod, final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
	this();
	setExecutionPeriod(executionPeriod);
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

}
