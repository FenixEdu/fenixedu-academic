/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servicos.MasterDegree.Candidate;

import junit.framework.TestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;

public class TestCaseServicosCandidato extends TestCase {
    protected ISuportePersistente suportePersistente = null;

    protected IPersistentDepartment persistentDepartment = null;

    protected ICursoPersistente persistentDegree = null;

    protected IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;

    protected dbaccess db = null;

    public TestCaseServicosCandidato(String testName) {
        super(testName);
    }

    /*
     * public static void main(java.lang.String[] args) {
     * junit.textui.TestRunner.run(suite()); }
     * 
     * public static Test suite() { TestSuite suite = new
     * TestSuite(TestCaseOJB.class);
     * 
     * return suite; }
     */

    protected void setUp() {
        ligarSuportePersistente();

        try {
            db = new dbaccess();
            db.openConnection();
            db.backUpDataBaseContents("etc/testBackup.xml");
            db.loadDataBase("etc/testDataSet.xml");
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println("Setup failed :o(" + ex);
        }
    }

    protected void tearDown() {
        try {
            db = new dbaccess();
            db.openConnection();
            db.loadDataBase("etc/testBackup.xml");
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println("Setup failed :o(" + ex);
        }
    }

    protected void ligarSuportePersistente() {
        try {
            suportePersistente = SuportePersistenteOJB.getInstance();
        } catch (ExcepcaoPersistencia excepcao) {
            fail("Exception when opening database");
        }
        persistentDegree = suportePersistente.getICursoPersistente();
        persistentDepartment = suportePersistente.getIDepartamentoPersistente();
        persistentMasterDegreeCandidate = suportePersistente.getIPersistentMasterDegreeCandidate();
    }

    protected void cleanData() {
    }
}