/*
 * Created on 29/Jul/2003
 */
package Dominio;

import Util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public interface ITestQuestion extends IDomainObject {

    public abstract Integer getKeyQuestion();

    public abstract Integer getKeyTest();

    public abstract IQuestion getQuestion();

    public abstract Double getTestQuestionValue();

    public abstract ITest getTest();

    public abstract Integer getTestQuestionOrder();

    public abstract CorrectionFormula getCorrectionFormula();

    public abstract void setKeyQuestion(Integer integer);

    public abstract void setKeyTest(Integer integer);

    public abstract void setQuestion(IQuestion question);

    public abstract void setTest(ITest test);

    public abstract void setTestQuestionOrder(Integer integer);

    public abstract void setTestQuestionValue(Double value);

    public abstract void setCorrectionFormula(CorrectionFormula formula);

}