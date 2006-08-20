/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionWithAll extends InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest {

    public void copyFromDomain(StudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            setStudent(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
        }
    }

    public static InfoStudentTestQuestion newInfoFromDomain(StudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestionWithAll infoStudentTestQuestion = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestion = new InfoStudentTestQuestionWithAll();
            infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestion;
    }

}