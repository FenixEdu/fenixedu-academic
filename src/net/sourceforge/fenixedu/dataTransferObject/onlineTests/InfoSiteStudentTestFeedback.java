/*
 * Created on Oct 24, 2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoSiteStudentTestFeedback extends DataTranferObject implements ISiteComponent {

    private Integer responseNumber;

    private Integer notResponseNumber;

    private List<String> errors;

    private List studentTestQuestionList;

    private StudentTestLog studentTestLog;

    public InfoSiteStudentTestFeedback() {
    }

    public List<String> getErrors() {
	return errors;
    }

    public void setErrors(List<String> errors) {
	this.errors = errors;
    }

    public Integer getNotResponseNumber() {
	return notResponseNumber;
    }

    public void setNotResponseNumber(Integer notResponseNumber) {
	this.notResponseNumber = notResponseNumber;
    }

    public Integer getResponseNumber() {
	return responseNumber;
    }

    public void setResponseNumber(Integer responseNumber) {
	this.responseNumber = responseNumber;
    }

    public List getStudentTestQuestionList() {
	return studentTestQuestionList;
    }

    public void setStudentTestQuestionList(List studentTestQuestionList) {
	this.studentTestQuestionList = studentTestQuestionList;
    }

    public StudentTestLog getStudentTestLog() {
	return studentTestLog;
    }

    public void setStudentTestLog(StudentTestLog studentTestLog) {
	this.studentTestLog = studentTestLog;
    }
}