package ServidorAplicacao.Servicos.teacher;

import java.util.Calendar;

import framework.factory.ServiceManagerServiceFactory;

import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class EditSummaryTest extends SummaryBelongsExecutionCourseTest
{

    /**
     * @param testName
     */
    public EditSummaryTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditSummaryDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedEditSummaryDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditSummary";
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

        Integer executionCourseId = new Integer(24);
        Integer summaryId = new Integer(261);

        ISummary summary = null;

        try
        {

            summary = new Summary(summaryId);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            summary = (ISummary) persistentSummary.readByOId(summary, false);
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex)
        {
            fail("Editing the summary: failed reading the summary to edit " + ex);
        }

        Calendar summaryDate = Calendar.getInstance();
        summaryDate.set(Calendar.DAY_OF_MONTH, 1);
        summaryDate.set(Calendar.MONTH, 2);
        summaryDate.set(Calendar.YEAR, 1999);

        Calendar summaryHour = Calendar.getInstance();
        summaryHour.set(Calendar.HOUR_OF_DAY, 12);
        summaryHour.set(Calendar.MINUTE, 0);
        summaryHour.set(Calendar.SECOND, 0);

        summary.setTitle("Novo Titulo");
        summary.setSummaryText("Novo texto do sumario");
        Integer summaryType = new Integer(TipoAula.DUVIDAS);

        Object[] args =
            {
                executionCourseId,
                summaryId,
                summaryDate,
                summaryHour,
                summaryType,
                summary.getTitle(),
                summary.getSummaryText()};
        return args;
    }

    protected Object[] getTestSummaryUnsuccessfullArguments()
    {

        Integer executionCourseId = new Integer(25);
        Integer summaryId = new Integer(281);

        ISummary summary = null;

        try
        {

            summary = new Summary(summaryId);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            summary = (ISummary) persistentSummary.readByOId(summary, false);
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex)
        {
            fail("Editing the summary: failed reading the summary to edit " + ex);
        }

        summary.setTitle("Novo Titulo");
        summary.setSummaryText("Novo texto do sumario");
        Integer summaryType = new Integer(TipoAula.DUVIDAS);

        Object[] args =
            {
                executionCourseId,
                summaryId,
                summary.getSummaryDate(),
                summary.getSummaryHour(),
                summaryType,
                summary.getTitle(),
                summary.getSummaryText()};
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

            String[] args1 = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args1);

            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args2);

            Integer summaryId = (Integer) args2[1];
            ISummary summary = (ISummary) new Summary(summaryId);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            summary = (ISummary) persistentSummary.readByOId(summary, false);
            sp.confirmarTransaccao();

            // verificar se o sumario foi alterado na base de dados
            Calendar expectedSummaryDate = (Calendar) args2[2];
            Calendar expectedSummaryHour = (Calendar) args2[3];
            assertEquals(
                summary.getSummaryDate().get(Calendar.DAY_OF_MONTH),
                expectedSummaryDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(
                summary.getSummaryDate().get(Calendar.MONTH),
                expectedSummaryDate.get(Calendar.MONTH));
            assertEquals(
                summary.getSummaryDate().get(Calendar.YEAR),
                expectedSummaryDate.get(Calendar.YEAR));
            assertEquals(
                summary.getSummaryHour().get(Calendar.HOUR_OF_DAY),
                expectedSummaryHour.get(Calendar.HOUR_OF_DAY));
            assertEquals(
                summary.getSummaryHour().get(Calendar.MINUTE),
                expectedSummaryHour.get(Calendar.MINUTE));
            assertEquals(
                summary.getSummaryHour().get(Calendar.SECOND),
                expectedSummaryHour.get(Calendar.SECOND));
            assertEquals(summary.getSummaryType().getTipo(), args2[4]);
            assertEquals(summary.getTitle(), (String) args2[5]);
            assertEquals(summary.getSummaryText(), (String) args2[6]);

            // apaga o sumario inserido para verificar se nao houve mais nenhuma alteracao da bd
            sp.iniciarTransaccao();
            sp.getIPersistentSummary().deleteByOID(Summary.class, summaryId);
            sp.confirmarTransaccao();

            // verificar as alteracoes da bd
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        } catch (FenixServiceException ex)
        {
            fail("Editing the Summary of a Site" + ex);
        } catch (Exception ex)
        {
            fail("Editing the Summary of a Site" + ex);
        }
    }
}