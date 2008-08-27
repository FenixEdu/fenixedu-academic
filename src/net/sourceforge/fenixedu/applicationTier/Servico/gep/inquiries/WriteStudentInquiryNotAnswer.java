/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiryNotAnsweredJustification;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class WriteStudentInquiryNotAnswer extends Service {

    public void run(final InquiriesRegistry inquiriesRegistry, final InquiryNotAnsweredJustification justification,
	    final String otherJustification) {
	InquiriesCourse.makeNewNotAnswered(inquiriesRegistry, justification, otherJustification);
    }

}
