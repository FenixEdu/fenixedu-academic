/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoWeeklyOcupation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditWeeklyOcupationTest extends ServiceNeedsAuthenticationTestCase
{

    public EditWeeklyOcupationTest(String testName)
    {
        super(testName);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "EditWeeklyOcupation";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditWeeklyOcupationDataSet.xml";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "maria", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "manager", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
	 */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        Integer weeklyOcupationId = new Integer(1);

        InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
        infoWeeklyOcupation.setIdInternal(weeklyOcupationId);
        infoWeeklyOcupation.setOther(new Integer(20));
        infoWeeklyOcupation.setResearch(new Integer(17));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(3));

        infoWeeklyOcupation.setInfoTeacher(infoTeacher);

        Object[] args = { weeklyOcupationId, infoWeeklyOcupation };
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testCreateNewWeeklyOcupation()
    {
        try
        {
            Boolean result = null;

            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setOther(new Integer(1));
            infoWeeklyOcupation.setResearch(new Integer(3));

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoWeeklyOcupation.setInfoTeacher(infoTeacher);

            Object[] args = { null, infoWeeklyOcupation };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateWeeklyOcupationDataSet.xml");
        } catch (Exception ex)
        {
            fail("Creating a new weekly ocupation " + ex);
        }
    }

    public void testCreateExistingWeeklyOcupation()
    {
        try
        {
            Boolean result = null;
            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setOther(new Integer(5));
            infoWeeklyOcupation.setResearch(new Integer(13));

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(3));

            infoWeeklyOcupation.setInfoTeacher(infoTeacher);

            Object[] serviceArgs = { null, infoWeeklyOcupation };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), serviceArgs);

            assertEquals(result, new Boolean(false));
            compareDataSetUsingExceptedDataSetTablesAndColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Creating an existing weekly ocupation " + ex);
        }
    }

    public void testEditExistingWeeklyOcupation()
    {
        try
        {
            Boolean result = null;
            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            Integer weeklyOcupationId = new Integer(1);

            InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
            infoWeeklyOcupation.setIdInternal(weeklyOcupationId);
            infoWeeklyOcupation.setOther(new Integer(5));
            infoWeeklyOcupation.setResearch(new Integer(13));

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(3));

            infoWeeklyOcupation.setInfoTeacher(infoTeacher);

            Object[] serviceArgs = { weeklyOcupationId, infoWeeklyOcupation };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), serviceArgs);

            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditWeeklyOcupationDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing a weekly ocupation " + ex);
        }
    }
}
