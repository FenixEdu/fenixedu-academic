/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionWithAll extends InfoStudentTestQuestionWithInfoQuestionAndInfoDistributedTest {

    public void copyFromDomain(IStudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            setStudent(InfoStudentWithInfoPerson.newInfoFromDomain(studentTestQuestion.getStudent()));
        }
    }

    public static InfoStudentTestQuestion newInfoFromDomain(IStudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestionWithAll infoStudentTestQuestion = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestion = new InfoStudentTestQuestionWithAll();
            infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestion;
    }

}