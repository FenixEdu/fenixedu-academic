/*
 * Created on 28/Ago/2003
 */
package DataBeans;

import Util.tests.CorrectionFormula;
import Util.tests.Response;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestion extends InfoObject implements Comparable {

    private InfoStudent student;

    private InfoDistributedTest distributedTest;

    private InfoQuestion question;

    private Integer testQuestionOrder;

    private Double testQuestionValue;

    private Integer oldResponse;

    private String optionShuffle;

    private Double testQuestionMark;

    private Response response;

    private CorrectionFormula formula;

    public InfoStudentTestQuestion() {
    }

    public InfoDistributedTest getDistributedTest() {
        return distributedTest;
    }

    public String getOptionShuffle() {
        return optionShuffle;
    }

    public InfoQuestion getQuestion() {
        return question;
    }

    public Integer getOldResponse() {
        return oldResponse;
    }

    public InfoStudent getStudent() {
        return student;
    }

    public Integer getTestQuestionOrder() {
        return testQuestionOrder;
    }

    public Double getTestQuestionValue() {
        return testQuestionValue;
    }

    public Double getTestQuestionMark() {
        return testQuestionMark;
    }

    public Response getResponse() {
        return response;
    }

    public void setDistributedTest(InfoDistributedTest test) {
        distributedTest = test;
    }

    public void setOptionShuffle(String string) {
        optionShuffle = string;
    }

    public void setQuestion(InfoQuestion question) {
        this.question = question;
    }

    public void setOldResponse(Integer response) {
        this.oldResponse = response;
    }

    public void setStudent(InfoStudent student) {
        this.student = student;
    }

    public void setTestQuestionOrder(Integer integer) {
        testQuestionOrder = integer;
    }

    public void setTestQuestionValue(Double value) {
        testQuestionValue = value;
    }

    public void setTestQuestionMark(Double double1) {
        testQuestionMark = double1;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoStudentTestQuestion) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) obj;
            result = getIdInternal().equals(
                    infoStudentTestQuestion.getIdInternal());
            result = result
                    || (getStudent().equals(infoStudentTestQuestion
                            .getStudent()))
                    && (getDistributedTest().equals(infoStudentTestQuestion
                            .getDistributedTest()))
                    && (getQuestion().equals(infoStudentTestQuestion
                            .getQuestion()))
                    && (getTestQuestionOrder().equals(infoStudentTestQuestion
                            .getTestQuestionOrder()))
                    && (getTestQuestionValue().equals(infoStudentTestQuestion
                            .getTestQuestionValue()))
                    && (getOldResponse().equals(infoStudentTestQuestion
                            .getOldResponse()))
                    && (getOptionShuffle().equals(infoStudentTestQuestion
                            .getOptionShuffle()))
                    && (getTestQuestionMark().equals(infoStudentTestQuestion
                            .getTestQuestionMark()));
        }
        return result;
    }

    public int compareTo(Object o) {
        InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) o;
        return getTestQuestionOrder().compareTo(
                infoStudentTestQuestion.getTestQuestionOrder());
    }

    public CorrectionFormula getCorrectionFormula() {
        return formula;
    }

    public void setCorrectionFormula(CorrectionFormula formula) {
        this.formula = formula;
    }
}