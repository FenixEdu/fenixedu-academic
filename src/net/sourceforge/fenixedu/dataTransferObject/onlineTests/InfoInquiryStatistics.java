/*
 * Created on 9/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class InfoInquiryStatistics extends DataTranferObject implements ISiteComponent {
    private StudentTestQuestion infoStudentTestQuestion;

    private List optionStatistics;

    public InfoInquiryStatistics() {
    }

    public List getOptionStatistics() {
        return optionStatistics;
    }

    public StudentTestQuestion getInfoStudentTestQuestion() {
        return infoStudentTestQuestion;
    }

    public void setOptionStatistics(List list) {
        optionStatistics = list;
    }

    public void setInfoStudentTestQuestion(StudentTestQuestion question) {
        infoStudentTestQuestion = question;
    }

}