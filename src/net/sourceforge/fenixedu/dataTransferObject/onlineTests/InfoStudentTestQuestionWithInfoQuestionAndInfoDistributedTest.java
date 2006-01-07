/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest extends InfoStudentTestQuestionWithInfoQuestion {

    public void copyFromDomain(StudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            setDistributedTest(InfoDistributedTestWithTestScope.newInfoFromDomain(studentTestQuestion.getDistributedTest()));
        }
    }

    public static InfoStudentTestQuestion newInfoFromDomain(StudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest infoStudentTestQuestion = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestion = new InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest();
            infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestion;
    }

}