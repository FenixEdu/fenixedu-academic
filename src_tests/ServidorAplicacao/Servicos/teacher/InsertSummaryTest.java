package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.Calendar;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class InsertSummaryTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public InsertSummaryTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testInsertSummaryDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedInsertSummaryDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertSummary";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {

        Integer executionCourseId = new Integer(24);

        Calendar summaryDate = Calendar.getInstance();
        summaryDate.set(Calendar.DAY_OF_MONTH, 1);
        summaryDate.set(Calendar.MONTH, 2);
        summaryDate.set(Calendar.YEAR, 1999);

        Calendar summaryHour = Calendar.getInstance();
        summaryHour.set(Calendar.HOUR_OF_DAY, 12);
        summaryHour.set(Calendar.MINUTE, 0);
        summaryHour.set(Calendar.SECOND, 0);

        String title = "Titulo";
        String text = "Texto do sumario";
        Integer tipoAula = new Integer(TipoAula.DUVIDAS);

        Object[] args = { executionCourseId, summaryDate, summaryHour, tipoAula, title, text };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {
            String[] args1 = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args1);

            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args2);

            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            Criteria criteria = new Criteria();
            //criteria.addOrderBy("lastModifiedDate", false);
            Query queryCriteria = new QueryByCriteria(Summary.class, criteria);
            ((QueryByCriteria) queryCriteria).addOrderBy("lastModifiedDate", false);
            ISummary summary = (ISummary) broker.getObjectByQuery(queryCriteria);
            broker.close();

            // verificar se o sumario foi inserido na base de dados
            Calendar expectedSummaryDate = (Calendar) args2[1];
            Calendar expectedSummaryHour = (Calendar) args2[2];

            assertEquals(summary.getExecutionCourse().getIdInternal(), args2[0]);
            assertEquals(summary.getSummaryDate().get(Calendar.DAY_OF_MONTH), expectedSummaryDate
                    .get(Calendar.DAY_OF_MONTH));
            assertEquals(summary.getSummaryDate().get(Calendar.MONTH), expectedSummaryDate
                    .get(Calendar.MONTH));
            assertEquals(summary.getSummaryDate().get(Calendar.YEAR), expectedSummaryDate
                    .get(Calendar.YEAR));
            assertEquals(summary.getSummaryHour().get(Calendar.HOUR_OF_DAY), expectedSummaryHour
                    .get(Calendar.HOUR_OF_DAY));
            assertEquals(summary.getSummaryHour().get(Calendar.MINUTE), expectedSummaryHour
                    .get(Calendar.MINUTE));
            assertEquals(summary.getSummaryHour().get(Calendar.SECOND), expectedSummaryHour
                    .get(Calendar.SECOND));
            assertEquals(summary.getSummaryType().getTipo(), args2[3]);
            assertEquals(summary.getTitle(), (String) args2[4]);
            assertEquals(summary.getSummaryText(), (String) args2[5]);

            // apaga o sumario inserido para verificar se nao houve mais nenhuma
            // alteracao da bd
            Integer summaryId = summary.getIdInternal();
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            sp.getIPersistentSummary().deleteByOID(Summary.class, summaryId);
            sp.confirmarTransaccao();

            // verificar as alteracoes da bd
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Insert the Summary of a Site" + ex);
        } catch (Exception ex) {
            fail("Insert the Summary of a Site" + ex);
        }
    }
}