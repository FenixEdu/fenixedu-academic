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
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;

public class ReadContributorListTest extends TestCaseServicos {

    public ReadContributorListTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadContributorListTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadContributorList() {
        System.out.println("- Test 1 : Read Contributor List");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoContributor infoContributor = null;

        Object[] args = { new Integer(123) };

        try {
            infoContributor = (InfoContributor) ServiceManagerServiceFactory.executeService(userView,
                    "ReadContributor", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

        assertNotNull(infoContributor);
        assertEquals(infoContributor.getContributorNumber(), new Integer(123));

        Object[] args2 = { new Integer(9999) };

        infoContributor = null;

        try {
            infoContributor = (InfoContributor) ServiceManagerServiceFactory.executeService(userView,
                    "ReadContributor", args2);
        } catch (ExcepcaoInexistente ex) {
            // All is OK
        } catch (Exception ex) {
            fail("Exception");
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