/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentInquiriesCourseResultBean implements Serializable {

    private DomainReference<ExecutionDegree> executionDegree;

    private DomainReference<StudentInquiriesCourseResult> studentInquiriesCourseResult;

    private List<DomainReference<StudentInquiriesTeachingResult>> studentInquiriesTeachingResults = new ArrayList<DomainReference<StudentInquiriesTeachingResult>>();

    public StudentInquiriesCourseResultBean(final ExecutionDegree executionDegree,
	    final StudentInquiriesCourseResult studentInquiriesCourseResult) {
	super();
	this.executionDegree = new DomainReference<ExecutionDegree>(executionDegree);
	this.studentInquiriesCourseResult = new DomainReference<StudentInquiriesCourseResult>(studentInquiriesCourseResult);
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree.getObject();
    }

    public StudentInquiriesCourseResult getStudentInquiriesCourseResult() {
	return studentInquiriesCourseResult.getObject();
    }

    public List<StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
	List<StudentInquiriesTeachingResult> result = new ArrayList<StudentInquiriesTeachingResult>();
	for (DomainReference<StudentInquiriesTeachingResult> domainReference : studentInquiriesTeachingResults) {
	    result.add(domainReference.getObject());
	}
	return result;
    }

    public void setStudentInquiriesTeachingResults(List<StudentInquiriesTeachingResult> studentInquiriesTeachingResults) {
	for (StudentInquiriesTeachingResult studentInquiriesTeachingResult : studentInquiriesTeachingResults) {
	    this.studentInquiriesTeachingResults.add(new DomainReference<StudentInquiriesTeachingResult>(
		    studentInquiriesTeachingResult));
	}
    }

    public void addStudentInquiriesTeachingResult(StudentInquiriesTeachingResult studentInquiriesTeachingResult) {
	this.studentInquiriesTeachingResults.add(new DomainReference<StudentInquiriesTeachingResult>(
		studentInquiriesTeachingResult));
    }
}
