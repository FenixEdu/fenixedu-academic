package ServidorAplicacao.Servicos.student;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class EnrollStudentInExamTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public EnrollStudentInExamTest(String name)
    {
        super(name);
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EnrollStudentInExam";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/student/testEnrollStudentInExamDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "13", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "nmsn", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {
        Object[] args = { userView.getUtilizador(), new Integer(1)};
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testEnrollNonExistingStudentInExam()
    {
        Object[] args = { "UsernameOfNonExistingStudent", new Integer(1)};

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println(
                "testEnrollNonExistingStudentInExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollNonExistingStudentInExam");

        } catch (NotAuthorizedException ex)
        {
            compareDataSetUsingExceptedDataSetTableColumns(
                "etc/datasets/servicos/student/"
                    + "testExpectedEnrollNonExistingStudentInExamDataSet.xml");
            ex.printStackTrace();
            System.out.println(
                "testEnrollNonExistingStudentInExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testEnrollNonExistingStudentInExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollNonExistingStudentInExam");
        }
    }

    public void testEnrollStudentInNonExistingExam()
    {
        Object[] args = { userView.getUtilizador(), new Integer(7)};

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println(
                "testEnrollStudentInNonExistingExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollStudentInNonExistingExam");

        } catch (NotAuthorizedException ex)
        {
            compareDataSetUsingExceptedDataSetTableColumns(
                "etc/datasets/servicos/student/"
                    + "testExpectedEnrollStudentInNonExistingExamDataSet.xml");
            ex.printStackTrace();
            System.out.println(
                "testEnrollStudentInNonExistingExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testEnrollStudentInNonExistingExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollStudentInNonExistingExam");
        }
    }

    public void testReEnrollStudentInExam()
    {
        Object[] args = { userView.getUtilizador(), new Integer(4)};

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println(
                "testReEnrollStudentInExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReEnrollStudentInExam");

        } catch (ExistingServiceException ex)
        {
            compareDataSetUsingExceptedDataSetTableColumns(
                "etc/datasets/servicos/student/" + "testExpectedReEnrollStudentInExamDataSet.xml");
            ex.printStackTrace();
            System.out.println(
                "testReEnrollStudentInExam was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testReEnrollStudentInExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testReEnrollStudentInExam");
        }
    }

    public void testEnrollStudentInExam()
    {
        Object[] args = { userView.getUtilizador(), new Integer(1)};
        Boolean result;

        try
        {
            result = (Boolean)ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTableColumns(
                "etc/datasets/servicos/student/" + "testExpectedEnrollStudentInExamDataSet.xml");
            System.out.println(
                "testEnrollStudentInExam was SUCCESSFULY runned by class: " + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testEnrollStudentInExam was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollStudentInExam");
        }
    }

    public void testEnrollStudentNotAttendsExecutionCourse()
    {
        Object[] args = { userView.getUtilizador(), new Integer(5)};

        try
        {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            System.out.println(
                "testEnrollStudentNotAttendsExecutionCourse was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollStudentNotAttendsExecutionCourse");

        } catch (NotAuthorizedException ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testEnrollStudentNotAttendsExecutionCourse was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testEnrollStudentNotAttendsExecutionCourse was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testEnrollStudentNotAttendsExecutionCourse");
        }
    }

}
