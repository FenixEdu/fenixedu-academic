/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class InfoTestQuestion extends InfoObject implements Comparable {

    private Integer testQuestionOrder;

    private Double testQuestionValue;

    private InfoQuestion question;

    private InfoTest test;

    private CorrectionFormula formula;

    public InfoTestQuestion() {
    }

    public int compareTo(Object arg0) {
        InfoTestQuestion infoTestQuestion = (InfoTestQuestion) arg0;
        return getTestQuestionOrder().compareTo(infoTestQuestion.getTestQuestionOrder());
    }

    public InfoQuestion getQuestion() {
        return question;
    }

    public InfoTest getTest() {
        return test;
    }

    public Double getTestQuestionValue() {
        return testQuestionValue;
    }

    public Integer getTestQuestionOrder() {
        return testQuestionOrder;
    }

    public void setQuestion(InfoQuestion question) {
        this.question = question;
    }

    public void setTest(InfoTest test) {
        this.test = test;
    }

    public void setTestQuestionValue(Double value) {
        testQuestionValue = value;
    }

    public void setTestQuestionOrder(Integer integer) {
        testQuestionOrder = integer;
    }

    public CorrectionFormula getCorrectionFormula() {
        return formula;
    }

    public void setCorrectionFormula(CorrectionFormula formula) {
        this.formula = formula;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoTestQuestion) {
            InfoTestQuestion infoTestQuestion = (InfoTestQuestion) obj;
            result = getIdInternal().equals(infoTestQuestion.getIdInternal());
            result = result
                    || ((getTestQuestionOrder().equals(infoTestQuestion.getTestQuestionOrder()))
                            && (getTestQuestionValue().equals(infoTestQuestion.getTestQuestionValue()))
                            && (getTest().equals(infoTestQuestion.getTest())) && (getQuestion()
                            .equals(infoTestQuestion.getQuestion())));
        }
        return result;
    }

    public void copyFromDomain(ITestQuestion testQuestion) {
        super.copyFromDomain(testQuestion);
        if (testQuestion != null) {
            setTestQuestionOrder(testQuestion.getTestQuestionOrder());
            setTestQuestionValue(testQuestion.getTestQuestionValue());
            setCorrectionFormula(testQuestion.getCorrectionFormula());
        }
    }

    public static InfoTestQuestion newInfoFromDomain(ITestQuestion testQuestion) {
        InfoTestQuestion infoTestQuestion = null;
        if (testQuestion != null) {
            infoTestQuestion = new InfoTestQuestion();
            infoTestQuestion.copyFromDomain(testQuestion);
        }
        return infoTestQuestion;
    }
}