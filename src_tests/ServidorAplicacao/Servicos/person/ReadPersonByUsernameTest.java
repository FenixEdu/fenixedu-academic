/*
 * Created on 17/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.person;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseNeedAuthorizationServices;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.RoleType;

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