/*
 * Created on 7/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoCourseReport;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCourseInformationTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public EditCourseInformationTest(String name)
    {
        super(name);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/gesdis/testEditCourseInformationDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditCourseInformation";
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
        Integer courseReportId = new Integer(1);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer(1));

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(1));
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setIdInternal(new Integer(24));
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        InfoCourseReport infoCourseReport = new InfoCourseReport();
        infoCourseReport.setIdInternal(courseReportId);
        infoCourseReport.setReport("novo relatorio da disciplina");
        infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

        Object[] args = { courseReportId, infoCourseReport };
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfullWithCourseReport()
    {
        try
        {
            Boolean result = null;

            result =
                (Boolean) gestor.executar(
                    userView,
                    getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            assertTrue(result.booleanValue());
            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/gesdis/testExpectedEditCourseInformationWithCourseReportDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing a Course Information with a CourseReport " + ex);
        }
    }

    public void testSuccessfullWithoutCourseReport()
    {
        try
        {
            Boolean result = null;

            String[] args = { "maria", "pass", getApplication()};
            IUserView userView = authenticateUser(args);

            InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.setIdInternal(new Integer(1));

            InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.setIdInternal(new Integer(1));
            infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

            InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(new Integer(25));
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            InfoCourseReport infoCourseReport = new InfoCourseReport();
            infoCourseReport.setReport("relatorio da disciplina");
            infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

            Object[] serviceArgs = { null, infoCourseReport };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), serviceArgs);

            assertTrue(result.booleanValue());
            // verifica as alteracoes da base de dados
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/gesdis/testExpectedEditCourseInformationWithoutCourseReportDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing a Course Information without a CourseReport " + ex);
        }
    }
}
