
/*
 * CriarSalaServicosTest.java JUnit based test
 * 
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 * @author Nuno Nunes & Joana Mota
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import ServidorAplicacao.Servicos.TestCaseReadServicesIntranet;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Specialization;

public class ReadMasterDegreeCandidateTest extends TestCaseReadServicesIntranet
{

    public ReadMasterDegreeCandidateTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(ReadMasterDegreeCandidateTest.class);

        return suite;
    }

    protected void setUp()
    {
        super.setUp();
    }

    protected void tearDown()
    {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadMasterDegreeCandidate";
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 1;
    }
    protected Object getObjectToCompare()
    {
        ISuportePersistente sp = null;
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readCurrentExecutionYear();
            assertNotNull(executionYear);

            ICurso degree = sp.getICursoPersistente().readBySigla("MEEC");
            assertNotNull(degree);

            IDegreeCurricularPlan degreeCurricularPlan =
                sp.getIPersistentDegreeCurricularPlan().readByNameAndDegree("plano2", degree);
            assertNotNull(degreeCurricularPlan);

            ICursoExecucao executionDegree =
                sp.getICursoExecucaoPersistente().readByDegreeCurricularPlanAndExecutionYear(
                    degreeCurricularPlan,
                    executionYear);
            assertNotNull(executionDegree);

            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
            assertNotNull(person);

            IMasterDegreeCandidate masterDegreeCandidate =
                sp.getIPersistentMasterDegreeCandidate().readByExecutionDegreeAndPerson(
                    executionDegree,
                    person);
            infoMasterDegreeCandidate =
                Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);

            sp.confirmarTransaccao();
        }
        catch (Exception e)
        {
            fail("Error !");
        }

        return infoMasterDegreeCandidate;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        ISuportePersistente sp = null;
        InfoExecutionDegree infoExecutionDegree = null;
        ICursoExecucao executionDegree = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readCurrentExecutionYear();
            assertNotNull(executionYear);

            ICurso degree = sp.getICursoPersistente().readBySigla("MEEC");
            assertNotNull(degree);

            IDegreeCurricularPlan degreeCurricularPlan =
                sp.getIPersistentDegreeCurricularPlan().readByNameAndDegree("plano2", degree);
            assertNotNull(degreeCurricularPlan);

            executionDegree =
                sp.getICursoExecucaoPersistente().readByDegreeCurricularPlanAndExecutionYear(
                    degreeCurricularPlan,
                    executionYear);
            assertNotNull(executionDegree);

            sp.confirmarTransaccao();
        }
        catch (Exception e)
        {
            fail("Error !");
        }

        infoExecutionDegree = Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree);
        Object[] args =
            { infoExecutionDegree, new Integer(1), new Specialization(Specialization.MESTRADO)};

        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        ISuportePersistente sp = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            sp.getIPersistentMasterDegreeCandidate().deleteAll();
            sp.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia excepcao)
        {
            fail("Exception when setUp");
        }
        return null;
    }
}