package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.GuideRequester;
import net.sourceforge.fenixedu.util.RoleType;
import net.sourceforge.fenixedu.util.Specialization;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class PrepareCreateGuideTest extends TestCaseServicos {

    public PrepareCreateGuideTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PrepareCreateGuideTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testPrepareCreateGuide() {
        System.out.println("- Test 1 : Prepare Create Guide");
        SuportePersistenteOJB sp = null;
        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoExecutionDegree infoExecutionDegree = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readCurrentExecutionYear();
            assertNotNull(executionYear);

            ICurso degree = sp.getICursoPersistente().readBySigla("MEEC");
            assertNotNull(degree);

            IDegreeCurricularPlan degreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan()
                    .readByNameAndDegree("plano2", degree);
            assertNotNull(degreeCurricularPlan);

            ICursoExecucao executionDegree = sp.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
            assertNotNull(executionDegree);

            infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);

            sp.confirmarTransaccao();
        } catch (Exception e) {
            fail("Error !");

        }

        Object[] args = { Specialization.MESTRADO_STRING, infoExecutionDegree, new Integer(1),
                GuideRequester.CANDIDATE_STRING, new Integer(123), null, null };

        InfoGuide infoGuide = null;

        try {
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareCreateGuide", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

        assertNotNull(infoGuide);
        assertEquals(infoGuide.getInfoGuideEntries().size(), 1);
        assertEquals(infoGuide.getVersion(), new Integer(1));
        assertEquals(infoGuide.getInfoContributor().getContributorNumber(), new Integer(123));

    }

    private UserView getUserViewToBeTested(String username, boolean withRole) {
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