/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiryNotAnsweredJustification;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class WriteStudentInquiryNotAnswer extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(final InquiriesRegistry inquiriesRegistry, final InquiryNotAnsweredJustification justification,
	    final String otherJustification) {
	InquiriesCourse.makeNewNotAnswered(inquiriesRegistry, justification, otherJustification);
    }

}