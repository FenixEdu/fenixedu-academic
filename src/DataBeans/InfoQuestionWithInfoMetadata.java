/*
 * Created on 1/Jul/2004
 */
package DataBeans;

import Dominio.IQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoQuestionWithInfoMetadata extends InfoQuestion {

    public void copyFromDomain(IQuestion question) {
        super.copyFromDomain(question);
        if (question != null) {
            setInfoMetadata(InfoMetadata.newInfoFromDomain(question.getMetadata()));
        }
    }

    public static InfoQuestion newInfoFromDomain(IQuestion question) {
        InfoQuestionWithInfoMetadata infoQuestion = null;
        if (question != null) {
            infoQuestion = new InfoQuestionWithInfoMetadata();
            infoQuestion.copyFromDomain(question);
        }
        return infoQuestion;
    }
}