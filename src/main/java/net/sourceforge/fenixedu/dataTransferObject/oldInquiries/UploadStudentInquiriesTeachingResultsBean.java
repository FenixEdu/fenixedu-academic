/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UploadStudentInquiriesTeachingResultsBean extends UploadStudentInquiriesResultsBean implements Serializable {

    private String keyTeacherHeader;

    private String shiftTypeHeader;

    private String unsatisfactoryResultsAssiduityHeader;

    private String unsatisfactoryResultsPedagogicalCapacityHeader;

    private String unsatisfactoryResultsPresencialLearningHeader;

    private String unsatisfactoryResultsStudentInteractionHeader;

    private String internalDegreeDisclosureHeader;

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

    public String getUnsatisfactoryResultsAssiduityHeader() {
        return unsatisfactoryResultsAssiduityHeader;
    }

    public void setUnsatisfactoryResultsAssiduityHeader(String unsatisfactoryResultsAssiduityHeader) {
        this.unsatisfactoryResultsAssiduityHeader = unsatisfactoryResultsAssiduityHeader;
    }

    public String getUnsatisfactoryResultsPedagogicalCapacityHeader() {
        return unsatisfactoryResultsPedagogicalCapacityHeader;
    }

    public void setUnsatisfactoryResultsPedagogicalCapacityHeader(String unsatisfactoryResultsPedagogicalCapacityHeader) {
        this.unsatisfactoryResultsPedagogicalCapacityHeader = unsatisfactoryResultsPedagogicalCapacityHeader;
    }

    public String getUnsatisfactoryResultsPresencialLearningHeader() {
        return unsatisfactoryResultsPresencialLearningHeader;
    }

    public void setUnsatisfactoryResultsPresencialLearningHeader(String unsatisfactoryResultsPresencialLearningHeader) {
        this.unsatisfactoryResultsPresencialLearningHeader = unsatisfactoryResultsPresencialLearningHeader;
    }

    public String getUnsatisfactoryResultsStudentInteractionHeader() {
        return unsatisfactoryResultsStudentInteractionHeader;
    }

    public void setUnsatisfactoryResultsStudentInteractionHeader(String unsatisfactoryResultsStudentInteractionHeader) {
        this.unsatisfactoryResultsStudentInteractionHeader = unsatisfactoryResultsStudentInteractionHeader;
    }

    public String getInternalDegreeDisclosureHeader() {
        return internalDegreeDisclosureHeader;
    }

    public void setInternalDegreeDisclosureHeader(String internalDegreeDisclosureHeader) {
        this.internalDegreeDisclosureHeader = internalDegreeDisclosureHeader;
    }
}
