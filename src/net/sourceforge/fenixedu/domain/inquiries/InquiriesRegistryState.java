/**
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum InquiriesRegistryState {

    ANSWERED, NOT_ANSWERED, ANSWER_LATER, UNAVAILABLE, TEACHERS_TO_ANSWER /*
									   * exceptional state created because the lack of connection
									   * between the answers the teacher/shift
									   */;

}
