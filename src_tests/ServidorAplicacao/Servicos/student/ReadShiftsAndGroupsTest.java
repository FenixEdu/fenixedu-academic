/*
 * 
 * Created on 29/09/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.student;

/**
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Turno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadShiftsAndGroupsTest extends TestCaseReadServices {

    public ReadShiftsAndGroupsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadShiftsAndGroupsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadShiftsAndGroups";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(6) };
        return result;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
        InfoSiteShift infoSiteShift = new InfoSiteShift();
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();

        List infoSiteGroupsByShiftList = new ArrayList();
        List infoSiteStudentGroupList = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();
            ITurno shift = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class, new Integer(30));
            IStudentGroup studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
                    StudentGroup.class, new Integer(10));

            infoSiteShift.setInfoShift((InfoShift) Cloner.get(shift));
            infoSiteShift.setNrOfGroups(new Integer(9));
            infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

            infoSiteStudentGroup.setInfoSiteStudentInformationList(null);
            infoSiteStudentGroup.setInfoStudentGroup(Cloner
                    .copyIStudentGroup2InfoStudentGroup(studentGroup));
            infoSiteStudentGroupList.add(infoSiteStudentGroup);

            infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupList);
            infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);

            infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoSiteShiftsAndGroups;

    }

}