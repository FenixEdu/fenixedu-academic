/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.contributor;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingContributorServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.RoleType;

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