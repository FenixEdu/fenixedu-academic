/**
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum InquiryResponsePeriodType {

    STUDENT, DELEGATE, TEACHING, REGENT, COORDINATOR;

    public String getName() {
	return name();
    }

}
