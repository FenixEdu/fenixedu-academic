
package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.guide;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.PaymentType;
import net.sourceforge.fenixedu.util.RoleType;
import net.sourceforge.fenixedu.util.SituationOfGuide;
import net.sourceforge.fenixedu.util.State;

public class ChangeGuideSituationTest extends TestCaseServicos {

    public ChangeGuideSituationTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ChangeGuideSituationTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();

    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testCreateCandidateSituationNonExisting() {
        System.out.println("- Test 1 : Change Guide Situation");

        UserView userView = this.getUserViewToBeTested("nmsn", true);
        Integer guideNumber = new Integer(1);
        Integer guideYear = new Integer(2003);
        Integer guideVersion = new Integer(1);

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            IGuide guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear,
                    guideVersion);
            assertNotNull(guide);

            Iterator iterator = guide.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                IGuideSituation guideSituation = (IGuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    assertEquals(guideSituation.getSituation(), SituationOfGuide.PAYED_TYPE);
                }
            }

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        String remarks = "nota";

        Calendar date = Calendar.getInstance();
        date.set(2003, 5, 12);

        // Update the Payed Situation
        Object[] args = { guideNumber, guideYear, guideVersion, date.getTime(), remarks,
                SituationOfGuide.PAYED_STRING, PaymentType.ATM_STRING };

        try {
            ServiceManagerServiceFactory.executeService(userView, "ChangeGuideSituation", args);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            IGuide guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear,
                    guideVersion);
            assertNotNull(guide);

            Iterator iterator = guide.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                IGuideSituation guideSituation = (IGuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    assertEquals(guideSituation.getSituation(), SituationOfGuide.PAYED_TYPE);
                    assertEquals(guide.getPaymentType(), PaymentType.ATM_TYPE);
                    assertEquals(guide.getPaymentDate(), date.getTime());
                    assertEquals(guideSituation.getRemarks(), remarks);
                }
            }

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }

        // Change the Situation to Annuled
        Object args2[] = { guideNumber, guideYear, guideVersion, null, remarks,
                SituationOfGuide.ANNULLED_STRING, null };

        try {
            ServiceManagerServiceFactory.executeService(userView, "ChangeGuideSituation", args2);
        } catch (FenixServiceException ex) {
            fail("Fenix Service Exception" + ex);
        } catch (Exception ex) {
            fail("Eception");
        }

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentGuide persistentGuide = sp.getIPersistentGuide();
            IGuide guide = persistentGuide.readByNumberAndYearAndVersion(guideNumber, guideYear,
                    guideVersion);
            assertNotNull(guide);

            Iterator iterator = guide.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                IGuideSituation guideSituation = (IGuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    assertEquals(guideSituation.getSituation(), SituationOfGuide.ANNULLED_TYPE);
                }
            }

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
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