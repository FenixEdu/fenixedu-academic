/*
 * Created on 7/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformationTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public EditTeacherInformationTest(String name)
    {
        super(name);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditTeacherInformationDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedEditTeacherInformationDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditTeacherInformation";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {

        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {

        String[] args = { "jorge", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {

        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(1));

        InfoServiceProviderRegime infoServiceProviderRegime = new InfoServiceProviderRegime();
        infoServiceProviderRegime.setIdInternal(new Integer(1));
        infoServiceProviderRegime.setProviderRegimeType(ProviderRegimeType.getEnum(ProviderRegimeType.CUMULATIVE_TYPE));
        infoServiceProviderRegime.setInfoTeacher(infoTeacher);

        InfoWeeklyOcupation infoWeeklyOcupation = new InfoWeeklyOcupation();
        infoWeeklyOcupation.setIdInternal(new Integer(1));
        infoWeeklyOcupation.setResearch(new Integer(10));
        infoWeeklyOcupation.setManagement(new Integer(4));
        infoWeeklyOcupation.setOther(new Integer(5));
        infoWeeklyOcupation.setInfoTeacher(infoTeacher);
        
        Object[] args = { infoServiceProviderRegime, infoWeeklyOcupation };
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
            Boolean result = null;

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            result =
                (Boolean) gestor.executar(
                    userView,
                    getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            assertTrue(result.booleanValue());
            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Editing a teacher Information " + ex);
        }
    }
}
