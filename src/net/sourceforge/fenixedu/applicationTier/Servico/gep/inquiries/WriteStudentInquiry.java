/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiryDTO;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class WriteStudentInquiry extends Service {

    public void run(final StudentInquiryDTO studentInquiry) {
	InquiriesCourse.makeNew(studentInquiry);
    }

}
