package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.IPessoa;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.RoleType;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CreateCandidateSituationTest extends TestCaseServicos {

    public CreateCandidateSituationTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CreateCandidateSituationTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testCreateCandidateSituationNonExisting() {
        System.out.println("- Test 1 : Create Candidate Situation");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoExecutionDegree infoExecutionDegree = null;
        InfoPerson infoPerson = null;
        ICursoExecucao executionDegree = null;
        IPessoa person = null;

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            ICurso degree = persistentDegree.readBySigla("MEEC");
            assertNotNull(degree);

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan
                    .readByNameAndDegree("plano2", degree);
            assertNotNull(degreeCurricularPlan);

            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(
                    degreeCurricularPlan, executionYear);
            assertNotNull(executionDegree);

            infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);

            person = sp.getIPessoaPersistente().lerPessoaPorUsername("nmsn");

            IMasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByExecutionDegreeAndPerson(executionDegree, person);
            assertNotNull(masterDegreeCandidate);

            Iterator iterator = masterDegreeCandidate.getSituations().iterator();
            while (iterator.hasNext()) {
                ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next();
                if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                    assertEquals(candidateSituation.getSituation(), new SituationName(
                            SituationName.ADMITIDO_STRING));
                }
            }

            infoPerson = Cloner.copyIPerson2InfoPerson(person);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        Object[] args = { infoExecutionDegree, infoPerson };

        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateCandidateSituation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Eception");
        }

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IMasterDegreeCandidate masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate()
                    .readByExecutionDegreeAndPerson(executionDegree, person);
            assertNotNull(masterDegreeCandidate);

            Iterator iterator = masterDegreeCandidate.getSituations().iterator();
            while (iterator.hasNext()) {
                ICandidateSituation candidateSituation = (ICandidateSituation) iterator.next();
                if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                    assertEquals(candidateSituation.getSituation(), new SituationName(
                            SituationName.PENDENTE_STRING));
                }
            }

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

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