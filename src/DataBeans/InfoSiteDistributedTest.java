/*
 * Created on 20/Ago/2003
 *
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */
public class InfoSiteDistributedTest extends DataTranferObject implements ISiteComponent {
    private InfoDistributedTest infoDistributedTest;

    private InfoExecutionCourse executionCourse;

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoDistributedTest(InfoDistributedTest infoDistributedTest) {
        this.infoDistributedTest = infoDistributedTest;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteDistributedTest) {
            InfoSiteDistributedTest infoSiteDistributedTest = (InfoSiteDistributedTest) obj;
            result = getExecutionCourse().equals(infoSiteDistributedTest.getExecutionCourse())
                    && getInfoDistributedTest().equals(infoSiteDistributedTest.getInfoDistributedTest());
        }
        return result;
    }
}