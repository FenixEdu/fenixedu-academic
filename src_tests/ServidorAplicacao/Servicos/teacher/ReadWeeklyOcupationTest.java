/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.SiteView;
import DataBeans.teacher.InfoWeeklyOcupation;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadWeeklyOcupationTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public ReadWeeklyOcupationTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testReadWeeklyOcupationDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadWeeklyOcupation";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "maria", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "manager", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {
        Integer careerId = new Integer(1);

        Object[] args = { careerId };
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull()
    {
        try
        {
            SiteView result = null;
            Object[] args = { new Integer(1)};

            result = (SiteView) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            InfoWeeklyOcupation infoWeeklyOcupation = (InfoWeeklyOcupation) result.getComponent();

            assertTrue(infoWeeklyOcupation.getIdInternal().equals(args[0]));
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading the WeeklyOcupation of a Teacher" + ex);
        }
    }
}
