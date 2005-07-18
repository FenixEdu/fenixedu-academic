/*
 * Created on 20/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;

/**
 * @author Susana Fernandes
 */
public class InfoSiteStudentDistributedTests extends DataTranferObject implements ISiteComponent {
    private List infoDistributedTestsToDo;

    private List infoDoneDistributedTests;

    public List getInfoDistributedTestsToDo() {
        return infoDistributedTestsToDo;
    }

    public List getInfoDoneDistributedTests() {
        return infoDoneDistributedTests;
    }

    public void setInfoDistributedTestsToDo(List list) {
        infoDistributedTestsToDo = list;
    }

    public void setInfoDoneDistributedTests(List list) {
        infoDoneDistributedTests = list;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteStudentDistributedTests) {
            InfoSiteStudentDistributedTests infoSiteStudentDistributedTests = (InfoSiteStudentDistributedTests) obj;
            result = getInfoDistributedTestsToDo().equals(infoSiteStudentDistributedTests.getInfoDistributedTestsToDo())
                    && infoSiteStudentDistributedTests.getInfoDoneDistributedTests().equals(getInfoDoneDistributedTests());

        }
        return result;
    }
}