package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGuide;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.GuideRequester;
import Util.RoleType;
import Util.Specialization;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class PrepareCreateGuideTest extends TestCaseServicos
{

    public PrepareCreateGuideTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(PrepareCreateGuideTest.class);

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
    public void testPrepareCreateGuide()
    {
        System.out.println("- Test 1 : Prepare Create Guide");
        SuportePersistenteOJB sp = null;
        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoExecutionDegree infoExecutionDegree = null;
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

            infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);

            sp.confirmarTransaccao();
        }
        catch (Exception e)
        {
            fail("Error !");

        }

        Object[] args =
            {
                Specialization.MESTRADO_STRING,
                infoExecutionDegree,
                new Integer(1),
                GuideRequester.CANDIDATE_STRING,
                new Integer(123),
                null,
                null };

        InfoGuide infoGuide = null;

        try
        {
            infoGuide =
                (InfoGuide) ServiceManagerServiceFactory.executeService(
                    userView,
                    "PrepareCreateGuide",
                    args);
        }
        catch (FenixServiceException ex)
        {
            fail("Fenix Service Exception");
        }
        catch (Exception ex)
        {
            fail("Exception");
        }

        assertNotNull(infoGuide);
        assertEquals(infoGuide.getInfoGuideEntries().size(), 1);
        assertEquals(infoGuide.getVersion(), new Integer(1));
        assertEquals(infoGuide.getInfoContributor().getContributorNumber(), new Integer(123));

    }

    private UserView getUserViewToBeTested(String username, boolean withRole)
    {
        Collection roles = new ArrayList();
        InfoRole infoRole = new InfoRole();
        if (withRole)
            infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        else
            infoRole.setRoleType(RoleType.PERSON);
        roles.add(infoRole);
        UserView userView = new UserView(username, roles);
        return userView;
    }

}