/*
 * Created on Nov 13, 2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.SiteView;
import DataBeans.teacher.InfoSiteCareers;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCareersTest extends ServiceNeedsAuthenticationTestCase
{
    /**
	 * @param testName
	 */
    public ReadCareersTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testReadCareersDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadCareers";
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
        Object[] args = { CareerType.PROFESSIONAL, userView.getUtilizador()};
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testReadAllCareersTeacherWithCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = null;

            Object[] args = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertEquals(infoCareers.size(), 2);
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading AllCareers of a Teacher with careers" + ex);
        }
    }

    public void testReadAllCareersTeacherWithoutCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = null;

            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            Object[] serviceArgs = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArgs);
            System.out.println("passei o servico");

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertEquals(infoCareers.size(), 0);

            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading the AllCareers of a Teacher without careers" + ex);
        }
    }

    public void testReadAllProfessionalCareersTeacherWithCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = CareerType.PROFESSIONAL;

            Object[] args = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertEquals(infoCareers.size(), 1);
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading AllProfessionalCareers of a Teacher with careers" + ex);
        }
    }

    public void testReadAllProfessionalCareersTeacherWithoutCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = CareerType.PROFESSIONAL;

            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            Object[] serviceArgs = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArgs);
            System.out.println("passei o servico");

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertEquals(infoCareers.size(), 0);

            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading the AllProfessionalCareers of a Teacher without careers" + ex);
        }
    }

    public void testReadAllTeachingCareersTeacherWithCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = CareerType.TEACHING;

            Object[] args = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertEquals(infoCareers.size(), 1);
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading AllTeachingCareers of a Teacher with careers" + ex);
        }
    }

    public void testReadAllTeachingCareersTeacherWithoutCareers()
    {
        try
        {
            SiteView result = null;
            CareerType careerType = CareerType.TEACHING;

            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            Object[] serviceArgs = { careerType, userView.getUtilizador()};

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArgs);
            System.out.println("passei o servico");

            InfoSiteCareers infoSiteCareers = (InfoSiteCareers) result.getComponent();
            assertEquals(infoSiteCareers.getCareerType(), careerType);

            List infoCareers = infoSiteCareers.getInfoCareers();
            assertTrue(infoCareers.isEmpty());

            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex)
        {
            fail("Reading the AllTeachingCareers of a Teacher without careers" + ex);
        }
    }
}
