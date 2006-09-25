/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.util.tests.Response;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestion extends InfoObject implements Comparable {

    private InfoStudent student;

    private DistributedTest distributedTest;

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

    public DistributedTest getDistributedTest() {
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

    public void setDistributedTest(DistributedTest test) {
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

    public CorrectionFormula getCorrectionFormula() {
        return formula;
    }

    public void setCorrectionFormula(CorrectionFormula formula) {
        this.formula = formula;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoStudentTestQuestion) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) obj;
            result = getIdInternal().equals(infoStudentTestQuestion.getIdInternal());
            result = result || (getStudent().equals(infoStudentTestQuestion.getStudent()))
                    && (getDistributedTest().equals(infoStudentTestQuestion.getDistributedTest()))
                    && (getQuestion().equals(infoStudentTestQuestion.getQuestion()))
                    && (getTestQuestionOrder().equals(infoStudentTestQuestion.getTestQuestionOrder()))
                    && (getTestQuestionValue().equals(infoStudentTestQuestion.getTestQuestionValue()))
                    && (getOldResponse().equals(infoStudentTestQuestion.getOldResponse()))
                    && (getOptionShuffle().equals(infoStudentTestQuestion.getOptionShuffle()))
                    && (getTestQuestionMark().equals(infoStudentTestQuestion.getTestQuestionMark()));
        }
        return result;
    }

    public int compareTo(Object o) {
        InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) o;
        return getTestQuestionOrder().compareTo(infoStudentTestQuestion.getTestQuestionOrder());
    }

    public void copyFromDomain(StudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            setTestQuestionMark(studentTestQuestion.getTestQuestionMark());
            setTestQuestionOrder(studentTestQuestion.getTestQuestionOrder());
            setTestQuestionValue(studentTestQuestion.getTestQuestionValue());
            setOptionShuffle(studentTestQuestion.getOptionShuffle());
            setCorrectionFormula(studentTestQuestion.getCorrectionFormula());
            // if (studentTestQuestion.getResponse() != null) {
            // XMLDecoder decoder = new XMLDecoder(new
            // ByteArrayInputStream(studentTestQuestion.getResponse().getBytes()));
            // setResponse((Response) decoder.readObject());
            // decoder.close();
            // }
            setResponse(studentTestQuestion.getResponse());
        }
    }

    public static InfoStudentTestQuestion newInfoFromDomain(StudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestion infoStudentTestQuestion = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestion = new InfoStudentTestQuestion();
            infoStudentTestQuestion.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestion;
    }

}