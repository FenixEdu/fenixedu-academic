/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;

/**
 * @author Susana Fernandes
 */
public class InfoSiteDistributedTestAdvisory extends DataTranferObject {

    private InfoDistributedTest infoDistributedTest;

    private InfoAdvisory infoAdvisory;

    private List<InfoStudent> infoStudentList;

    public InfoAdvisory getInfoAdvisory() {
        return infoAdvisory;
    }

    public void setInfoAdvisory(InfoAdvisory infoAdvisory) {
        this.infoAdvisory = infoAdvisory;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public void setInfoDistributedTest(InfoDistributedTest infoDistributedTest) {
        this.infoDistributedTest = infoDistributedTest;
    }

    public List<InfoStudent> getInfoStudentList() {
        return infoStudentList;
    }

    public void setInfoStudentList(List<InfoStudent> infoStudentList) {
        this.infoStudentList = infoStudentList;
    }

}