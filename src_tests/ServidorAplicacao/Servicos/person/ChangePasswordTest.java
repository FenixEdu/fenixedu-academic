/*
 * Created on 17/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Servicos.person;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRole;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author jpvl
 */
public class ChangePasswordTest extends TestCaseNeedAuthorizationServices {

    /**
     * @param testName
     */
    public ChangePasswordTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ChangePasswordTest.class);
        return suite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ChangePasswordService";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#needsAuthorization()
     */
    protected boolean needsAuthorization() {
        return true;
    }

    public void testSuccessfullChangePassword() {
        UserView userView = getUserViewToBeTested("user");

        Object args[] = { userView, "pass", "pass2" };
        try {
            ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), args);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Calling service " + getNameOfServiceToBeTested() + "!");
        }
        IPessoa person = readPerson("user");
        assertEquals("Passwords aren't equals!", PasswordEncryptor.encryptPassword("pass2"), person
                .getPassword());
    }

    public void testUnsucessfullChangePassword() {
        UserView userView = getUserViewToBeTested("user");

        Object args[] = { userView, "pass2", "pass3" };
        try {
            ServiceUtils.executeService(userView, getNameOfServiceToBeTested(), args);
            fail("Must throw " + InvalidPasswordServiceException.class.getName() + " exception.");
        } catch (InvalidPasswordServiceException e) {
            //ok
        } catch (Exception e) {
            e.printStackTrace();
            fail("Calling service " + getNameOfServiceToBeTested() + "!");
        }

        IPessoa person = readPerson("user");
        assertEquals("Password must not change!", PasswordEncryptor.encryptPassword("pass"), person
                .getPassword());
    }

    private UserView getUserViewToBeTested(String username) {
        Collection roles = new ArrayList();
        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.PERSON);
        roles.add(infoRole);
        UserView userView = new UserView(username, roles);
        return userView;
    }

    private IPessoa readPerson(String username) {
        IPessoa person = null;
        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            sp.iniciarTransaccao();
            person = personDAO.lerPessoaPorUsername(username);
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                //ignored
            }
            e.printStackTrace();
            fail("Error reading person to test equal password!");
        }
        return person;
    }
}