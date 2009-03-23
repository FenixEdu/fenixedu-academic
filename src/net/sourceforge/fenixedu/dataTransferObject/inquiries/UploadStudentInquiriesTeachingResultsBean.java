/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UploadStudentInquiriesTeachingResultsBean extends UploadStudentInquiriesResultsBean implements Serializable {

    private String keyTeacherHeader;

    private String shiftTypeHeader;

    public String getKeyTeacherHeader() {
	return keyTeacherHeader;
    }

    public void setKeyTeacherHeader(String keyTeacherHeader) {
	this.keyTeacherHeader = keyTeacherHeader;
    }

    public String getShiftTypeHeader() {
	return shiftTypeHeader;
    }

    public void setShiftTypeHeader(String shiftTypeHeader) {
	this.shiftTypeHeader = shiftTypeHeader;
    }

}
