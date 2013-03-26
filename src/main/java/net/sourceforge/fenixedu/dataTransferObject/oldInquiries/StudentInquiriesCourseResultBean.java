/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiriesCourseResultBean implements Serializable {

    private StudentInquiriesCourseResult studentInquiriesCourseResult;

    private List<StudentInquiriesTeachingResult> studentInquiriesTeachingResults =
            new ArrayList<StudentInquiriesTeachingResult>();

    public StudentInquiriesCourseResultBean(final StudentInquiriesCourseResult studentInquiriesCourseResult) {
        super();
        this.studentInquiriesCourseResult = studentInquiriesCourseResult;
    }

    public ExecutionDegree getExecutionDegree() {
        return getStudentInquiriesCourseResult().getExecutionDegree();
    }

    public StudentInquiriesCourseResult getStudentInquiriesCourseResult() {
        return studentInquiriesCourseResult;
    }

    public List<StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
        List<StudentInquiriesTeachingResult> result = new ArrayList<StudentInquiriesTeachingResult>();
        for (StudentInquiriesTeachingResult domainReference : studentInquiriesTeachingResults) {
            result.add(domainReference);
        }
        return result;
    }

    public void setStudentInquiriesTeachingResults(List<StudentInquiriesTeachingResult> studentInquiriesTeachingResults) {
        for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : studentInquiriesTeachingResults) {
            this.studentInquiriesTeachingResults.add(studentInquiriesTeachingResult);
        }
    }

    public void addStudentInquiriesTeachingResult(StudentInquiriesTeachingResult studentInquiriesTeachingResult) {
        this.studentInquiriesTeachingResults.add(studentInquiriesTeachingResult);
    }

    public boolean isToImproove() {
        if (getStudentInquiriesCourseResult().isUnsatisfactory()) {
            return true;
        }

        for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : getStudentInquiriesTeachingResults()) {
            if (studentInquiriesTeachingResult.isUnsatisfactory()) {
                return true;
            }
        }

        return false;
    }

}
