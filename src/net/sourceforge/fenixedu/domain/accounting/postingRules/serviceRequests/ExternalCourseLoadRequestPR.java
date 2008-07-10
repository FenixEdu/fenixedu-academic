package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ExternalCourseLoadRequestPR extends ExternalCourseLoadRequestPR_Base {

    private ExternalCourseLoadRequestPR() {
	super();
    }

    public ExternalCourseLoadRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
	this();
	super.init(EntryType.EXTERNAL_COURSE_LOAD_REQUEST_FEE, EventType.EXTERNAL_COURSE_LOAD_REQUEST, startDate, endDate,
		serviceAgreementTemplate, certificateAmount, amountPerPage);
	checkParameters(amountFirstPage);
	super.setAmountFirstPage(amountFirstPage);
    }
}
