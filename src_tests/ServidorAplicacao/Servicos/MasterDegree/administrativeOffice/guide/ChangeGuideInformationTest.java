package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangeGuideInformationTest extends TestCaseServicos {

    public ChangeGuideInformationTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ChangeGuideInformationTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testChangeContributor() {
        System.out.println("- Test 1 : Change Guide Information (Change the Contributor) ");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2003);
        Integer guideVersion = new Integer(1);

        ISuportePersistente sp = null;
        IGuide guide = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
            assertNotNull(guide);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        String quantityList[] = { "1", "2", "1" };
        Integer contributorNumber = new Integer(456);

        // Update The Contributor
        InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        Object[] args = { infoGuide, quantityList, contributorNumber, null, null, null };

        InfoGuide result = null;

        try {
            result = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "EditGuideInformation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Exception " + ex);
        }

        assertNotNull(result);

        assertEquals(result.getVersion(), new Integer(2));
        assertEquals(result.getInfoContributor().getContributorNumber(), contributorNumber);
    }

    public void testDeleteOneGuideLine() {
        System.out.println("- Test 2 : Change Guide Information (Delete One Line) ");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2003);
        Integer guideVersion = new Integer(1);

        ISuportePersistente sp = null;
        IGuide guide = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
            assertNotNull(guide);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        String quantityList[] = { "0", "2", "1" };
        Integer contributorNumber = new Integer(456);

        // Update The Contributor
        InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        Object[] args = { infoGuide, quantityList, contributorNumber, null, null, null };

        InfoGuide result = null;

        assertEquals(infoGuide.getInfoGuideEntries().size(), 3);
        assertEquals(infoGuide.getTotal(), new Double(50.02));

        try {
            result = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "EditGuideInformation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Exception " + ex);
        }

        assertNotNull(result);

        assertEquals(result.getVersion(), new Integer(2));
        assertEquals(result.getInfoGuideEntries().size(), 2);
        assertEquals(result.getTotal(), new Double(40));

    }

    public void testChangeTheQuantities() {
        System.out.println("- Test 3 : Change Guide Information (Change Quantities) ");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2003);
        Integer guideVersion = new Integer(1);

        ISuportePersistente sp = null;
        IGuide guide = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
            assertNotNull(guide);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        String quantityList[] = { "2", "3", "4" };
        Integer contributorNumber = new Integer(456);

        // Update The Contributor
        InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        Object[] args = { infoGuide, quantityList, contributorNumber, null, null, null };

        InfoGuide result = null;

        assertEquals(infoGuide.getTotal(), new Double(50.02));

        try {
            result = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "EditGuideInformation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Exception " + ex);
        }

        assertNotNull(result);

        assertEquals(result.getVersion(), new Integer(2));
        assertEquals(result.getTotal(), new Double(105.04));

    }

    public void testAddNewLine() {
        System.out.println("- Test 4 : Change Guide Information (Add New Line) ");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2003);
        Integer guideVersion = new Integer(1);

        ISuportePersistente sp = null;
        IGuide guide = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear, guideVersion);
            assertNotNull(guide);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        String quantityList[] = { "1", "2", "1" };
        Integer contributorNumber = new Integer(456);

        // Update The Contributor
        InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        Object[] args = { infoGuide, quantityList, contributorNumber, "Stamps", new Integer(2),
                new Double(1.15) };

        InfoGuide result = null;

        assertEquals(infoGuide.getTotal(), new Double(50.02));
        assertEquals(infoGuide.getInfoGuideEntries().size(), 3);

        try {
            result = (InfoGuide) ServiceManagerServiceFactory.executeService(userView,
                    "EditGuideInformation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Exception " + ex);
        }

        assertNotNull(result);

        assertEquals(result.getVersion(), new Integer(2));
        assertEquals(result.getInfoGuideEntries().size(), 4);
        assertEquals(result.getTotal(), new Double(52.32));

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