/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.teacher.InfoCareer;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareerTest extends ServiceNeedsAuthenticationTestCase
{

	/**
	 * @param testName
	 */
	public ReadCareerTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testReadCareerDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadCareer";
	}

	protected String[] getAuthenticatedAndAuthorizedUser()
	{

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser()
	{

		String[] args = { "julia", "pass", getApplication()};
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

	public void testReadProfessionalCareer()
	{
		try
		{
            InfoCareer result = null;
            Object[] args = { new Integer(1) };

			result =
				(InfoCareer) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);
			
			assertTrue(result.getIdInternal().equals(args[0]));
			// verifica se a base de dados nao foi alterada
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		} catch (Exception ex)
		{
			fail("Reading the ProfessionalCareer of a Teacher" + ex);
		}
	}
    
    public void testReadTeachingCareer()
    {
        try
        {
            InfoCareer result = null;
            Object[] args = { new Integer(2) };

            result =
                (InfoCareer) gestor.executar(
                    userView,
                    getNameOfServiceToBeTested(),
                    args);
            
            assertTrue(result.getIdInternal().equals(args[0]));
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading the TeachingCareer of a Teacher" + ex);
        }
    }

}
