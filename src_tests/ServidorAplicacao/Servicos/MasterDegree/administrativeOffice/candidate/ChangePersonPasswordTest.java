/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.RoleType;

public class ChangePersonPasswordTest extends TestCaseServicos {

    public ChangePersonPasswordTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ChangePersonPasswordTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testChangePersonPassword() {
        System.out.println("- Test 1 : Change Person Password");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        InfoMasterDegreeCandidate infoMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");

        InfoPerson infoPerson = infoMasterDegreeCandidate.getInfoPerson();
        infoPerson.setPassword("pass2");

        Object[] args = { infoPerson };

        InfoMasterDegreeCandidate changedMasterDegreeCandidate = null;

        try {
            ServiceManagerServiceFactory.executeService(userView, "ChangePersonPassword", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception");
        } catch (Exception ex) {
            fail("Exception");
        }

        changedMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");

        assertTrue(PasswordEncryptor.areEquals(changedMasterDegreeCandidate.getInfoPerson()
                .getPassword(), "pass2"));

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

    private InfoMasterDegreeCandidate readMasterDegreeCandidate(String username) {
        ISuportePersistente sp = null;
        List result = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            sp.iniciarTransaccao();
            result = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidatesByUsername(
                    username);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                //ignored
            }
            e.printStackTrace();
            fail("Error !");
        }

        return Cloner
                .copyIMasterDegreeCandidate2InfoMasterDegreCandidate((IMasterDegreeCandidate) result
                        .get(0));
    }

}