package ServidorAplicacao.Servicos.commons;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class ReadCurrentExecutionPeriodTest extends TestCaseServicos {
    protected ISuportePersistente sp = null;

    private IPersistentExecutionPeriod executionPeriodDAO = null;

    /**
     * Constructor for SelectShiftsTest.
     * 
     * @param testName
     */
    public ReadCurrentExecutionPeriodTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadCurrentExecutionPeriodTest.class);
        return suite;
    }

    protected void setUp() {
        super.setUp();
        try {
            sp = SuportePersistenteOJB.getInstance();
            executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace(System.out);
            fail("Getting persistent support!");
        }

    }

    protected void tearDown() {
        super.tearDown();
    }

    /**
     * The test only checks if the service returns something, the real test is
     * done on Persistent layer.
     */
    public void testReadActualExecutionPeriod() {
        InfoExecutionPeriod executionPeriod = null;
        try {
            executionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(_userView,
                    "ReadCurrentExecutionPeriod", null);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Executing service ReadCurrentExecutionPeriod");
        }
        IExecutionPeriod expectedIExecutionPeriod = null;
        try {
            sp.iniciarTransaccao();
            expectedIExecutionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            fail("Reading execution period using persistent layer!");
        }
        InfoExecutionPeriod expectedInfoExecutionPeriod = (InfoExecutionPeriod) Cloner
                .get(expectedIExecutionPeriod);
        assertEquals(expectedInfoExecutionPeriod, executionPeriod);
    }
}