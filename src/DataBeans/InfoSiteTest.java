/*
 * Created on 1/Ago/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */
public class InfoSiteTest extends DataTranferObject implements ISiteComponent {
    private InfoTest infoTest;

    private List infoTestQuestions;

    private InfoExecutionCourse executionCourse;

    public InfoSiteTest() {
    }

    public List getInfoTestQuestions() {
        return infoTestQuestions;
    }

    public void setInfoTestQuestions(List list) {
        infoTestQuestions = list;
    }

    public InfoTest getInfoTest() {
        return infoTest;
    }

    public void setInfoTest(InfoTest test) {
        infoTest = test;
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteTest) {
            InfoSiteTest infoSiteTest = (InfoSiteTest) obj;
            result = getExecutionCourse().equals(infoSiteTest.getExecutionCourse())
                    && getInfoTest().equals(infoSiteTest.getInfoTest())
                    && getInfoTestQuestions().containsAll(infoSiteTest.getInfoTestQuestions())
                    && infoSiteTest.getInfoTestQuestions().containsAll(getInfoTestQuestions());
        }
        return result;
    }
}