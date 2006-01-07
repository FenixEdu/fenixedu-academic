/*
 * Created on 1/Jul/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.Question;

/**
 * @author Susana Fernandes
 */
public class InfoQuestionWithInfoMetadata extends InfoQuestion {

    public void copyFromDomain(Question question) {
        super.copyFromDomain(question);
        if (question != null) {
            setInfoMetadata(InfoMetadataWithVisibleQuestions.newInfoFromDomain(question.getMetadata()));
        }
    }

    public static InfoQuestion newInfoFromDomain(Question question) {
        InfoQuestionWithInfoMetadata infoQuestion = null;
        if (question != null) {
            infoQuestion = new InfoQuestionWithInfoMetadata();
            infoQuestion.copyFromDomain(question);
        }
        return infoQuestion;
    }
}