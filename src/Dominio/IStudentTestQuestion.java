/*
 * Created on 27/Ago/2003
 */
package Dominio;

import Util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public interface IStudentTestQuestion extends IDomainObject {

    public abstract IDistributedTest getDistributedTest();

    public abstract Integer getKeyDistributedTest();

    public abstract Integer getKeyQuestion();

    public abstract Integer getKeyStudent();

    public abstract String getOptionShuffle();

    public abstract IQuestion getQuestion();

    public abstract String getResponse();

    public abstract IStudent getStudent();

    public abstract Integer getTestQuestionOrder();

    public abstract Double getTestQuestionValue();

    public abstract Double getTestQuestionMark();

    public abstract CorrectionFormula getCorrectionFormula();

    public abstract void setDistributedTest(IDistributedTest test);

    public abstract void setKeyDistributedTest(Integer integer);

    public abstract void setKeyQuestion(Integer integer);

    public abstract void setKeyStudent(Integer integer);

    public abstract void setOptionShuffle(String string);

    public abstract void setQuestion(IQuestion question);

    public abstract void setResponse(String response);

    public abstract void setStudent(IStudent student);

    public abstract void setTestQuestionOrder(Integer integer);

    public abstract void setTestQuestionValue(Double double1);

    public abstract void setTestQuestionMark(Double double1);

    public abstract Integer getOldResponse();

    public abstract void setOldResponse(Integer oldResponse);

    public abstract void setCorrectionFormula(CorrectionFormula formula);
}