package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 */
public class ReadSummaryTest extends SummaryBelongsExecutionCourseTest {

    /**
     * @param testName
     */
    public ReadSummaryTest(String testName) {
        super(testName);
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testReadSummaryDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadSummary";
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
        Integer summaryId = new Integer(261);

        Object[] args = { executionCourseId, summaryId };
        return args;
    }

    protected Object[] getTestSummaryUnsuccessfullArguments() {

        Integer executionCourseId = new Integer(25);
        Integer summaryId = new Integer(261);

        Object[] args = { executionCourseId, summaryId };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfull() {

        try {

            SiteView result = null;

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ISummary newSummary;
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            IPersistentSummary persistentSummary = sp.getIPersistentSummary();
            newSummary = (ISummary) persistentSummary.readByOID(Summary.class, new Integer(261));
            sp.confirmarTransaccao();

            result = (SiteView) ServiceManagerServiceFactory.executeService(userView,
                    getNameOfServiceToBeTested(), getAuthorizeArguments());

            InfoSiteSummary infoSiteSummary = (InfoSiteSummary) result.getComponent();
            InfoSummary infoSummary = infoSiteSummary.getInfoSummary();
            ISummary oldSummary = Cloner.copyInfoSummary2ISummary(infoSummary);

            assertTrue(newSummary.compareTo(oldSummary));
            // verifica se a base de dados nao foi alterada
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Reading the Summary of a Site" + ex);
        } catch (Exception ex) {
            fail("Reading the Summary of a Site" + ex);
        }
    }
}