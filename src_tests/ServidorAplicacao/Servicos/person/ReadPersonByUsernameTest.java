/*
 * Created on 17/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.person;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.RoleType;

/**
 * @author jpvl
 */
public class ReadPersonByUsernameTest extends TestCaseNeedAuthorizationServices {

    /**
     * @param testName
     */
    public ReadPersonByUsernameTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadPersonByUsernameTest.class);
        return suite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadPersonByUsername";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#needsAuthorization()
     */
    protected boolean needsAuthorization() {
        return true;
    }

    public void testSuccessfullReadPersonByUsername() {
        UserView userView = getUserViewToBeTested("user");

        Object args[] = { userView };
        InfoPerson infoPerson = null;
        try {
            infoPerson = (InfoPerson) ServiceUtils.executeService(userView,
                    getNameOfServiceToBeTested(), args);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Calling service " + getNameOfServiceToBeTested() + "!");
        }
        assertNotNull(infoPerson);
        assertEquals("user", infoPerson.getUsername());

    }

    public void testUnsucessfullReadPersonByUsername() {
        UserView userView = getUserViewToBeTested("nonexisting");

        Object args[] = { userView };
        try {
            ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), args);
            fail("Must throw " + InvalidPasswordServiceException.class.getName() + " exception.");
        } catch (FenixServiceException e) {
            // All is OK
        } catch (Exception e) {
            e.printStackTrace();
            fail("Calling service " + getNameOfServiceToBeTested() + "!");
        }
    }

    private UserView getUserViewToBeTested(String username) {
        Collection roles = new ArrayList();
        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.PERSON);
        roles.add(infoRole);
        UserView userView = new UserView(username, roles);
        return userView;
    }

}