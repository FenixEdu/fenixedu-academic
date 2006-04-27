/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoTestQuestionWithInfoQuestion extends InfoTestQuestion {

    public void copyFromDomain(TestQuestion testQuestion) {
        super.copyFromDomain(testQuestion);
        if (testQuestion != null) {
            setQuestion(InfoQuestionWithInfoMetadata.newInfoFromDomain(testQuestion.getQuestion()));
        }
    }

    public static InfoTestQuestion newInfoFromDomain(TestQuestion testQuestion) {
        InfoTestQuestionWithInfoQuestion infoTestQuestion = null;
        if (testQuestion != null) {
            infoTestQuestion = new InfoTestQuestionWithInfoQuestion();
            infoTestQuestion.copyFromDomain(testQuestion);
        }
        return infoTestQuestion;
    }
}