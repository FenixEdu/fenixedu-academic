/*
 * Created on 28/Jul/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */
public class InfoSiteTests extends DataTranferObject implements ISiteComponent {
    private List infoTests;

    private InfoExecutionCourse executionCourse;

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public List getInfoTests() {
        return infoTests;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoTests(List list) {
        infoTests = list;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteTests) {
            InfoSiteTests infoSiteTests = (InfoSiteTests) obj;
            result = getExecutionCourse().equals(infoSiteTests.getExecutionCourse())
                    && getInfoTests().containsAll(infoSiteTests.getInfoTests())
                    && infoSiteTests.getInfoTests().containsAll(getInfoTests());
        }
        return result;
    }
}