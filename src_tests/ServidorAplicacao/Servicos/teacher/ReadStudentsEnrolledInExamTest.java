package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExam;
import DataBeans.InfoExamStudentRoom;
import DataBeans.InfoSiteTeacherStudentsEnrolledList;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorAplicacao.Servicos.UtilsTestCase;

/**
 * @author João Mota
 *  
 */
public class ReadStudentsEnrolledInExamTest extends ServiceNeedsAuthenticationTestCase {
    /**
     * @param testName
     */
    public ReadStudentsEnrolledInExamTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadStudentsEnrolledInExam";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "3", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        Object[] args = { new Integer(27), new Integer(1) };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadStudentsEnrolledInExamDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "13", "pass", getApplication() };
        return args;
    }

    public void testReadExecutionCourseExam() {
        TeacherAdministrationSiteView result = null;

        try {

            result = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

            InfoSiteTeacherStudentsEnrolledList infoSiteTeacherStudentsEnrolledList = (InfoSiteTeacherStudentsEnrolledList) result
                    .getComponent();
            InfoExam infoExam = infoSiteTeacherStudentsEnrolledList.getInfoExam();
            List infoExamsStudentRoomList = infoSiteTeacherStudentsEnrolledList
                    .getInfoExamStudentRoomList();
            List infoStudents = infoSiteTeacherStudentsEnrolledList.getInfoStudents();

            assertEquals(new Integer(1), infoExam.getIdInternal());
            assertEquals(4, infoExamsStudentRoomList.size());

            Object[] args1 = { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };

            UtilsTestCase.readTestList(infoExamsStudentRoomList, args1, "idInternal",
                    InfoExamStudentRoom.class);

            assertEquals(4, infoStudents.size());

            Object[] args2 = { new Integer(600), new Integer(700), new Integer(800), new Integer(900) };

            UtilsTestCase.readTestList(infoStudents, args2, "number", InfoStudent.class);

            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testReadStudentsEnrolledInExamExpectedDataSet.xml");

            System.out.println("testReadExecutionCourseExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testReadExecutionCourseExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReadExecutionCourseExam");
        }
    }

    public void testNonExistingExecutionCourse() {
        Object[] args = { new Integer(100), new Integer(1) };

        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingExecutionCourse was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingExecutionCourse");

        } catch (NotAuthorizedException ex) {
            ex.printStackTrace();
            System.out.println("testNonAuthenticatedUser was SUCCESSFULY runned by service: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonAuthenticatedUser was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }

    public void testNonExistingExam() {
        Object[] args = { new Integer(27), new Integer(100) };

        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            System.out.println("testNonExistingExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testNonExistingExam");
        } catch (FenixServiceException ex) {
            System.out.println("testReadNonExistingSection was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("testNonAuthenticatedUser was UNSUCCESSFULY runned by service: "
                    + this.getClass().getName());
            fail("Unable to run service: " + this.getClass().getName());
        }
    }
}