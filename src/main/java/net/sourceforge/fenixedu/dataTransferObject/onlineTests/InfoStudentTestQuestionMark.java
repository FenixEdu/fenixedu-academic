/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestQuestionMark extends InfoObject {
    private String studentName;

    private Integer studentNumber;

    private String studentExternalId;

    private String studentDegree;

    private List<Double> testQuestionMarks;

    private Double maximumMark;

    public InfoStudentTestQuestionMark() {
    }

    public Double getMaximumMark() {
        if (maximumMark == null) {
            maximumMark = new Double(0);
        }
        return maximumMark;
    }

    public void setMaximumMark(Double maximumMark) {
        this.maximumMark = maximumMark;
    }

    public String getStudentExternalId() {
        return studentExternalId;
    }

    public void setStudentExternalId(String studentExternalId) {
        this.studentExternalId = studentExternalId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentDegree() {
        return studentDegree;
    }

    public void setStudentDegree(String studentDegree) {
        this.studentDegree = studentDegree;
    }

    public List<Double> getTestQuestionMarks() {
        if (testQuestionMarks == null) {
            testQuestionMarks = new ArrayList<Double>();
        }
        return testQuestionMarks;
    }

    public void setTestQuestionMarks(List<Double> testQuestionMarks) {
        this.testQuestionMarks = testQuestionMarks;
    }

    public void copyFromDomain(StudentTestQuestion studentTestQuestion) {
        super.copyFromDomain(studentTestQuestion);
        if (studentTestQuestion != null) {
            if (studentTestQuestion.getStudent() != null) {
                setStudentExternalId(studentTestQuestion.getStudent().getExternalId());
                setStudentNumber(studentTestQuestion.getStudent().getNumber());
                setStudentDegree(studentTestQuestion.getStudent().getDegree() != null ? studentTestQuestion.getStudent()
                        .getDegree().getSigla() : "-");
                if (studentTestQuestion.getStudent().getPerson() != null) {
                    setStudentName(studentTestQuestion.getStudent().getPerson().getName());
                }
            }
            addTestQuestionMark(studentTestQuestion.getTestQuestionMark());
            setMaximumMark(studentTestQuestion.getTestQuestionValue());
        }
    }

    public static InfoStudentTestQuestionMark newInfoFromDomain(StudentTestQuestion studentTestQuestion) {
        InfoStudentTestQuestionMark infoStudentTestQuestionMark = null;
        if (studentTestQuestion != null) {
            infoStudentTestQuestionMark = new InfoStudentTestQuestionMark();
            infoStudentTestQuestionMark.copyFromDomain(studentTestQuestion);
        }
        return infoStudentTestQuestionMark;
    }

    public void addToMaximumMark(Double testQuestionValue) {
        setMaximumMark(new Double(getMaximumMark().doubleValue() + testQuestionValue.doubleValue()));
    }

    public void addTestQuestionMark(Double testQuestionMark) {
        getTestQuestionMarks().add(testQuestionMark);
    }

    public void addTestQuestionMark(int i, Double testQuestionMark) {
        getTestQuestionMarks().set(i, new Double(getTestQuestionMarks().get(i).doubleValue() + testQuestionMark.doubleValue()));

    }

}