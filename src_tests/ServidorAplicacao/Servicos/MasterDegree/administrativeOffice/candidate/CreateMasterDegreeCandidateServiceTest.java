/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

public class CreateMasterDegreeCandidateServiceTest extends TestCaseServicos {

    public CreateMasterDegreeCandidateServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CreateMasterDegreeCandidateServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testCreateMasterDegreeCandidateNonExisting() {
        System.out.println("- Test 1 : Create Master Degree Candidate");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setNome("Nuno & Joana");
        infoPerson.setNumeroDocumentoIdentificacao("123");
        infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO_STRING));

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
        infoMasterDegreeCandidate.setSpecialization(new Specialization(Specialization.MESTRADO_STRING));
        infoMasterDegreeCandidate.setInfoPerson(infoPerson);

        InfoDegree infoDegree = new InfoDegree();
        infoDegree.setNome("Mestrado em Engenharia Electrotecnica e de Computadores");
        infoDegree.setSigla("MEEC");

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear("2002/2003");

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        infoMasterDegreeCandidate.setInfoExecutionDegree(infoExecutionDegree);

        Object[] args = { infoMasterDegreeCandidate };

        InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = null;

        try {
            newInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "CreateMasterDegreeCandidate", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(newInfoMasterDegreeCandidate);
        assertEquals(newInfoMasterDegreeCandidate.getInfoPerson().getNome(), infoMasterDegreeCandidate
                .getInfoPerson().getNome());
        assertEquals(newInfoMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao(),
                infoMasterDegreeCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());
        assertEquals(newInfoMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao(),
                infoMasterDegreeCandidate.getInfoPerson().getTipoDocumentoIdentificacao());
        assertEquals(newInfoMasterDegreeCandidate.getSpecialization(), infoMasterDegreeCandidate
                .getSpecialization());

    }

    public void testCreateMasterDegreeCandidateExisting() {
        System.out.println("- Test 2 : Create Existing Master Degree Candidate");

        UserView userView = this.getUserViewToBeTested("jccm", true);

        InfoPerson infoPerson = new InfoPerson();
        infoPerson.setNome("Joana");
        infoPerson.setNumeroDocumentoIdentificacao("55555555");
        infoPerson.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
                TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
        infoMasterDegreeCandidate.setSpecialization(new Specialization(Specialization.MESTRADO_STRING));
        infoMasterDegreeCandidate.setInfoPerson(infoPerson);

        Object[] args = { infoMasterDegreeCandidate,
                "Mestrado em Engenharia Electrotecnica e de Computadores", userView };

        InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = null;

        try {
            newInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "CreateMasterDegreeCandidate", args);
        } catch (FenixServiceException ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Error Reading without Role");
        }

        assertNull(newInfoMasterDegreeCandidate);
    }

    public void testCreateMasterDegreeCandidateWithoutRole() {
        System.out.println("- Test 3 : Create Master Degree Candidate without Role");

        UserView userView = this.getUserViewToBeTested("nmsn", false);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

        try {
            infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "CreateMasterDegreeCandidate", null);
        } catch (FenixServiceException ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Error Reading without Role");
        }
        assertNull(infoMasterDegreeCandidate);
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