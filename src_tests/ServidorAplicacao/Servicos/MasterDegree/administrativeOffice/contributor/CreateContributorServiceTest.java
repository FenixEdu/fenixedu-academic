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
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;

public class CreateContributorServiceTest extends TestCaseServicos {

    public CreateContributorServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CreateContributorServiceTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testCreateContributor() {
        System.out.println("- Test 1 : Create Contributor");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoContributor infoContributor = new InfoContributor();
        infoContributor.setContributorNumber(new Integer(1));
        infoContributor.setContributorName("nomeC");
        infoContributor.setContributorAddress("moradaC");

        Object[] args = { infoContributor };

        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateContributor", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

    }

    public void testCreateContributorExisting() {
        System.out.println("- Test 2 : Create Existing Contributor");

        UserView userView = this.getUserViewToBeTested("jccm", true);

        InfoContributor infoContributor = new InfoContributor();
        infoContributor.setContributorNumber(new Integer(123));
        infoContributor.setContributorName("Nome1");
        infoContributor.setContributorAddress("Morada1");

        Object[] args = { infoContributor };

        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateContributor", args);
        } catch (FenixServiceException ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Exception");
        }
    }

    public void testCreateContributorWithoutRole() {
        System.out.println("- Test 3 : Create Contributor without Role");

        UserView userView = this.getUserViewToBeTested("nmsn", false);

        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateContributor", null);
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