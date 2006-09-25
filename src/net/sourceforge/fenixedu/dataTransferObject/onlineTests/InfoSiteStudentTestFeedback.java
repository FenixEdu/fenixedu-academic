/*
 * Created on Oct 24, 2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;

/**
 * @author Susana Fernandes
 */
public class InfoSiteStudentTestFeedback extends DataTranferObject implements ISiteComponent {

    private Integer responseNumber;

    private Integer notResponseNumber;

    private List errors;

    private List studentTestQuestionList;

    public InfoSiteStudentTestFeedback() {
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
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
}