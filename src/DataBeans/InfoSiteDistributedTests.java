/*
 * Created on 20/Ago/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */
public class InfoSiteDistributedTests extends DataTranferObject implements ISiteComponent {
    private List infoDistributedTests;

    private InfoExecutionCourse executionCourse;

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public List getInfoDistributedTests() {
        return infoDistributedTests;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoDistributedTests(List list) {
        infoDistributedTests = list;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteDistributedTests) {
            InfoSiteDistributedTests infoSiteDistributedTests = (InfoSiteDistributedTests) obj;
            if (getExecutionCourse() != null) {
                result = getExecutionCourse().equals(infoSiteDistributedTests.getExecutionCourse())
                        && getInfoDistributedTests().containsAll(
                                infoSiteDistributedTests.getInfoDistributedTests())
                        && infoSiteDistributedTests.getInfoDistributedTests().containsAll(
                                getInfoDistributedTests());

            } else {
                result = getInfoDistributedTests().containsAll(
                        infoSiteDistributedTests.getInfoDistributedTests())
                        && infoSiteDistributedTests.getInfoDistributedTests().containsAll(
                                getInfoDistributedTests());
            }
        }
        return result;
    }
}