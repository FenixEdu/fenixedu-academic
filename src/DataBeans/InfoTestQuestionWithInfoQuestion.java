/*
 * Created on 31/Jul/2003
 */
package DataBeans;

import Dominio.ITestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoTestQuestionWithInfoQuestion extends InfoTestQuestion {

    public void copyFromDomain(ITestQuestion testQuestion) {
        super.copyFromDomain(testQuestion);
        if (testQuestion != null) {
            setQuestion(InfoQuestion.newInfoFromDomain(testQuestion.getQuestion()));
        }
    }

    public static InfoTestQuestion newInfoFromDomain(ITestQuestion testQuestion) {
        InfoTestQuestionWithInfoQuestion infoTestQuestion = null;
        if (testQuestion != null) {
            infoTestQuestion = new InfoTestQuestionWithInfoQuestion();
            infoTestQuestion.copyFromDomain(testQuestion);
        }
        return infoTestQuestion;
    }
}