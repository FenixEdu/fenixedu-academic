/*
 * Created on Oct 24, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSiteStudentsTestMarksStatistics extends DataTranferObject implements ISiteComponent {

    private List correctAnswersPercentage;

    private List partiallyCorrectAnswersPercentage;

    private List wrongAnswersPercentage;

    private List notAnsweredPercentage;

    private List answeredPercentage;

    private InfoDistributedTest infoDistributedTest;

    private InfoExecutionCourse executionCourse;

    public InfoSiteStudentsTestMarksStatistics() {
    }

    public List getCorrectAnswersPercentage() {
        return correctAnswersPercentage;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public List getNotAnsweredPercentage() {
        return notAnsweredPercentage;
    }

    public List getWrongAnswersPercentage() {
        return wrongAnswersPercentage;
    }

    public void setCorrectAnswersPercentage(List list) {
        correctAnswersPercentage = list;
    }

    public void setInfoDistributedTest(InfoDistributedTest infoDistributedTest) {
        this.infoDistributedTest = infoDistributedTest;
    }

    public void setNotAnsweredPercentage(List list) {
        notAnsweredPercentage = list;
    }

    public void setWrongAnswersPercentage(List list) {
        wrongAnswersPercentage = list;
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public List getPartiallyCorrectAnswersPercentage() {
        return partiallyCorrectAnswersPercentage;
    }

    public void setPartiallyCorrectAnswersPercentage(List partiallyCorrectAnswersPercentage) {
        this.partiallyCorrectAnswersPercentage = partiallyCorrectAnswersPercentage;
    }

    public List getAnsweredPercentage() {
        return answeredPercentage;
    }

    public void setAnsweredPercentage(List answeredPercentage) {
        this.answeredPercentage = answeredPercentage;
    }
}