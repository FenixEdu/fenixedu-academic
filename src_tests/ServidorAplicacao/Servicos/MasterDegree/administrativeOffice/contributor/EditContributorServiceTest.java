/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.contributor;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoContributor;
import DataBeans.InfoRole;
import Dominio.IContributor;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingContributorServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class EditContributorServiceTest extends TestCaseServicos {

    public EditContributorServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditContributorServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testEditContributor() {
        System.out.println("- Test 1 : Edit Contributor");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoContributor infoContributor = new InfoContributor();
        infoContributor.setContributorNumber(new Integer(123));
        infoContributor.setContributorName("nome do Nuno");
        infoContributor.setContributorAddress("Rua do Nuno");

        Object[] args = { infoContributor, new Integer(1), "nome do Nuno1", "Rua do Nuno1" };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditContributor", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

        // Check the edition
        try {
            SuportePersistenteOJB.getInstance().iniciarTransaccao();
            IContributor contributor = SuportePersistenteOJB.getInstance().getIPersistentContributor()
                    .readByContributorNumber(new Integer(1));
            SuportePersistenteOJB.getInstance().confirmarTransaccao();

            assertNotNull(contributor);
            assertEquals(contributor.getContributorAddress(), "Rua do Nuno1");
            assertEquals(contributor.getContributorName(), "nome do Nuno1");
            assertEquals(contributor.getContributorNumber(), new Integer(1));
        } catch (Exception e) {
            fail("Error reading Existing");
        }

    }

    public void testEditNonExistingContributor() {
        System.out.println("- Test 2 : Edit Non Existing Contributor");

        UserView userView = this.getUserViewToBeTested("jccm", true);

        InfoContributor infoContributor = new InfoContributor();
        infoContributor.setContributorNumber(new Integer(13));
        infoContributor.setContributorName("Nome13");
        infoContributor.setContributorAddress("Morada13");

        Object[] args = { infoContributor, new Integer(13), "Nome1", "Morada1" };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditContributor", args);
        } catch (NonExistingContributorServiceException ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Exception");
        }
    }

    public void testEditContributorWithoutRole() {
        System.out.println("- Test 3 : Edit Contributor without Role");

        UserView userView = this.getUserViewToBeTested("nmsn", false);

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditContributor", null);
        } catch (FenixServiceException ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Error Reading without Role");
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