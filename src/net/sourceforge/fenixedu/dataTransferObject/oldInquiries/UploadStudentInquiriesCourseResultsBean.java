/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UploadStudentInquiriesCourseResultsBean extends UploadStudentInquiriesResultsBean implements Serializable {

	private String unsatisfactoryResultsCUEvaluationHeader;

	private String unsatisfactoryResultsCUOrganizationHeader;

	public String getUnsatisfactoryResultsCUEvaluationHeader() {
		return unsatisfactoryResultsCUEvaluationHeader;
	}

	public void setUnsatisfactoryResultsCUEvaluationHeader(String unsatisfactoryResultsCUEvaluationHeader) {
		this.unsatisfactoryResultsCUEvaluationHeader = unsatisfactoryResultsCUEvaluationHeader;
	}

	public String getUnsatisfactoryResultsCUOrganizationHeader() {
		return unsatisfactoryResultsCUOrganizationHeader;
	}

	public void setUnsatisfactoryResultsCUOrganizationHeader(String unsatisfactoryResultsCUOrganizationHeader) {
		this.unsatisfactoryResultsCUOrganizationHeader = unsatisfactoryResultsCUOrganizationHeader;
	}
}
