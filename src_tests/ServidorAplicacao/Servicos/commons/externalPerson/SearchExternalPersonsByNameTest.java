package ServidorAplicacao.Servicos.commons.externalPerson;

import java.util.Iterator;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExternalPerson;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SearchExternalPersonsByNameTest extends ServiceTestCase {

    private String dataSetFilePath;

    //private IUserView userView = null;

    /**
     * @param testName
     */
    public SearchExternalPersonsByNameTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/commons/externalPerson/testSearchExternalPersonsByNameDataSet.xml";
    }

    protected void setUp() {
        super.setUp();
        //	userView = this.authenticateUser(getAuthenticatedUser());
    }

    /*
     * private IUserView authenticateUser(String[] args) {
     * SuportePersistenteOJB.resetInstance();
     * 
     * try { return (IUserView)
     * ServiceManagerServiceFactory.executeService(null, "Autenticacao", args); }
     * catch (Exception ex) { fail("Authenticating User!" + ex); return null; } }
     * 
     * protected String[] getAuthenticatedUser() { String[] args = { "f3667",
     * "pass", getApplication()}; return args; }
     * 
     * protected String getApplication() { return Autenticacao.INTRANET; }
     */

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "SearchExternalPersonsByName";
    }

    public void testSearchExistingExternalPersons() {
        try {
            String name = "externo";
            Object[] argsSearchExternalPersons = { name };

            List infoExternalPersons = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), argsSearchExternalPersons);
            assertNotNull(infoExternalPersons);
            assertEquals(infoExternalPersons.size(), 1);
            for (Iterator iter = infoExternalPersons.iterator(); iter.hasNext();) {
                InfoExternalPerson infoExternalPerson = (InfoExternalPerson) iter.next();
                assertTrue(infoExternalPerson.getInfoPerson().getNome().indexOf(name) > -1);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSearchExistingExternalPersons " + ex.getMessage());
        }

    }

    public void testSearchNonExistingExternalPersons() {
        try {
            String name = "Partial name to find";
            Object[] argsSearchExternalPersons = { name };

            List infoExternalPersons = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), argsSearchExternalPersons);
            assertNotNull(infoExternalPersons);
            assertTrue(infoExternalPersons.isEmpty());

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSearchNonExistingExternalPersons " + ex.getMessage());
        }
    }

}