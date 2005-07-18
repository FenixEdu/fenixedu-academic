/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionWithInfoQuestion extends InfoStudentTestQuestion {
    public void copyFromDomain(IStudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            setQuestion(InfoQuestion.newInfoFromDomain(studentTestQuestion.getQuestion()));
        }
    }

    public static InfoStudentTestQuestion newInfoFromDomain(IStudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestionWithInfoQuestion infoStudentTestQuestion = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestion = new InfoStudentTestQuestionWithInfoQuestion();
            infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestion;
    }

}