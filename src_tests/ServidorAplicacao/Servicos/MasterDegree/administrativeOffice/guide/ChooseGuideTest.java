
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide;

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
import DataBeans.InfoGuide;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicesIntranet;
import Util.RoleType;

public class ChooseGuideTest extends TestCaseServicesIntranet {

    public ChooseGuideTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ChooseGuideTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testChooseGuideByYearAndNumber() {
        System.out.println("- Test 1 : Choose Guide By Number and Year");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2002);

        Object[] args = { guideNumber, guideYear };

        List result = null;
        try {
            result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(result);
        assertTrue(!result.isEmpty());
        assertEquals(result.size(), 2);

        guideYear = new Integer(2003);

        Object[] args2 = { guideNumber, guideYear };

        result = null;
        try {
            result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide", args2);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(result);
        assertTrue(!result.isEmpty());
        assertEquals(result.size(), 1);

    }

    public void testChooseGuideByYearAndNumberAndVersion() {
        System.out.println("- Test 2 : Choose Guide By Number, Year and Version");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2002);
        Integer guideVersion = new Integer(1);

        Object[] args = { guideNumber, guideYear, guideVersion };

        InfoGuide infoGuide = null;
        try {
            infoGuide = (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                    args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(infoGuide);
        assertEquals(infoGuide.getNumber(), guideNumber);
        assertEquals(infoGuide.getYear(), guideYear);
        assertEquals(infoGuide.getVersion(), guideVersion);

    }

    public void testChooseGuideByYear() {
        System.out.println("- Test 3 : Choose Guide By Year");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideYear = new Integer(2002);

        Object[] args = { guideYear };

        List result = null;
        try {
            result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(result);
        assertEquals(result.size(), 1);

        guideYear = new Integer(2003);

        args[0] = guideYear;

        result = null;
        try {
            result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        assertNotNull(result);
        assertEquals(result.size(), 3);

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