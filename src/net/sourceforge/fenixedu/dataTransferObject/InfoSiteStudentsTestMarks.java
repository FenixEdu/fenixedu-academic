/*
 * Created on Oct 24, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author Susana Fernandes
 *  
 */
public class InfoSiteStudentsTestMarks extends DataTranferObject implements ISiteComponent {

    private List infoStudentTestQuestionList;

    private Double maximumMark;

    private InfoDistributedTest infoDistributedTest;

    private InfoExecutionCourse executionCourse;

    public InfoSiteStudentsTestMarks() {
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public List getInfoStudentTestQuestionList() {
        return infoStudentTestQuestionList;
    }

    public Double getMaximumMark() {
        return maximumMark;
    }

    public void setMaximumMark(Double maximumMark) {
        this.maximumMark = maximumMark;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoDistributedTest(InfoDistributedTest test) {
        infoDistributedTest = test;
    }

    public void setInfoStudentTestQuestionList(List list) {
        infoStudentTestQuestionList = list;
    }

}