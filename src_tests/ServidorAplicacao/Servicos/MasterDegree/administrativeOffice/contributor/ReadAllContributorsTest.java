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
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;

public class ReadAllContributorsTest extends TestCaseServicos {

    public ReadAllContributorsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadAllContributorsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAllContributors() {
        System.out.println("- Test 1 : Read All Contributors");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        List result = null;

        try {
            result = (List) ServiceManagerServiceFactory.executeService(userView, "ReadAllContributors",
                    null);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

        assertEquals(result.size(), 2);

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