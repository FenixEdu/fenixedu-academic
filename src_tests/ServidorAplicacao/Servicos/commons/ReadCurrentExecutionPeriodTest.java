package net.sourceforge.fenixedu.applicationTier.Servicos.commons;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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