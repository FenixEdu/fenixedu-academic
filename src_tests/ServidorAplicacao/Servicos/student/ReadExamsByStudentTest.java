/*
 * ReadCourseByStudentTest.java JUnit based test
 * 
 * Created on February 26th, 2003, 15:33
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.student;

/**
 * @author Nuno Nunes & Joana Mota
 */
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentSiteExams;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.applicationTier.Servicos.UtilsTestCase;

public class ReadExamsByStudentTest extends ServiceNeedsAuthenticationTestCase {

    public ReadExamsByStudentTest(java.lang.String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "nmsn", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {
        Object[] args = { "13" };
        return args;
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/student/testReadExamsByStudentDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamsByStudent";
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    public void testNonExistingStudent() {

        Object[] args = { "100" };
        SiteView result = null;

        try {
            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);
            InfoStudentSiteExams infoStudentsSiteExams = (InfoStudentSiteExams) result.getComponent();
            List examsToEnroll = infoStudentsSiteExams.getExamsToEnroll();
            List infoExamStudentRoomList = infoStudentsSiteExams.getExamsEnrolledDistributions();

            assertEquals(examsToEnroll.size(), 0);
            assertEquals(infoExamStudentRoomList.size(), 0);

            System.out.println("testNonExistingStudent was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonExistingStudent was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingStudent");
        }
    }

    public void testReadExamsExistingStudent() {
        Object[] args = getAuthorizeArguments();
        SiteView result = null;

        try {
            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), args);

            InfoStudentSiteExams infoStudentsSiteExams = (InfoStudentSiteExams) result.getComponent();

            List infoExamsStudentRoomList = infoStudentsSiteExams.getExamsEnrolledDistributions();
            assertEquals(infoExamsStudentRoomList.size(), 3);

            List examsToEnroll = infoStudentsSiteExams.getExamsToEnroll();
            assertEquals(examsToEnroll.size(), 1);

            Object[] values = { new Integer(1), new Integer(2), new Integer(3) };

            UtilsTestCase.readTestList(infoExamsStudentRoomList, values, "idInternal",
                    InfoExamStudentRoom.class);

            Object[] values2 = { new Integer(1) };

            UtilsTestCase.readTestList(examsToEnroll, values2, "idInternal", InfoExam.class);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/student/testReadExamsByStudentExpectedDataSet.xml");

            System.out.println("testReadExamsExistingStudent was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadExamsExistingStudent was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadExamsExistingStudent");
        }

    }

}