/*
 * 
 * Created on 29/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.util.Cloner;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadShiftsAndGroupsTest extends TestCaseReadServices
{

    public ReadShiftsAndGroupsTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(ReadShiftsAndGroupsTest.class);

        return suite;
    }

    protected void setUp()
    {
        super.setUp();
    }

    protected void tearDown()
    {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadShiftsAndGroups";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] result = { new Integer(6)};
        return result;

    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
        InfoSiteShift infoSiteShift = new InfoSiteShift();
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();

        List infoSiteGroupsByShiftList = new ArrayList();
        List infoSiteStudentGroupList = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            ITurno shift =
                (ITurno) sp.getITurnoPersistente().readByOId(new Turno(new Integer(30)), false);
            IStudentGroup studentGroup =
                (IStudentGroup) sp.getIPersistentStudentGroup().readByOId(
                    new StudentGroup(new Integer(10)),
                    false);

            infoSiteShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
            infoSiteShift.setNrOfGroups(new Integer(9));
            infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

            infoSiteStudentGroup.setInfoSiteStudentInformationList(null);
            infoSiteStudentGroup.setInfoStudentGroup(
                Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup));
            infoSiteStudentGroupList.add(infoSiteStudentGroup);

            infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupList);
            infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);

            infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

            sp.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        return infoSiteShiftsAndGroups;

    }

}
